package se.lundakarnevalen.extern.fragments;

import android.graphics.Picture;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ViewFlipper;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import se.lundakarnevalen.extern.android.ContentActivity;
import se.lundakarnevalen.extern.android.R;
import se.lundakarnevalen.extern.map.GPSTracker;
import se.lundakarnevalen.extern.map.TrainMapLoader;
import se.lundakarnevalen.extern.util.Delay;
import se.lundakarnevalen.extern.util.Logf;
import se.lundakarnevalen.extern.widget.SVGView;

import static se.lundakarnevalen.extern.util.ViewUtil.get;

/**
 * Created by Markus on 2014-04-16.
 */
public class TrainMapFragment extends LKFragment implements GPSTracker.GPSListener {
    private static final String LOG_TAG = TrainMapFragment.class.getSimpleName();

    private static final String STATE_MATRIX = "matrix";

    private float[] mMatrixValues;
    private MediaPlayer mp;
    private SVGView img;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null && savedInstanceState.containsKey(STATE_MATRIX)){
            Log.d(LOG_TAG, "Matrix values restored");
            mMatrixValues = savedInstanceState.getFloatArray(STATE_MATRIX);
        }

        setRetainInstance(true);
    }

    // Every time you switch to this fragment.
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_map_train, container, false);

        img = get(root, R.id.map_id, SVGView.class);

        final ViewFlipper flipper = get(root, R.id.map_switcher, ViewFlipper.class);

        Bundle bundle = getArguments();

        if(bundle.getBoolean("sound")) {

            mp = MediaPlayer.create(getContext(), R.raw.train_sound);

            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.release();
                }

            });

            mp.start();
        }

        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Picture picture = TrainMapLoader.preload(inflater.getContext()).get(20, TimeUnit.SECONDS);
                    waitForLayout();
                    final float scale = calculateMinZoom(img, picture) / 2.0f;
                    img.setSvg(picture, scale, null);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            img.panTo(420.0f, 300.0f);
                        }
                    });
                } catch (InterruptedException e) {
                    Log.wtf(LOG_TAG, "Future was interrupted", e);
                } catch (ExecutionException e) {
                    Log.wtf(LOG_TAG, "ExecutionException", e);
                } catch (TimeoutException e) {
                    loadMap();
                }
                return null;
            }

            private void loadMap() {
                try{
                    Picture picture = new TrainMapLoader(inflater.getContext()).call();
                    waitForLayout();
                    final float scale = calculateMinZoom(img, picture) / 2.0f;
                    img.setSvg(picture, scale, null);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            img.panTo(420.0f, 300.0f);
                        }
                    });
                } catch (Exception ex){
                    Log.wtf(LOG_TAG, "Failed to load image after timeout", ex);
                }
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                flipper.showNext();
            }
        }.execute();

        flipper.setAnimateFirstView(true);
        flipper.setInAnimation(AnimationUtils.loadAnimation(inflater.getContext(), R.anim.abc_fade_in));
        flipper.setOutAnimation(AnimationUtils.loadAnimation(inflater.getContext(), R.anim.abc_fade_out));
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        ContentActivity.class.cast(getActivity()).allBottomsUnfocus();
        if(mMatrixValues != null) {
            img.importMatrixValues(mMatrixValues);
        }
    }

    @Override
    public void onPause() {
        mMatrixValues = img.exportMatrixValues();
        super.onPause();
    }

    @Override
    public void onStop() {
        ContentActivity.class.cast(getActivity()).inactivateTrainButton();
        ContentActivity.class.cast(getActivity()).unregisterForLocationUpdates(this);
        super.onStop();
    }

    @Override
    public void onStart() {
        ContentActivity.class.cast(getActivity()).activateTrainButton();
        ContentActivity.class.cast(getActivity()).registerForLocationUpdates(this);
        super.onStart();
    }


    private void waitForLayout() {
        int counter = 0;
        while (img.getMeasuredHeight() == 0 && counter++ < 100) Delay.ms(100); //Wait for layout
        img.updateViewLimitBounds();
    }

    private float calculateMinZoom(View root, Picture pic) {
        //We assume that the svg image is 512x512 for now
        return Math.max(
                root.getMeasuredHeight() * 1.0f / pic.getHeight(),
                root.getMeasuredWidth() * 1.0f / pic.getWidth());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(LOG_TAG, "onSaveInstanceState() called");
        outState.putFloatArray(STATE_MATRIX, img.exportMatrixValues());
    }

    public static TrainMapFragment create(boolean startSound) {
        TrainMapFragment fragment = new TrainMapFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("sound", startSound);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static TrainMapFragment create() {
        Bundle bundle = new Bundle();
        TrainMapFragment fragment = new TrainMapFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onNewLocation(double lat, double lng) {
        Logf.d(LOG_TAG, "Got new location (%f, %f)", lat, lng);
    }
}
