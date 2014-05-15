package se.lundakarnevalen.extern.android;

import android.app.Dialog;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

import se.lundakarnevalen.extern.data.DataType;
import se.lundakarnevalen.extern.fragments.FoodFragment;
import se.lundakarnevalen.extern.fragments.FunFragment;
import se.lundakarnevalen.extern.fragments.MapFragment;
import se.lundakarnevalen.extern.fragments.OtherFragment;
import se.lundakarnevalen.extern.fragments.SchemeFragment;
import se.lundakarnevalen.extern.fragments.TrainMapFragment;
import se.lundakarnevalen.extern.map.GPSTracker;
import se.lundakarnevalen.extern.map.LocationTracker;
import se.lundakarnevalen.extern.map.MapLoader;
import se.lundakarnevalen.extern.util.Logf;
import se.lundakarnevalen.extern.widget.LKMapView;
import se.lundakarnevalen.extern.widget.LKRightMenuArrayAdapter;
import se.lundakarnevalen.extern.widget.LKTrainView;

import static se.lundakarnevalen.extern.util.ViewUtil.get;

public class ContentActivity extends ActionBarActivity {
    public static final String LOG_TAG = ContentActivity.class.getSimpleName();

    public ListView mRightMenuList;
    public MapFragment mMapFragment;

    private FragmentManager mFragmentMgr;
    private BottomMenuClickListener mBottomMenuListener;
    private View mActionBarView;
    private DrawerLayout mDrawerLayout;
    private GPSTracker mGpsTracker;
    private LocationTracker mLocationTracker;

    public <T> T find(int id, Class<T> clz) {
        return clz.cast(findViewById(id));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        setupActionbar();
        setupDrawerLayout();
        setupTint();
        mFragmentMgr = getSupportFragmentManager();
        popFragmentStack();
        MapLoader.startPreLoading(getApplicationContext());

        mMapFragment = new MapFragment();
        loadFragmentReplaceBS(mMapFragment);

        mRightMenuList = find(R.id.right_menu_list, ListView.class);

        populateBottomMenu(find(R.id.bottom_frame_menu, LinearLayout.class));

        //Load populate with delay
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                populateRightMenuDrawer();
            }
        }, 300);
    }

    private void setupDrawerLayout() {
        mDrawerLayout = find(R.id.drawer_layout, DrawerLayout.class);
        mDrawerLayout.setScrimColor(Color.TRANSPARENT);
    }

    public void toggleShowFilterDrawer() {
        if (mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
            mDrawerLayout.closeDrawer(Gravity.RIGHT);
        } else {
            mDrawerLayout.openDrawer(Gravity.RIGHT);
        }
    }

    public void lockFilterDrawer(boolean lock){
        mDrawerLayout.setDrawerLockMode(lock ? DrawerLayout.LOCK_MODE_LOCKED_CLOSED : DrawerLayout.LOCK_MODE_UNLOCKED, Gravity.RIGHT);
    }

    private void createCustomDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.welcome_dialog);
        dialog.setTitle("WELCOME");
        // set the custom dialog components - text, image and button
        TextView text = (TextView) dialog.findViewById(R.id.text);
        text.setText("Android custom dialog example!");

        Button dialogButton = (Button) dialog.findViewById(R.id.okButton);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    protected void onStart() {
        mGpsTracker = new GPSTracker(this);
        mLocationTracker = new LocationTracker(this);
        super.onStart();
    }

    @Override
    protected void onStop() {
        if(mLocationTracker != null){
            mLocationTracker.stopUpdates();
        }
        if (mGpsTracker != null) {
            mGpsTracker.stopUsingGPS();
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        //Log.d(LOG_TAG, "onDestroy()!?  Cleaning allocated resources: MapFragment, TrainMapFragment, LKMapView");
        //MapLoader.clean();
        //TrainMapLoader.clean();
        LKMapView.clean();
        LKTrainView.clean();
        System.gc();
        super.onDestroy();
    }

    /*
    @Override
    public void onLowMemory() {
        if (mBottomMenuListener.selected != null) {
            Fragment visibleFragment = Fragment.class.cast(mBottomMenuListener.selected.getTag(R.id.bottom_menu_tag_fragment));
            if (visibleFragment instanceof MapFragment) {
                LKTrainView.clean();
            } else if (visibleFragment instanceof TrainMapFragment) {
                Log.w(LOG_TAG, "onLowMemory() called: TrainMap showing thus cleaning LKMapIcons");
                LKMapView.clean();
            } else {
                Log.w(LOG_TAG, "onLowMemory() called: No map showing thus cleaning train LKMapIcons");
                LKMapView.clean();
            }
        }
        System.gc();
        super.onLowMemory();
    }
    */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    private void setupTint() {
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintColor(getResources().getColor(R.color.red));
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setNavigationBarTintColor(getResources().getColor(R.color.red));
    }

    private ActionBar setupActionbar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = mActionBarView = inflater.inflate(R.layout.action_bar_layout, null);
        actionBar.setCustomView(view);
        return actionBar;
    }

    private void populateBottomMenu(LinearLayout bottomMenu) {
        mBottomMenuListener = new BottomMenuClickListener();
        AtomicInteger counter = new AtomicInteger(0);
        createBottomMenuItem(bottomMenu, mBottomMenuListener, counter, new FunFragment(), R.id.button1, R.string.fun, R.drawable.fun_logo);
        createBottomMenuItem(bottomMenu, mBottomMenuListener, counter, new FoodFragment(), R.id.button2, R.string.food, R.drawable.food_logo);
        createBottomMenuItem(bottomMenu, mBottomMenuListener, counter, mMapFragment, R.id.button3, R.string.map, R.drawable.map_logo);
        createBottomMenuItem(bottomMenu, mBottomMenuListener, counter, new SchemeFragment(), R.id.button4, R.string.scheme, R.drawable.scheme_logo);
        createBottomMenuItem(bottomMenu, mBottomMenuListener, counter, new OtherFragment(), R.id.button5, R.string.other, R.drawable.other_logo);
        mBottomMenuListener.first(get(bottomMenu, R.id.button3, ViewGroup.class));
    }

    private void createBottomMenuItem(LinearLayout menu, BottomMenuClickListener listener, AtomicInteger counter, Fragment f, int itemId, int textId, int imageId) {
        ViewGroup group = get(menu, itemId, ViewGroup.class);
        get(group, R.id.bottom_menu_text, TextView.class).setText(textId);
        get(group, R.id.bottom_menu_image, ImageView.class).setImageResource(imageId);
        if (Build.VERSION.SDK_INT >= 11) {
            get(group, R.id.bottom_menu_image, ImageView.class).setAlpha(0.7f);
        }
        group.setTag(BottomMenuClickListener.TAG_FRAGMENT, f);
        group.setTag(BottomMenuClickListener.TAG_IDX, counter.getAndIncrement());
        group.setOnClickListener(listener);
    }

    public void loadFragmentAddingBS(Fragment f) {
        Log.d("ContentActivity", "loadFragmentAddingBS(" + f + ")");
        FragmentTransaction t = mFragmentMgr.beginTransaction();
        t.setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_out);
        t.replace(R.id.content_frame, f);
        t.addToBackStack(null);
        t.commit();
    }

    private void loadFragmentReplaceBS(Fragment f) {
        Log.d("ContentActivity", "loadFragmentReplaceBS(" + f + ")");
        FragmentTransaction t = mFragmentMgr.beginTransaction();
        t.setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_out);
        t.replace(R.id.content_frame, f);
        t.commit();
    }

    public void popFragmentStack() {
        mFragmentMgr.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    /**
     * Sets up the ListView in the navigationdrawer menu.
     */
    private void populateRightMenuDrawer() {
        LKRightMenuArrayAdapter adapter = new LKRightMenuArrayAdapter(this);
        adapter.setNotifyOnChange(false);
        adapter.add(getString(R.string.show_all), 0, DataType.values(), false);
        adapter.add(getString(R.string.food), R.drawable.food_logo, new DataType[]{DataType.FOOD, DataType.FOODSTOCK, DataType.SNACKS}, true);
        adapter.add(getString(R.string.fun), R.drawable.fun_logo,
                new DataType[]{DataType.FUN, DataType.SMALL_FUN, DataType.TENT_FUN, DataType.TOMBOLAN, DataType.SCENE, DataType.RADIO}, true);
        adapter.add(getString(R.string.tent), R.drawable.tent_logo, new DataType[]{DataType.TENT_FUN}, false);
        adapter.add(getString(R.string.tombola), R.drawable.tombola_logo, new DataType[]{DataType.TOMBOLAN}, false);
        adapter.add(getString(R.string.music), R.drawable.musik_logo, new DataType[]{DataType.SCENE, DataType.MUSIC}, false);
        adapter.add(getString(R.string.help), R.drawable.help_logo, new DataType[]{DataType.POLICE, DataType.CARE}, false);
        adapter.add(getString(R.string.wc), R.drawable.wc_logo, new DataType[]{DataType.TOILETS}, false);
        adapter.add(getString(R.string.entre), R.drawable.entrance_filter_icon, new DataType[]{DataType.ENTRANCE}, true);
        adapter.add(getString(R.string.trash), R.drawable.soptunna_filter_icon, new DataType[]{DataType.TRASHCAN}, false);
        adapter.setNotifyOnChange(true);
        adapter.notifyDataSetChanged();

        mRightMenuList.setAdapter(adapter);
        mRightMenuList.setOnItemClickListener(adapter);
        mRightMenuList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        //mRightMenuList.setItemsCanFocus(false);
    }

    public void allBottomsUnfocus() {
        mBottomMenuListener.deselectItem(getResources());
        mBottomMenuListener.selected = null;
    }

    public void focusBottomItem(int nbr) {
        mBottomMenuListener.deselectItem(getResources());
        final View child = find(R.id.bottom_frame_menu, LinearLayout.class).getChildAt(nbr);
        mBottomMenuListener.selectItem(child, getResources());
    }

    public void updateMapView(Collection<DataType> types) {
        mMapFragment.setActiveType(types);
    }

    public void showMapAndPanTo(float lat, float lng) {
        focusBottomItem(MapFragment.BOTTOM_MENU_ID);
        mMapFragment.addZoomHintForNextCreate(lat, lng, -1.0f); // will use midZoom
        loadFragmentAddingBS(mMapFragment);
    }

    public void registerForLocationUpdates(GPSTracker.GPSListener gpsListener, LocationTracker.LocationJSONListener jsonListener) {
        Logf.d(LOG_TAG, "registerForLocationUpdates(%s)", gpsListener);

        mGpsTracker.addListener(gpsListener);
        mLocationTracker.addListener(jsonListener);

        mGpsTracker.invalidateMe(gpsListener);
        mLocationTracker.invalidateMe(jsonListener);
    }

    public void unregisterForLocationUpdates(GPSTracker.GPSListener gpsListener, LocationTracker.LocationJSONListener jsonListener) {
        Logf.d(LOG_TAG, "unregisterForLocationUpdates(%s)", gpsListener);
        mGpsTracker.removeListener(gpsListener);
        mLocationTracker.removeListener(jsonListener);
    }

    public void triggerFilterUpdate() {
        final LKRightMenuArrayAdapter adapter = LKRightMenuArrayAdapter.class.cast(mRightMenuList.getAdapter());
        if (adapter != null) { // Will be null when activity starts
            adapter.notifyDataSetChanged();
        }
    }

    public void activateTrainButton() {
        ImageButton b = get(mActionBarView, R.id.train, ImageButton.class);
        b.setImageResource(R.drawable.train_logo_small);
        b.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragmentAddingBS(TrainMapFragment.create(true));
            }
        });
        b.setVisibility(View.VISIBLE);
        final ImageButton gps = get(mActionBarView, R.id.gps_marker, ImageButton.class);
        gps.setImageResource(R.drawable.gps_menu_marker);
        gps.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mMapFragment.zoomToMarker();
            }
        });
        gps.setVisibility(View.VISIBLE);
    }

    public void activateMapButton() {
        ImageButton b = get(mActionBarView, R.id.train, ImageButton.class);
        b.setImageResource(R.drawable.map_logo);
        b.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragmentAddingBS(mMapFragment);
            }
        });
        b.setVisibility(View.VISIBLE);
        b = get(mActionBarView, R.id.gps_marker, ImageButton.class);
        b.setVisibility(View.INVISIBLE);
    }

    public void inactivateTrainButton() {
        get(mActionBarView, R.id.train, ImageButton.class).setVisibility(View.INVISIBLE);
        get(mActionBarView, R.id.gps_marker, ImageButton.class).setVisibility(View.INVISIBLE);
    }

    public void ensureSelectedFilters(DataType[] types) {
        LKRightMenuArrayAdapter adapter = LKRightMenuArrayAdapter.class.cast(mRightMenuList.getAdapter());
        adapter.ensureSelectedFilters(types);
    }

    public void showMapAndPanDeveloper(float lat, float lng, int i) {
        focusBottomItem(MapFragment.BOTTOM_MENU_ID);
        mMapFragment.zoomToDeveloper(lat,lng,i);
        loadFragmentAddingBS(mMapFragment);
    }

    private class BottomMenuClickListener implements OnClickListener {
        private static final int TAG_IDX = R.id.bottom_menu_tag_idx;
        private static final int TAG_FRAGMENT = R.id.bottom_menu_tag_fragment;

        private final Resources r = getResources();

        private View selected;

        private BottomMenuClickListener() {        }

        public void first(View first) {
            selected = first;
            selectItem(first, r);
        }

        @Override
        public void onClick(View v) {
            if (v == this.selected) return;
            Fragment f = (Fragment) v.getTag(TAG_FRAGMENT);
            deselectItem(r);
            selectItem(v, r);
            popFragmentStack();
            loadFragmentReplaceBS(f);
        }


        private void selectItem(View target, Resources res) {
            target.setBackgroundColor(res.getColor(R.color.bottom_menu_background_selected));
            get(target, R.id.bottom_menu_text, TextView.class).setTextColor(res.getColor(R.color.white));
            get(target, R.id.bottom_menu_shadow, LinearLayout.class).setBackgroundColor(res.getColor(R.color.bottom_menu_shadow_selected));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                get(target, R.id.bottom_menu_image, ImageView.class).setAlpha(1.0f);
            }
            mDrawerLayout.closeDrawers();
            this.selected = target;
        }

        private void deselectItem(Resources res) {
            if (selected != null) {
                selected.setBackgroundColor(res.getColor(R.color.red));
                get(selected, R.id.bottom_menu_text, TextView.class).setTextColor(res.getColor(R.color.pink_white_unselected));
                get(selected, R.id.bottom_menu_shadow, LinearLayout.class).setBackgroundColor(res.getColor(R.color.bottom_menu_shadow));
                if (Build.VERSION.SDK_INT > 10) {
                    get(selected, R.id.bottom_menu_image, ImageView.class).setAlpha(0.7f);
                }
            }
        }
    }

}