package se.lundakarnevalen.extern.fragments;

import android.graphics.Picture;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.util.Collection;

import se.lundakarnevalen.extern.android.ContentActivity;
import se.lundakarnevalen.extern.android.R;
import se.lundakarnevalen.extern.data.DataType;
import se.lundakarnevalen.extern.map.GPSTracker;
import se.lundakarnevalen.extern.map.LocationTracker;
import se.lundakarnevalen.extern.map.MapLoader;
import se.lundakarnevalen.extern.map.Marker;
import se.lundakarnevalen.extern.util.Delay;
import se.lundakarnevalen.extern.util.Logf;
import se.lundakarnevalen.extern.widget.LKMapView;

import static se.lundakarnevalen.extern.util.ViewUtil.get;

public class MapFragment extends LKFragment
        implements GPSTracker.GPSListener, MapLoader.MapLoaderCallback,
        LocationTracker.LocationJSONListener {

    public static final int BOTTOM_MENU_ID = 2;
    public static final float STARTZOOM = 1.3f;
    public static final int VIEWFLIPPER_CHILD_MAP = 1;

    private float mGpsMarkerLat = -1;
    private float mGpsMarkerLng = -1;

    private float showOnNextCreateLat = -1.0f;
    private float showOnNextCreateLng = -1.0f;
    private float showOnNextCreateScale = -1.0f;

    private static final String LOG_TAG = MapFragment.class.getSimpleName();
    private static final String STATE_MATRIX = "matrix";

    private boolean mIsGPSWithinMap;
    private float[] mMatrixValues;

    private ViewFlipper mViewFlipper;
    private LKMapView mMapView;
    private View mSpinnerView;

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

        mSpinnerView = get(root, R.id.map_spinner, View.class);
        final RotateAnimation a = new RotateAnimation(
                0.0f, 3 * 360.0f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        a.setDuration(3400);
        a.setInterpolator(new LinearInterpolator());
        a.setRepeatCount(Animation.INFINITE);
        a.setRepeatMode(Animation.RESTART);
        mSpinnerView.startAnimation(a);

        final ContentActivity activity = ContentActivity.class.cast(getActivity());

        mMapView = get(root, R.id.map_id, LKMapView.class);

        activity.allBottomsUnfocus();
        activity.focusBottomItem(BOTTOM_MENU_ID);

        get(root, R.id.map_pull_out, View.class).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentActivity.class.cast(getActivity()).toggleShowFilterDrawer();
            }
        });

        mViewFlipper = get(root, R.id.map_switcher, ViewFlipper.class);
        mViewFlipper.setAnimateFirstView(true);
        mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(inflater.getContext(), R.anim.abc_fade_in));
        mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(inflater.getContext(), R.anim.abc_fade_out));

        mMapView.setListener(new LKMapView.OnMarkerSelectedListener() {
            @Override
            public void onMarkerSelected(final Marker m) {
                if(isDetached()) {
                    return;
                }
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
            }, 1000);
        }
        mMapView.setGpsMarker(mGpsMarkerLat, mGpsMarkerLng, (savedInstanceState != null));

        if(MapLoader.hasLoadedMapLarge()){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Picture p = MapLoader.getMapLarge();
                    float minZoom = calculateMinZoom(mMapView, p);
                    mMapView.setSvg(p, minZoom, null);
                    clearSpinner();
                }
            }, 400);
        } else if (MapLoader.hasLoadedMapMini()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Picture p = MapLoader.getMapMini();
                    float minZoom = calculateMinZoom(mMapView, p);
                    mMapView.setSvg(p, minZoom, null);
                    clearSpinner();
                }
            }, 400);
        } else {
            new MapLoader.MapSvgLoader(this).startWait(); // Wait for maps async
        }

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mMatrixValues != null) {
            mMapView.importMatrixValues(mMatrixValues);
        }
        final ContentActivity contentActivity = ContentActivity.class.cast(getActivity());
        contentActivity.focusBottomItem(BOTTOM_MENU_ID);
        contentActivity.triggerFilterUpdate();
        mMapView.updateViewLimitBounds();
    }

    @Override
    public void onPause() {
        mMatrixValues = mMapView.exportMatrixValues();
        super.onPause();
    }

    @Override
    public void onStop() {
        final ContentActivity activity = ContentActivity.class.cast(getActivity());
        activity.unregisterForLocationUpdates(this, this);
        activity.lockFilterDrawer(true);
        activity.inactivateTrainButton();
        super.onStop();
    }


    @Override
    public void onStart() {
        final ContentActivity activity = ContentActivity.class.cast(getActivity());
        activity.lockFilterDrawer(false);
        activity.activateTrainButton();
        activity.registerForLocationUpdates(this, this);
        super.onStart();
    }


    private void waitForLayout() {
        int counter = 0;
        while (mMapView.getMeasuredHeight() == 0 && counter++ < 100) Delay.ms(100); //Wait for layout
        mMapView.updateViewLimitBounds();
    }

    private float calculateMinZoom(View root, Picture pic) {
        // We assume that the svg image is 512x512 for now
        return STARTZOOM * Math.max(
                    root.getMeasuredHeight() * 1.0f / pic.getHeight(),
                    root.getMeasuredWidth() * 1.0f / pic.getWidth());
    }

    public void setActiveType(Collection<DataType> types) {
        mMapView.setActiveTypes(types);
    }

    private void showItem(final float lat, final float lng, float showOnNextCreateScale) {
        if(showOnNextCreateScale > 0.0f){
            mMapView.zoom(showOnNextCreateScale * 0.99f);
        } else {
            mMapView.zoom(mMapView.mMidZoom);
        }

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isDetached()) return; // Do nothing if fragment has been detached
                final float[] dst = new float[2];
                mMapView.getPointFromCoordinates(lat, lng, dst);
                mMapView.panTo(dst[0], dst[1]);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (isDetached()) return; // Do nothing if fragment has been detached
                        mMapView.getPointFromCoordinates(lat, lng, dst);
                        mMapView.triggerClick(dst[0], dst[1]);
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
        outState.putFloatArray(STATE_MATRIX, mMapView.exportMatrixValues());
    }

    public static MapFragment create(float lat, float lng) {
        Bundle bundle = new Bundle();
        bundle.putFloat("lat", lat);
        bundle.putFloat("lng", lng);
        MapFragment fragment = new MapFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public boolean zoomToMarker() {
        if(mIsGPSWithinMap){
            float[] dst = new float[2];
            mMapView.zoom(mMapView.mMidZoom);
            mMapView.panToCenterFast();
            mMapView.getPointFromCoordinates(mGpsMarkerLat, mGpsMarkerLng, dst);
            mMapView.panTo(dst[0], dst[1]);
            return true;
        } else {
            Toast.makeText(getContext(), R.string.gps_toast, Toast.LENGTH_LONG).show();
            return false;
        }
    }

    @Override
    public void onNewLocation(double lat, double lng) {
        Logf.d(LOG_TAG, "onNewLocation(lat: %f, lng: %f)", lat, lng);
        mGpsMarkerLat = (float) lat;
        mGpsMarkerLng = (float) lng;
        mIsGPSWithinMap = mMapView.isWithinLatLngRange((float) lat, (float) lng);
        mMapView.setGpsMarker(mGpsMarkerLat, mGpsMarkerLng, false);
    }

    @Override
    public void postMiniMap(Picture picture) {
        waitForLayout();
        float minZoom = calculateMinZoom(mMapView, picture);
        mMapView.setSvg(picture, minZoom, null);
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


    @Override
    public void postLargerMap(Picture picture) {
        waitForLayout();
        if (picture != null) {
            float minZoom = calculateMinZoom(mMapView, picture);
            mMapView.setSvg(picture, minZoom, null);
        }
    }

    private LocationTracker.LocationJSONResult.LatLng markus = new LocationTracker.LocationJSONResult.LatLng();
    private LocationTracker.LocationJSONResult.LatLng filip = new LocationTracker.LocationJSONResult.LatLng();
    private LocationTracker.LocationJSONResult.LatLng fredrik = new LocationTracker.LocationJSONResult.LatLng();

    public void zoomToDeveloper(float lat, float lng, int i) {
        // TODO Check inside map...
        switch (i) {
            case 1:
                addZoomHintForNextCreate(markus.lat, markus.lng, -1.0f); // will use midZoom
                break;
            case 2:
                addZoomHintForNextCreate(filip.lat, filip.lng, -1.0f); // will use midZoom
                break;
            case 3:
                addZoomHintForNextCreate(fredrik.lat, fredrik.lng, -1.0f); // will use midZoom
                break;
        }
    }

    @Override
    public void onNewLocationFromKarnevalist(LocationTracker.LocationJSONResult result) {
        if (result == null) return;
        Log.d(LOG_TAG, "Result: " + result.toString());

        if(result.success) {
            for(LocationTracker.LocationJSONResult.LatLng p : result.train_positions) {
                Log.d(LOG_TAG, p.id+" -  lat: "+p.lat+" lng: "+p.lng);
                switch(p.id) {
                    case 11:
                        markus = p;
                        break;
                    case 21:
                        filip = p;
                        break;
                    case 31:
                        fredrik = p;
                        break;
                }
            }
        }
    }
}
