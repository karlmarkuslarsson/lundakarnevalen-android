package se.lundakarnevalen.extern.fragments;

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

import se.lundakarnevalen.extern.android.R;
import se.lundakarnevalen.extern.util.Timer;
import se.lundakarnevalen.extern.widget.SVGMapView;

import static se.lundakarnevalen.extern.util.ViewUtil.get;

public class MapFragment extends LKFragment {

    private static final String LOG_TAG = MapFragment.class.getSimpleName();
    public static final String STATE_MATRIX = "matrix";

    public static SVG staticCache;

    private int dip;
    private int imageWidth;
    private int imageHeight;

    private SVGMapView img;

    // Every time you switch to this fragment.
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, null);

        if (imageWidth == 0) {
            DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
            imageWidth = metrics.widthPixels;
            imageHeight = metrics.heightPixels;
            dip = metrics.densityDpi;
        }

        img = get(rootView, R.id.map_id, SVGMapView.class);
        final ViewFlipper flipper = get(rootView, R.id.map_switcher, ViewFlipper.class);

        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Timer t = new Timer();
                    SVG svg = SVG.getFromResource(inflater.getContext(), R.raw.map3);
                    t.tick(LOG_TAG, "getFromResource()");
                    img.setSvg(svg, imageWidth, imageHeight, dip);
                    t.tick(LOG_TAG, "renderToPicture()");
                } catch (SVGParseException e) {
                    Log.wtf(LOG_TAG, "This wont happen");
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
