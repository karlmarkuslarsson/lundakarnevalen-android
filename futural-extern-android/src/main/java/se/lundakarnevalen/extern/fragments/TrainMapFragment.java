package se.lundakarnevalen.extern.fragments;

import android.graphics.Picture;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import se.lundakarnevalen.extern.android.ContentActivity;
import se.lundakarnevalen.extern.android.R;
import se.lundakarnevalen.extern.map.GPSTracker;
import se.lundakarnevalen.extern.map.LocationTracker;
import se.lundakarnevalen.extern.map.MapLoader;
import se.lundakarnevalen.extern.map.TrainMapLoader;
import se.lundakarnevalen.extern.util.Delay;
import se.lundakarnevalen.extern.util.Logf;
import se.lundakarnevalen.extern.widget.LKTrainView;

import static se.lundakarnevalen.extern.util.ViewUtil.get;

/**
 * Created by Markus on 2014-04-16.
 */
public class TrainMapFragment extends LKFragment implements GPSTracker.GPSListener,
        LocationTracker.LocationJSONListener, TrainMapLoader.MapLoaderCallback {
    private static final String LOG_TAG = TrainMapFragment.class.getSimpleName();

    private static final String STATE_MATRIX = "matrix";
    private static final int VIEWFLIPPER_CHILD_MAP = 1;

    private float[] mMatrixValues;
    private MediaPlayer mMediaPlayer;
    private boolean isGPSWithinMap = false;
    private float mGPSMarkerLng = -1.0f;
    private float mGPSMarkerLat = -1.0f;

    private LocationTracker.LocationJSONResult.LatLng mTrainPos = new LocationTracker.LocationJSONResult.LatLng();

    private LKTrainView mTrainView;
    private ViewFlipper mViewFlipper;
    private View mSpinnerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null && savedInstanceState.containsKey(STATE_MATRIX)){
            mMatrixValues = savedInstanceState.getFloatArray(STATE_MATRIX);
            Logf.d(LOG_TAG, "Matrix values restored: %s", String.valueOf(mMatrixValues));
        }

        setRetainInstance(true);
    }

    // Every time you switch to this fragment.
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_map_train, container, false);
        mViewFlipper = get(root, R.id.map_switcher, ViewFlipper.class);
        mSpinnerView = get(root, R.id.map_spinner, View.class);
        mTrainView = get(root, R.id.map_id, LKTrainView.class);

        Bundle bundle = getArguments();

        if(bundle.getBoolean("sound")) {
            mMediaPlayer = MediaPlayer.create(getContext(), R.raw.train_sound);
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.release();
                }

            });
            mMediaPlayer.start();
        }

        final Runnable onSvgLoaded = new Runnable() {
            @Override
            public void run() {
                mTrainView.panTo(220f, 265f, false);
                mViewFlipper.setDisplayedChild(VIEWFLIPPER_CHILD_MAP);
            }
        };

        if(TrainMapLoader.hasLoadedMap()){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Picture p = TrainMapLoader.getMapTrain();
                    float minZoom = calculateMinZoom(mTrainView, p);
                    mTrainView.setSvg(p, minZoom, null);
                    clearSpinner();
                }
            }, 400);
        } else {
            new TrainMapLoader.MapSvgLoader(this).startWait(); // Wait for maps async
        }

        mViewFlipper.setAnimateFirstView(true);
        mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(inflater.getContext(), R.anim.abc_fade_in));
        mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(inflater.getContext(), R.anim.abc_fade_out));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mTrainView.setGpsMarker(mGPSMarkerLat, mGPSMarkerLng, false);
                mTrainView.setTrainLocation(mTrainPos);
            }
        }, 300);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        ContentActivity.class.cast(getActivity()).allBottomsUnfocus();
        if(mMatrixValues != null) {
            mTrainView.importMatrixValues(mMatrixValues);
        }
    }

    @Override
    public void onPause() {
        mMatrixValues = mTrainView.exportMatrixValues();
        super.onPause();
    }

    @Override
    public void onStop() {
        ContentActivity.class.cast(getActivity()).inactivateTrainButton();
        ContentActivity.class.cast(getActivity()).unregisterForLocationUpdates(this, this);
        if(mMediaPlayer != null) {
            try{
                mMediaPlayer.release();
            } catch(Exception e){}
        }

        super.onStop();
    }

    @Override
    public void onStart() {
        ContentActivity.class.cast(getActivity()).registerForLocationUpdates(this, this);
        ContentActivity.class.cast(getActivity()).activateMapButton();
        super.onStart();
    }

    private void waitForLayout() {
        int counter = 0;
        while (mTrainView.getMeasuredHeight() == 0 && counter++ < 100) Delay.ms(100); //Wait for layout
        mTrainView.updateViewLimitBounds();
    }

    private float calculateMinZoom(View root, Picture pic) {
        return Math.max(
                root.getMeasuredHeight() * 1.0f / pic.getHeight(),
                root.getMeasuredWidth() * 1.0f / pic.getWidth());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(LOG_TAG, "onSaveInstanceState() called");
        if(mTrainView!=null) {
            outState.putFloatArray(STATE_MATRIX, mTrainView.exportMatrixValues());
        }
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

    public void zoomToMarker() {
        if(isGPSWithinMap){
            float[] dst = new float[2];
            mTrainView.zoom(mTrainView.mMidZoom);
            mTrainView.panToCenterFast();
            mTrainView.getPointFromCoordinates(mGPSMarkerLat, mGPSMarkerLng, dst);
            mTrainView.panTo(dst[0], dst[1]);
        } else {
            Toast.makeText(getContext(), "Du är utanför området eller har ej GPS aktiverad", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onNewLocation(double lat, double lng) {
        Logf.d(LOG_TAG, "onNewLocation(lat: %f, lng: %f)", lat, lng);
        mGPSMarkerLat = (float) lat;
        mGPSMarkerLng = (float) lng;
        mTrainView.setGpsMarker(mGPSMarkerLat, mGPSMarkerLng, false);
        if (mTrainView.isWithinLatLngRange((float) lat, (float) lng)) {
            isGPSWithinMap = true;
        } else {
            isGPSWithinMap = false;
        }
    }

    @Override
    public void onNewLocationFromKarnevalist(LocationTracker.LocationJSONResult json) {
        if (json == null) return;
        Log.d(LOG_TAG, "Result: " + json.toString());
        if(json.success) {
            for(LocationTracker.LocationJSONResult.LatLng p : json.train_positions) {
                switch(p.id) {
                    case 1:
                        Log.d(LOG_TAG, p.id+" - lat: "+p.lat+" lng: "+p.lng);
                        mTrainPos = p;
                        mTrainView.setTrainLocation(p);
                        break;
                }
            }
        }
    }

    @Override
    public void postTrainMap(Picture picture) {
        waitForLayout();
        float minZoom = calculateMinZoom(mTrainView, picture);
        mTrainView.setSvg(picture, minZoom, null);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                clearSpinner();
            }
        });
    }

    private void clearSpinner() {
        mViewFlipper.setDisplayedChild(VIEWFLIPPER_CHILD_MAP);
        mSpinnerView.clearAnimation();
    }
}
