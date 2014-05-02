package se.lundakarnevalen.extern.fragments;

import android.content.Context;
import android.graphics.Picture;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ViewFlipper;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;

import java.util.Collection;
import java.util.Random;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import se.lundakarnevalen.extern.android.R;
import se.lundakarnevalen.extern.util.Delay;
import se.lundakarnevalen.extern.util.Timer;
import se.lundakarnevalen.extern.widget.LKMapView;

import static se.lundakarnevalen.extern.util.ViewUtil.get;

public class MapFragment extends LKFragment {
    private static FutureTask<Picture> preloaded = null;
    private java.util.Timer mTimer;

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

    private static final String LOG_TAG = MapFragment.class.getSimpleName();
    private static final String STATE_MATRIX = "matrix";

    private float[] mMatrixValues;
    private int displayWidth;
    private int displayHeight;
    private LKMapView mapView;

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
        final View root = inflater.inflate(R.layout.fragment_map, container, false);

        DisplayMetrics metrics = inflater.getContext().getResources().getDisplayMetrics();

        displayWidth = metrics.widthPixels;
        displayHeight = metrics.heightPixels;

        mapView = get(root, R.id.map_id, LKMapView.class);

        final ViewFlipper flipper = get(root, R.id.map_switcher, ViewFlipper.class);

        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Picture picture = preloaded.get(20, TimeUnit.SECONDS);
                    waitForLayout();
                    float minZoom = calculateMinZoom(mapView, picture);
                    mapView.setSvg(picture, minZoom, mMatrixValues);
                } catch (InterruptedException e) {
                    Log.wtf(LOG_TAG, "Future was interrupted", e);
                } catch (ExecutionException e) {
                    Log.wtf(LOG_TAG, "ExecutionException", e);
                } catch (TimeoutException e) {
                    try{
                        Picture picture = new SvgLoader(inflater.getContext()).call();
                        waitForLayout();
                        float minZoom = calculateMinZoom(mapView, picture);
                        mapView.setSvg(picture, minZoom, mMatrixValues);
                    } catch (Exception ex){
                        Log.wtf(LOG_TAG, "Failed to load image after timeout", ex);
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                flipper.showNext();
            }
        }.execute();

        mTimer = new java.util.Timer();
        mTimer.scheduleAtFixedRate(new TimerTask() {
            private Random r = new Random();
            @Override
            public void run() {
                mapView.setGpsMarker(MapFragment.this, r.nextInt(512), r.nextInt(512));
            }
        }, 0, 5000);

        flipper.setAnimateFirstView(true);
        flipper.setInAnimation(AnimationUtils.loadAnimation(inflater.getContext(), R.anim.abc_fade_in));
        flipper.setOutAnimation(AnimationUtils.loadAnimation(inflater.getContext(), R.anim.abc_fade_out));
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mMatrixValues != null) {
            mapView.importMatrixValues(mMatrixValues);
        }
    }

    @Override
    public void onPause() {
        mMatrixValues = mapView.exportMatrixValues();
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        mTimer.cancel();
        super.onDestroyView();
    }

    private void waitForLayout() {
        int counter = 0;
        while (mapView.getMeasuredHeight() == 0 && counter++ < 100) Delay.ms(100); //Wait for layout
        mapView.updateViewLimitBounds();
    }

    private float calculateMinZoom(View root, Picture pic) {
        //We assume that the svg image is 512x512 for now
        return Math.max(
                    root.getMeasuredHeight() * 1.0f / pic.getHeight(),
                    root.getMeasuredWidth() * 1.0f / pic.getWidth());
    }

    public void setActiveType(Collection<Integer> types) {
        mapView.setActiveTypes(types);
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
                SVG svg = SVG.getFromResource(c, R.raw.kartabeta6_cleaned);
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
        outState.putFloatArray(STATE_MATRIX, mapView.exportMatrixValues());
    }

    public static MapFragment create(boolean zoom, float lat, float lng) {
        Bundle bundle = new Bundle();
        bundle.putFloat("lat", lat);
        bundle.putFloat("lng", lng);
        bundle.putBoolean("zoom", zoom);
        MapFragment fragment = new MapFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
}
