package se.lundakarnevalen.extern.fragments;

import android.graphics.Picture;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.util.Collection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import se.lundakarnevalen.extern.android.ContentActivity;
import se.lundakarnevalen.extern.android.R;
import se.lundakarnevalen.extern.data.DataType;
import se.lundakarnevalen.extern.map.GPSTracker;
import se.lundakarnevalen.extern.map.MapLoader;
import se.lundakarnevalen.extern.map.Marker;
import se.lundakarnevalen.extern.util.Delay;
import se.lundakarnevalen.extern.util.Logf;
import se.lundakarnevalen.extern.widget.LKMapView;
import se.lundakarnevalen.extern.widget.SVGView;

import static se.lundakarnevalen.extern.util.ViewUtil.get;

public class MapFragment extends LKFragment implements GPSTracker.GPSListener {
    public static final int BOTTOM_MENU_ID = 2;
    public static final float STARTZOOM = 1.3f;

    private float lng_marker = -1;
    private float lat_marker = -1;

    private float showOnNextCreateLat = -1.0f;
    private float showOnNextCreateLng = -1.0f;
    private float showOnNextCreateScale = -1.0f;

    private static final String LOG_TAG = MapFragment.class.getSimpleName();
    private static final String STATE_MATRIX = "matrix";

    private float[] mMatrixValues;
    private LKMapView mapView;
    private boolean isGPSWithinMap;

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

        mapView = get(root, R.id.map_id, LKMapView.class);
        ContentActivity activity = ContentActivity.class.cast(getActivity());
        activity.allBottomsUnfocus();
        activity.focusBottomItem(BOTTOM_MENU_ID);

        get(root, R.id.map_pull_out, View.class).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            ContentActivity.class.cast(getActivity()).toggleShowFilterDrawer();
            }
        });

        final ViewFlipper flipper = get(root, R.id.map_switcher, ViewFlipper.class);

        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Picture picture = MapLoader.preload(inflater.getContext()).get(60, TimeUnit.SECONDS);
                    waitForLayout();
                    float minZoom = calculateMinZoom(mapView, picture);
                    mapView.setSvg(picture, minZoom, mMatrixValues);
                } catch (InterruptedException e) {
                    Log.wtf(LOG_TAG, "Future was interrupted", e);
                } catch (ExecutionException e) {
                    Log.wtf(LOG_TAG, "ExecutionException", e);
                } catch (TimeoutException e) {
                    try{
                        Log.d(LOG_TAG,  "MapLoader timed out, restarting");
                        Picture picture = new MapLoader(inflater.getContext()).call();
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

        flipper.setAnimateFirstView(true);
        flipper.setInAnimation(AnimationUtils.loadAnimation(inflater.getContext(), R.anim.abc_fade_in));
        flipper.setOutAnimation(AnimationUtils.loadAnimation(inflater.getContext(), R.anim.abc_fade_out));

        mapView.setListener(new LKMapView.OnMarkerSelectedListener() {
            @Override
            public void onMarkerSelected(final Marker m) {
                final boolean wasSelected = (m != null);
                final ViewGroup layout = get(root, R.id.map_info_layout, ViewGroup.class);
                layout.setVisibility(wasSelected ? View.VISIBLE : View.GONE);
                if(wasSelected) {
                    get(root, R.id.map_title_text, TextView.class).setText(String.valueOf(getString(m.element.title)));
                    if (m.element.title == m.element.place || getString(m.element.place).equals("")) {
                        get(root, R.id.map_location_text, TextView.class).setVisibility(View.GONE);
                    } else {
                        get(root, R.id.map_location_text, TextView.class).setVisibility(View.VISIBLE);
                        get(root, R.id.map_location_text, TextView.class).setText(String.valueOf(getString(m.element.place)));
                    }
                    if(m.element.hasLandingPage()){
                        get(root, R.id.map_info_click_text, TextView.class).setVisibility(View.VISIBLE);
                        layout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            ContentActivity.class.cast(getActivity()).loadFragmentAddingBS(LandingPageFragment.create(m.element));
                            }
                        });
                    } else if(m.element.isRadio()) {
                        get(root, R.id.map_info_click_text, TextView.class).setVisibility(View.VISIBLE);
                        layout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ContentActivity.class.cast(getActivity()).loadFragmentAddingBS(new MusicFragment());
                            }
                        });

                    }else{

                        get(root, R.id.map_info_click_text, TextView.class).setVisibility(View.GONE);
                    }
                }
            }
        });

        if(showOnNextCreateLat > 0.0f && showOnNextCreateLng > 0.0f) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    showItem(showOnNextCreateLat, showOnNextCreateLng, showOnNextCreateScale);
                    showOnNextCreateLat = 1.0f;
                    showOnNextCreateLng = 1.0f;
                    showOnNextCreateScale = 1.0f;
                }
            }, 500);
        }
        mapView.setGpsMarker(lat_marker, lng_marker, (savedInstanceState != null));

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mMatrixValues != null) {
            mapView.importMatrixValues(mMatrixValues);
        }
        final ContentActivity contentActivity = ContentActivity.class.cast(getActivity());
        contentActivity.focusBottomItem(BOTTOM_MENU_ID);
        contentActivity.triggerFilterUpdate();
    }

    @Override
    public void onPause() {
        mMatrixValues = mapView.exportMatrixValues();
        super.onPause();
    }

    @Override
    public void onStop() {
        final ContentActivity activity = ContentActivity.class.cast(getActivity());
        activity.unregisterForLocationUpdates(this);
        activity.lockFilterDrawer(true);
        activity.inactivateTrainButton();
        super.onStop();
    }


    @Override
    public void onStart() {
        final ContentActivity activity = ContentActivity.class.cast(getActivity());
        activity.lockFilterDrawer(false);
        activity.activateTrainButton();
        activity.registerForLocationUpdates(this);
        super.onStart();
    }


    private void waitForLayout() {
        int counter = 0;
        while (mapView.getMeasuredHeight() == 0 && counter++ < 100) Delay.ms(100); //Wait for layout
        mapView.updateViewLimitBounds();
    }

    private float calculateMinZoom(View root, Picture pic) {
        // We assume that the svg image is 512x512 for now
        return STARTZOOM * Math.max(
                    root.getMeasuredHeight() * 1.0f / pic.getHeight(),
                    root.getMeasuredWidth() * 1.0f / pic.getWidth());
    }

    public void setActiveType(Collection<DataType> types) {
        mapView.setActiveTypes(types);
    }

    private void showItem(final float lat, final float lng, float showOnNextCreateScale) {
        if(showOnNextCreateScale > 0.0f){
            mapView.zoom(showOnNextCreateScale * 0.99f);
        } else {
            mapView.zoom(mapView.mMidZoom);
        }
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                final float[] dst = new float[2];
                mapView.getPointFromCoordinates(lat, lng, dst);
                mapView.panTo(dst[0], dst[1]);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mapView.getPointFromCoordinates(lat, lng, dst);
                        mapView.triggerClick(dst[0], dst[1]);
                    }
                }, 700);
            }
        }, 100);
    }

    public void addZoomHintForNextCreate(float lat, float lng, float v) {
        this.showOnNextCreateLat = lat;
        this.showOnNextCreateLng = lng;
        this.showOnNextCreateScale = v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(LOG_TAG, "onSaveInstanceState() called");
        outState.putFloatArray(STATE_MATRIX, mapView.exportMatrixValues());
    }

    public static MapFragment create(float lat, float lng) {
        Bundle bundle = new Bundle();
        bundle.putFloat("lat", lat);
        bundle.putFloat("lng", lng);
        MapFragment fragment = new MapFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public void zoomToMarker() {
        if(isGPSWithinMap){
            float[] dst = new float[2];
            mapView.zoom(mapView.mMidZoom);
            mapView.panToCenterFast();
            mapView.getPointFromCoordinates(lat_marker, lng_marker, dst);
            mapView.panTo(dst[0], dst[1]);
        } else {
            Toast.makeText(getContext(), "Du är utanför området eller har ej GPS aktiverad", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onNewLocation(double lat, double lng) {
        Logf.d(LOG_TAG, "onNewLocation(lat: %f, lng: %f)", lat, lng);
        if(mapView.isWithinLatLngRange((float) lat, (float) lng)){
            isGPSWithinMap = true;
            lat_marker = (float) lat;
            lng_marker = (float) lng;
            mapView.setGpsMarker(lat_marker, lng_marker, false);
        } else {
            isGPSWithinMap = false;
        }
    }
}
