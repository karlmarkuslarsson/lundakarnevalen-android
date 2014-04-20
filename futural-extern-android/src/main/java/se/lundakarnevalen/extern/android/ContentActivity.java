package se.lundakarnevalen.extern.android;

import android.content.Context;

import android.content.res.Resources;
import android.os.Build;
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

import se.lundakarnevalen.extern.fragments.FoodFragment;
import se.lundakarnevalen.extern.fragments.FunFragment;
import se.lundakarnevalen.extern.fragments.LKFragment;
import se.lundakarnevalen.extern.fragments.MapFragment;
import se.lundakarnevalen.extern.fragments.OtherFragment;
import se.lundakarnevalen.extern.fragments.SchemeFragment;
import se.lundakarnevalen.extern.map.MarkerType;
import se.lundakarnevalen.extern.widget.LKRightMenuArrayAdapter;
import se.lundakarnevalen.extern.widget.LKRightMenuArrayAdapter.*;

import static se.lundakarnevalen.extern.util.ViewUtil.*;

public class ContentActivity extends ActionBarActivity implements LKFragment.Messanger {
    public static final String TAG_MAP = "map";
    private int counterRight = 0;
    private FragmentManager fragmentMgr;
    private LKRightMenuArrayAdapter adapter;
    private ListView rightMenuList;
    private DrawerLayout drawerLayout;
    private BottomMenuClickListener list;
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

        mapFragment = new MapFragment();
        loadFragment(mapFragment, false);
        rightMenuList = (ListView) findViewById(R.id.right_menu_list);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.setScrimColor(Color.TRANSPARENT);
        populateMenu();
        generateLowerMenu(find(R.id.bottom_frame_menu, LinearLayout.class));
        actionBar = getSupportActionBar();
        setupActionbar();
        setupTint();
    }

    private void setupTint() {
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintColor(getResources().getColor(R.color.red));
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setNavigationBarTintColor(getResources().getColor(R.color.red));
    }

    private void setupActionbar() {
        LayoutInflater inflater = LayoutInflater.from(this);

        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(inflater.inflate(R.layout.action_bar_layout, null));
    }

    private void generateLowerMenu( LinearLayout bottomMenu) {
        list = new BottomMenuClickListener();
        createBottomMenuItem(bottomMenu, list, new FunFragment(), R.id.button1, R.string.fun, R.drawable.test_nojen);
        createBottomMenuItem(bottomMenu, list, new FoodFragment(), R.id.button2, R.string.food, R.drawable.test_spexet);
        createBottomMenuItem(bottomMenu, list, mapFragment, R.id.button3, R.string.map, R.drawable.test_nojen); // TODO ska vi inte ha mapFragment här?
        createBottomMenuItem(bottomMenu, list, new SchemeFragment(), R.id.button4, R.string.scheme, R.drawable.test_spexet);
        createBottomMenuItem(bottomMenu, list, new OtherFragment(), R.id.button5, R.string.other, R.drawable.test_nojen);
        list.first(get(bottomMenu, R.id.button3, ViewGroup.class));

    }

    private void createBottomMenuItem(LinearLayout menu, BottomMenuClickListener listener, Fragment f, int itemId, int textId, int imageId) {
        ViewGroup group = get(menu, itemId, ViewGroup.class);
        get(group, R.id.bottom_menu_text, TextView.class).setText(textId);
        get(group, R.id.bottom_menu_image, ImageView.class).setImageResource(imageId);
        if(Build.VERSION.SDK_INT >= 11){
            get(group, R.id.bottom_menu_image, ImageView.class).setAlpha(0.7f);
        }
        group.setTag(f);
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
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    @Override
    public void message(LKFragment.MessangerMessage message, Bundle data) {
        setTitle(data.getString("title"));
    }

    /**
     * Loads new fragment into the frame.
     */
    @Override
    public void loadFragment(Fragment f, boolean addToBackstack) {
        Log.d("ContentActivity", "loadFragment("+f+")");
         FragmentTransaction transaction = fragmentMgr
                .beginTransaction().setCustomAnimations(R.anim.slide_in_left,R.anim.slide_out_right)
                .replace(R.id.content_frame, f);
        if (addToBackstack) {
            transaction.addToBackStack(null);
        }
        if(list != null) {
            if (f instanceof MapFragment) {
                list.onClick(findViewById(R.id.button3));
            }
        }

        transaction.commit();

    }

    private void moveToFragment(Fragment f) {
        Log.d("ContentActivity", "moveToFragment("+f+")");
        fragmentMgr
            .beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
            .replace(R.id.content_frame, f)
            .commit();
    }

    @Override
    public void popFragmentStack() {
        fragmentMgr.popBackStack();

    }

    /**
     * Sets up the ListView in the navigationdrawer menu.
     */
    private void populateMenu() {
        LayoutInflater inflater = LayoutInflater.from(this);

        ArrayList<LKRightMenuListItem> listItems = new ArrayList<LKRightMenuListItem>();

        rightMenuItems = new ArrayList<LKRightMenuListItem>();

        LKRightMenuListItem header = new LKRightMenuListItem()
                .isStatic(true,inflater.inflate(R.layout.menu_header, null));
        listItems.add(header);

        LKRightMenuListItem foodItem = new LKRightMenuListItem(getString(R.string.food),0, MarkerType.FOOD);
        foodItem.setOnClickListener(new MenuClickSelector(foodItem));
        listItems.add(foodItem);
        rightMenuItems.add(foodItem);


        LKRightMenuListItem funItem = new LKRightMenuListItem(getString(R.string.fun),0, MarkerType.FUN);
        funItem.setOnClickListener(new MenuClickSelector(funItem));
        listItems.add(funItem);
        rightMenuItems.add(funItem);


        LKRightMenuListItem helpItem = new LKRightMenuListItem(getString(R.string.help),0, MarkerType.HELP);
        helpItem.setOnClickListener(new MenuClickSelector(helpItem));
        listItems.add(helpItem);
        rightMenuItems.add(helpItem);

        LKRightMenuListItem wcItem = new LKRightMenuListItem(getString(R.string.wc),0, MarkerType.WC);
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
                    mapFragment.updatePositions();
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
                mapFragment.updatePositions();
            }
        }
    }

    private class BottomMenuClickListener implements OnClickListener {
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
            Fragment f = (Fragment) v.getTag();

            moveToFragment(f);
            selectItem(newSelection, r);
            deselectItem(r);
            this.selected = newSelection;
        }

        private void selectItem(ViewGroup selected, Resources res) {
            selected.setBackgroundColor(res.getColor(R.color.bottom_menu_background_selected));
            get(selected, R.id.bottom_menu_text, TextView.class).setTextColor(res.getColor(R.color.white));
            get(selected, R.id.bottom_menu_shadow, LinearLayout.class).setBackgroundColor(res.getColor(R.color.bottom_menu_shadow_selected));
            if(Build.VERSION.SDK_INT >10) {
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
