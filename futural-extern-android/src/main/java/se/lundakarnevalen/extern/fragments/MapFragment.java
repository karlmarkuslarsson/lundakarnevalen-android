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

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import se.lundakarnevalen.extern.android.R;
import se.lundakarnevalen.extern.util.Timer;
import se.lundakarnevalen.extern.widget.SVGMapView;

import static se.lundakarnevalen.extern.util.ViewUtil.get;

public class MapFragment extends LKFragment {
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

    private static final String LOG_TAG = MapFragment.class.getSimpleName();
    private static final String STATE_MATRIX = "matrix";

    private int imageWidth;
    private int imageHeight;

    private SVGMapView img;

    // Every time you switch to this fragment.
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        DisplayMetrics metrics = inflater.getContext().getResources().getDisplayMetrics();
        imageWidth = metrics.widthPixels;
        imageHeight = metrics.heightPixels;

        img = get(rootView, R.id.map_id, SVGMapView.class);

        final ViewFlipper flipper = get(rootView, R.id.map_switcher, ViewFlipper.class);

        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Picture picture = preloaded.get(20, TimeUnit.SECONDS);
                    img.setSvg(picture, imageWidth, imageHeight);
                } catch (InterruptedException e) {
                    Log.wtf(LOG_TAG, "Future was interrupted", e);
                } catch (ExecutionException e) {
                    Log.wtf(LOG_TAG, "ExecutionException", e);
                } catch (TimeoutException e) {
                    try{
                        Picture picture = new SvgLoader(inflater.getContext()).call();
                        img.setSvg(picture, imageWidth, imageHeight);
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

        flipper.setAnimateFirstView(true);
        flipper.setInAnimation(AnimationUtils.loadAnimation(inflater.getContext(), R.anim.abc_fade_in));
        flipper.setOutAnimation(AnimationUtils.loadAnimation(inflater.getContext(), R.anim.abc_fade_out));

        if(savedInstanceState != null && savedInstanceState.containsKey(STATE_MATRIX)){
            Log.d(LOG_TAG, "Matrix values restored");
            img.setMatrixValues(savedInstanceState.getFloatArray(STATE_MATRIX));
        }

        //setRetainInstance(true);

        return rootView;
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
                SVG svg = SVG.getFromResource(c, R.raw.map3);
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
        outState.putFloatArray(STATE_MATRIX, img.getMatrixValues());
    }

    public static MapFragment create(boolean zoom, float lat, float lng) {
        MapFragment fragment = new MapFragment();
        Bundle bundle = new Bundle();

        bundle.putFloat("lat", lat);
        bundle.putFloat("lng", lng);
        bundle.putBoolean("zoom", zoom);

        fragment.setArguments(bundle);
        // Add arguments
        return fragment;
    }


    public void changeActive(int markerType, boolean b) {

    }
}
