package se.lundakarnevalen.extern.android;

import android.content.Context;

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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

public class ContentActivity extends ActionBarActivity implements LKFragment.Messanger {
    private FragmentManager fragmentMgr;
    private LKRightMenuArrayAdapter adapter;
    private ListView rightMenuList;
    private DrawerLayout drawerLayout;
    private RelativeLayout currentSelectedBottomMenu;
    private RelativeLayout mapLayout;
    private ActionBar actionBar;
    private MapFragment mapFragment;
    private ArrayList<LKRightMenuListItem> rightMenuItems = new ArrayList<LKRightMenuListItem>();
    private LKRightMenuListItem showAllItem;
    private boolean allItemsActivated = true;

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
        generateLowerMenu();
        actionBar = getSupportActionBar();
        setupActionbar();
    }

    private void setupActionbar() {
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View root = inflater.inflate(R.layout.action_bar_layout, null);
        actionBar.setCustomView(root);
    }

    private void generateLowerMenu() {
        LinearLayout bottomMenu = (LinearLayout) findViewById(R.id.bottomFrameMenu);
        RelativeLayout fun = (RelativeLayout) bottomMenu.findViewById(R.id.button1);
        fun.setOnClickListener(new BottomMenuClickListener(new FunFragment()));
        RelativeLayout food = (RelativeLayout) bottomMenu.findViewById(R.id.button2);
        food.setOnClickListener(new BottomMenuClickListener(new FoodFragment()));
        mapLayout = (RelativeLayout) bottomMenu.findViewById(R.id.button3);
        mapLayout.setOnClickListener(new BottomMenuClickListener(mapFragment));
        currentSelectedBottomMenu = mapLayout;
        RelativeLayout scheme = (RelativeLayout) bottomMenu.findViewById(R.id.button4);
        scheme.setOnClickListener(new BottomMenuClickListener(new SchemeFragment()));
        RelativeLayout other = (RelativeLayout) bottomMenu.findViewById(R.id.button5);
        other.setOnClickListener(new BottomMenuClickListener(new OtherFragment()));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.content, menu);
        return true;
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
    public void loadFragment(Fragment fragment, boolean addToBackstack) {
        Log.d("ContentActivity", "loadFragment()");
        FragmentTransaction transaction = fragmentMgr.beginTransaction().replace(R.id.content_frame, fragment);
        if (addToBackstack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    @Override
    public void popFragmentStack() {
        fragmentMgr.popBackStack();
    }

    /**
     * Sets up the ListView in the navigationdrawer menu.
     */
    private void populateMenu() {
        // Create logo and sigill objects.
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //View menuBottom = inflater.inflate(R.layout., null);

        ArrayList<LKRightMenuListItem> listItems = new ArrayList<LKRightMenuListItem>();

        rightMenuItems = new ArrayList<LKRightMenuListItem>();
        LKRightMenuListItem header = new LKRightMenuListItem().isStatic(true).showView(inflater.inflate(R.layout.menu_header, null));
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
                    RelativeLayout button = (RelativeLayout) v.findViewById(R.id.button);
                    button.setBackgroundColor(getResources().getColor(R.color.right_menu_button_selected));
                    item.isOn = true;
                    mapFragment.updatePositions();
                }
                    // show all..
            } else {
                RelativeLayout button = (RelativeLayout) v.findViewById(R.id.button);
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
                    item.isOn = false;
                } else {
                    mapFragment.changeActive(item.markerType,false);
                    button.setBackgroundColor(getResources().getColor(R.color.right_menu_button));
                    item.isOn = true;
                }
                showAllItem.isOn = false;
                showAllItem.button.setBackgroundColor(getResources().getColor(R.color.right_menu_button));
                mapFragment.updatePositions();
            }
        }


    }
    private class BottomMenuClickListener implements OnClickListener {
        private LKFragment fragment;


        public BottomMenuClickListener(LKFragment fragment) {
            this.fragment = fragment;
        }

        @Override
        public void onClick(View v) {
            if(v.equals(currentSelectedBottomMenu)) {
                return;
            }
            for(int i = 0; i < fragmentMgr.getBackStackEntryCount(); i++) {
                Log.d("ContentActivity", "Removed from backstack");
                fragmentMgr.popBackStack();
            }

            loadFragment(fragment,true);

            currentSelectedBottomMenu.setBackgroundColor(getResources().getColor(R.color.red));
            ((TextView)currentSelectedBottomMenu.findViewById(R.id.text)).setTextColor(getResources().getColor(R.color.white_unselected));
            v.setBackgroundColor(getResources().getColor(R.color.bottom_menu_background_selected));
            ((TextView)v.findViewById(R.id.text)).setTextColor(getResources().getColor(R.color.white));
            currentSelectedBottomMenu = (RelativeLayout) v;
            if(v.equals(mapLayout)) {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            } else {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                drawerLayout.closeDrawers();
            }

        }
    }


}
