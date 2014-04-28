package se.lundakarnevalen.extern.android;

import android.content.res.Resources;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.view.View.OnClickListener;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import se.lundakarnevalen.extern.fragments.FoodFragment;
import se.lundakarnevalen.extern.fragments.FunFragment;
import se.lundakarnevalen.extern.fragments.MapFragment;
import se.lundakarnevalen.extern.fragments.OtherFragment;
import se.lundakarnevalen.extern.fragments.SchemeFragment;
import se.lundakarnevalen.extern.map.MarkerType;
import se.lundakarnevalen.extern.widget.LKRightMenuArrayAdapter;
import se.lundakarnevalen.extern.widget.LKRightMenuArrayAdapter.*;

import static se.lundakarnevalen.extern.util.ViewUtil.*;

public class ContentActivity extends ActionBarActivity {
    public static final String TAG_MAP = "map";
    public static final String LOG_TAG = ContentActivity.class.getSimpleName();
    private int counterRight = 0;
    private FragmentManager fragmentMgr;
    private LKRightMenuArrayAdapter adapter;
    private ListView rightMenuList;
    private DrawerLayout drawerLayout;
    private BottomMenuClickListener listner;
    private ActionBar actionBar;
    public MapFragment mapFragment;
    private ArrayList<LKRightMenuListItem> rightMenuItems = new ArrayList<LKRightMenuListItem>();
    private LKRightMenuListItem showAllItem;
    private boolean allItemsActivated = true;

    public <T> T find(int id, Class<T> clz) {
        return clz.cast(findViewById(id));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        fragmentMgr = getSupportFragmentManager();

        if(savedInstanceState == null){ // Prevent multiple fragments creations
            mapFragment = new MapFragment();
            loadFragmentWithReplace(mapFragment);
        }

        rightMenuList = find(R.id.right_menu_list, ListView.class);
        drawerLayout = find(R.id.drawer_layout, DrawerLayout.class);
        drawerLayout.setScrimColor(Color.TRANSPARENT);

        populateBottomMenu(find(R.id.bottom_frame_menu, LinearLayout.class));

        //Load populate with delay
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                populateRightMenuDrawer();
            }
        }, 300);

        actionBar = setupActionbar();

        setupTint();
    }

    @Override
    protected void onDestroy() {
        Log.d(LOG_TAG, "onDestroy()!? null=>preloaded");
        MapFragment.clean();
        super.onDestroy();
    }


    public void hideBottomMenu(){

        /*
        final View menu = find(R.id.bottom_frame_menu, View.class);
        View content = find(R.id.content_frame, View.class);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            final Animation slideOutBottom = AnimationUtils.loadAnimation(this, R.anim.abc_slide_out_bottom);
            slideOutBottom.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}

                @Override
                public void onAnimationEnd(Animation animation) {
                    menu.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {}
            });
            menu.startAnimation(slideOutBottom);
        }else{
            menu.setVisibility(View.GONE);
        }
        */
    }

    //@TargetApi(11)
    public void showBottomMenu(){
        /*
        final View menu = find(R.id.bottom_frame_menu, View.class);
        View content = find(R.id.content_frame, View.class);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            Animation slideInBottom = AnimationUtils.loadAnimation(this, R.anim.abc_slide_in_bottom);
            slideInBottom.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    menu.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animation animation) {}

                @Override
                public void onAnimationRepeat(Animation animation) {}
            });
            menu.startAnimation(slideInBottom);
        } else {
            find(R.id.bottom_frame_menu, View.class).setVisibility(View.VISIBLE);
        }
        */
    }

    private void setupTint() {
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintColor(getResources().getColor(R.color.red));
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setNavigationBarTintColor(getResources().getColor(R.color.red));
    }

    private ActionBar setupActionbar() {
        LayoutInflater inflater = LayoutInflater.from(this);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(inflater.inflate(R.layout.action_bar_layout, null));
        return actionBar;
    }

    private void populateBottomMenu(LinearLayout bottomMenu) {
        listner = new BottomMenuClickListener();
        AtomicInteger counter = new AtomicInteger(0);
        createBottomMenuItem(bottomMenu, listner, counter, new FunFragment(), R.id.button1, R.string.fun, R.drawable.fun_logo);
        createBottomMenuItem(bottomMenu, listner, counter, new FoodFragment(), R.id.button2, R.string.food, R.drawable.food_logo);
        createBottomMenuItem(bottomMenu, listner, counter, mapFragment, R.id.button3, R.string.map, R.drawable.map_logo);
        createBottomMenuItem(bottomMenu, listner, counter, new SchemeFragment(), R.id.button4, R.string.scheme, R.drawable.scheme_logo);
        createBottomMenuItem(bottomMenu, listner, counter, new OtherFragment(), R.id.button5, R.string.other, R.drawable.other_logo);
        listner.first(get(bottomMenu, R.id.button3, ViewGroup.class));
    }

    private void createBottomMenuItem(LinearLayout menu, BottomMenuClickListener listener, AtomicInteger counter, Fragment f, int itemId, int textId, int imageId) {
        ViewGroup group = get(menu, itemId, ViewGroup.class);
        get(group, R.id.bottom_menu_text, TextView.class).setText(textId);
        get(group, R.id.bottom_menu_image, ImageView.class).setImageResource(imageId);
        if(Build.VERSION.SDK_INT >= 11){
            get(group, R.id.bottom_menu_image, ImageView.class).setAlpha(0.7f);
        }
        group.setTag(BottomMenuClickListener.TAG_FRAGMENT, f);
        group.setTag(BottomMenuClickListener.TAG_IDX, counter.getAndIncrement());
        group.setOnClickListener(listener);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.content, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    public void setTitle(String title) {
        setTitle(title);
    }

    public void loadFragmentWithAdd(Fragment f) {
        Log.d("ContentActivity", "loadFragmentWithAdd("+f+")");
        FragmentTransaction transaction = fragmentMgr.beginTransaction();
        transaction.setCustomAnimations(R.anim.abc_fade_in,R.anim.abc_fade_out);
        transaction.replace(R.id.content_frame, f);
        transaction.addToBackStack(null);
        if(listner != null) {
            if (f instanceof MapFragment) {
                listner.onClick(findViewById(R.id.button3));
            }
        }
        transaction.commit();
    }

    private void loadFragmentWithReplace(Fragment f) {
        Log.d("ContentActivity", "loadFragmentWithReplace("+f+")");
        fragmentMgr
            .beginTransaction()
                .setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_out)
                .replace(R.id.content_frame, f)
            .commit();
    }

    private void loadFragmentWithReplaceAnimated(Fragment f, boolean left) {
        Log.d("ContentActivity", "loadFragmentWithReplaceAnim("+f+")");
        int in = R.anim.abc_fade_in; //left ? R.anim.slide_in_left : R.anim.slide_in_right;
        int out = R.anim.abc_fade_out; //left ? R.anim.slide_out_right : R.anim.slide_out_left;
        fragmentMgr
            .beginTransaction()
                .setCustomAnimations(in, out)
                .replace(R.id.content_frame, f)
            .commit();
    }

    public void popFragmentStack() {
        fragmentMgr.popBackStack();
    }

    /**
     * Sets up the ListView in the navigationdrawer menu.
     */
    private void populateRightMenuDrawer() {
        LayoutInflater inflater = LayoutInflater.from(this);

        ArrayList<LKRightMenuListItem> listItems = new ArrayList<LKRightMenuListItem>();

        rightMenuItems = new ArrayList<LKRightMenuListItem>();

        LKRightMenuListItem header = new LKRightMenuListItem()
                .isStatic(true,inflater.inflate(R.layout.menu_header, null));
        listItems.add(header);

        LKRightMenuListItem foodItem = new LKRightMenuListItem(getString(R.string.food),R.drawable.food_logo, MarkerType.FOOD);
        foodItem.setOnClickListener(new MenuClickSelector(foodItem));
        listItems.add(foodItem);
        rightMenuItems.add(foodItem);


        LKRightMenuListItem funItem = new LKRightMenuListItem(getString(R.string.fun),R.drawable.fun_logo, MarkerType.FUN);
        funItem.setOnClickListener(new MenuClickSelector(funItem));
        listItems.add(funItem);
        rightMenuItems.add(funItem);


        LKRightMenuListItem helpItem = new LKRightMenuListItem(getString(R.string.help),R.drawable.help_logo, MarkerType.HELP);
        helpItem.setOnClickListener(new MenuClickSelector(helpItem));
        listItems.add(helpItem);
        rightMenuItems.add(helpItem);

        LKRightMenuListItem wcItem = new LKRightMenuListItem(getString(R.string.wc),R.drawable.wc_logo, MarkerType.WC);
        wcItem.setOnClickListener(new MenuClickSelector(wcItem));
        listItems.add(wcItem);
        rightMenuItems.add(wcItem);

        showAllItem = new LKRightMenuListItem(getString(R.string.show_all),0, MarkerType.SHOW);
        showAllItem.setOnClickListener(new MenuClickSelector(showAllItem));
        listItems.add(showAllItem);

        adapter = new LKRightMenuArrayAdapter(this, listItems);
        rightMenuList.setAdapter(adapter);
        rightMenuList.setOnItemClickListener(adapter);

    }

    public void allBottomsActive() {
        listner.deselectItem(getResources());
        listner.selected = null;
    }

    private class MenuClickSelector implements OnClickListener {
        LKRightMenuListItem item;
        MenuClickSelector(LKRightMenuListItem item) {
            this.item = item;
        }

        // change direction
        @Override
        public void onClick(View v) {
            // need later
            if(item.markerType == MarkerType.SHOW) {

                if(item.isOn) {

                } else {
                    for(LKRightMenuListItem i: rightMenuItems) {
                        mapFragment.changeActive(i.markerType,true);

                        Log.d("button:",""+i.button);
                        i.button.setBackgroundColor(getResources().getColor(R.color.right_menu_button));
                        i.isOn = true;

                    }
                    allItemsActivated = true;
                    RelativeLayout button = get(v, R.id.button, RelativeLayout.class);
                    button.setBackgroundColor(getResources().getColor(R.color.right_menu_button_selected));
                    item.isOn = true;
                    //mapFragment.renderMap();
                    counterRight = 0;
                }
                    // show all..
            } else {
                RelativeLayout button = get(v, R.id.button, RelativeLayout.class);
                if(allItemsActivated) {
                    allItemsActivated = false;
                    for(LKRightMenuListItem i: rightMenuItems) {
                        mapFragment.changeActive(i.markerType, false);
                        i.isOn = true;
                    }
                }
                if(item.isOn) {
                    mapFragment.changeActive(item.markerType,true);
                    button.setBackgroundColor(getResources().getColor(R.color.right_menu_button_selected));
                    counterRight++;
                    item.isOn = false;
                } else {
                    counterRight--;
                    mapFragment.changeActive(item.markerType,false);
                    button.setBackgroundColor(getResources().getColor(R.color.right_menu_button));
                    item.isOn = true;
                }
                if(counterRight==0) {
                    for(LKRightMenuListItem i: rightMenuItems) {
                        mapFragment.changeActive(i.markerType,true);
                        Log.d("button:",""+i.button);
                        i.isOn = true;
                    }
                    allItemsActivated = true;
                    showAllItem.isOn = true;
                    showAllItem.button.setBackgroundColor(getResources().getColor(R.color.right_menu_button_selected));

                } else {
                    showAllItem.isOn = false;
                    showAllItem.button.setBackgroundColor(getResources().getColor(R.color.right_menu_button));
                }
                //mapFragment.renderMap();
            }
        }
    }

    private class BottomMenuClickListener implements OnClickListener {
        private static final int TAG_IDX = R.id.bottom_menu_tag_idx;
        private static final int TAG_FRAGMENT = R.id.bottom_menu_tag_fragment;

        private final Resources r = getResources();

        private ViewGroup selected;

        private BottomMenuClickListener() {}

        public void first(ViewGroup first){
            selected = first;
            selectItem(first, r);
        }

        @Override
        public void onClick(View v) {
            if(v == this.selected) return;

            ViewGroup newSelection = (ViewGroup) v;
            Fragment f = (Fragment) v.getTag(TAG_FRAGMENT);

            int newIdx = Integer.class.cast(v.getTag(TAG_IDX));
            if(selected == null) {
                selectItem(newSelection, r);
                loadFragmentWithReplaceAnimated(f, true);
                this.selected = newSelection;
                return;
            }
            int oldIdx = Integer.class.cast(selected.getTag(TAG_IDX));
            boolean moveLeft = (newIdx > oldIdx);

            selectItem(newSelection, r);
            deselectItem(r);

            loadFragmentWithReplaceAnimated(f, moveLeft);

            this.selected = newSelection;
        }

        private void selectItem(ViewGroup selected, Resources res) {
            selected.setBackgroundColor(res.getColor(R.color.bottom_menu_background_selected));
            get(selected, R.id.bottom_menu_text, TextView.class).setTextColor(res.getColor(R.color.white));
            get(selected, R.id.bottom_menu_shadow, LinearLayout.class).setBackgroundColor(res.getColor(R.color.bottom_menu_shadow_selected));
            if(Build.VERSION.SDK_INT > 10) {
                get(selected, R.id.bottom_menu_image, ImageView.class).setAlpha(1.0f);
            }
            if(selected.getTag() instanceof MapFragment) {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            } else {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                drawerLayout.closeDrawers();
            }
        }

        private void deselectItem(Resources res) {
            if (selected != null) {
                selected.setBackgroundColor(res.getColor(R.color.red));
                get(selected, R.id.bottom_menu_text, TextView.class).setTextColor(res.getColor(R.color.white_unselected));
                get(selected, R.id.bottom_menu_shadow, LinearLayout.class).setBackgroundColor(res.getColor(R.color.bottom_menu_shadow));
                if(Build.VERSION.SDK_INT >10) {
                    get(selected, R.id.bottom_menu_image, ImageView.class).setAlpha(0.7f);
                }
            }
        }
    }
}
