package se.lundakarnevalen.extern.fragments;

import android.content.Context;
import android.graphics.Picture;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ViewFlipper;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import se.lundakarnevalen.extern.android.ContentActivity;
import se.lundakarnevalen.extern.android.R;
import se.lundakarnevalen.extern.util.Delay;
import se.lundakarnevalen.extern.util.Timer;
import se.lundakarnevalen.extern.widget.SVGView;

import static se.lundakarnevalen.extern.util.ViewUtil.get;

/**
 * Created by Markus on 2014-04-16.
 */
public class TrainMapFragment extends LKFragment{
    private static FutureTask<Picture> preloaded = null;

    public static Picture preload(Context c) {
        if(preloaded == null){
            preloaded = new FutureTask<Picture>(new SvgLoader(c));
            new AsyncTask<Void,Void,Void>(){
                @Override
                protected Void doInBackground(Void... params) {
                    preloaded.run();
                    return null;
                }
            }.execute();
        }
        try {
            return preloaded.get(1, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Log.wtf(LOG_TAG, "Future was interrupted", e);
        } catch (ExecutionException e) {
            Log.wtf(LOG_TAG, "ExecutionException", e);
        } catch (TimeoutException e) {}
        return null;
    }

    public static void clean(){
        preloaded = null;
    }

    private static final String LOG_TAG = TrainMapFragment.class.getSimpleName();
    private static final String STATE_MATRIX = "matrix";

    private float[] mMatrixValues;

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

        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Picture picture = preloaded.get(20, TimeUnit.SECONDS);
                    waitForLayout();
                    final float scale = calculateMinZoom(img, picture) / 2.0f;
                    img.setSvg(picture, scale, null);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            img.zoomTo(420.0f, 300.0f, scale);
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
                    Picture picture = new SvgLoader(inflater.getContext()).call();
                    waitForLayout();
                    final float scale = calculateMinZoom(img, picture) / 2.0f;
                    img.setSvg(picture, scale, null);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            img.zoomTo(420.0f, 300.0f, scale);
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

    public static class SvgLoader implements Callable<Picture> {
        private Context c;

        public SvgLoader(Context c) {
            this.c = c;
        }

        @Override
        public Picture call() throws Exception {
            try {
                Timer t = new Timer();
                SVG svg = SVG.getFromResource(c, R.raw.train_map_cleaned);
                t.tick(LOG_TAG, "getFromResource()");
                Picture pic = svg.renderToPicture();
                t.tick(LOG_TAG, "renderToPicture()");
                return pic;
            } catch (SVGParseException e) {
                Log.wtf(LOG_TAG, "This wont happen");
                return null;
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(LOG_TAG, "onSaveInstanceState() called");
        outState.putFloatArray(STATE_MATRIX, img.exportMatrixValues());
    }

    public static TrainMapFragment create() {
        Bundle bundle = new Bundle();
        TrainMapFragment fragment = new TrainMapFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
}
