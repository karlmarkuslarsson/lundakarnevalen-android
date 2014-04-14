package se.lundakarnevalen.extern.android;

import android.content.Context;
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
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import fragments.LKFragment;
import fragments.MapFragment;
import widget.LKRightMenuArrayAdapter;
import widget.LKRightMenuArrayAdapter.*;

public class ContentActivity extends ActionBarActivity implements LKFragment.Messanger {
    private FragmentManager fragmentMgr;
    private LKRightMenuArrayAdapter adapter;
    private ListView bottomMenuList;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        fragmentMgr = getSupportFragmentManager();
        loadFragment(new MapFragment(), false);
        bottomMenuList = (ListView) findViewById(R.id.right_menu_list);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.setScrimColor(Color.TRANSPARENT);
        populateMenu();

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
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
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


        List<LKRightMenuListItem> listItems = new ArrayList<LKRightMenuListItem>();



        listItems.add(new LKRightMenuListItem().isStatic(true).showView(inflater.inflate(R.layout.menu_header, null)));


        listItems.add(new LKRightMenuListItem(getString(R.string.food),
                0, new MapFragment(), fragmentMgr, this, true).closeDrawerOnClick(true, drawerLayout));

        listItems.add(new LKRightMenuListItem(getString(R.string.entertainment), 0,
                new MapFragment(), fragmentMgr, this, true).closeDrawerOnClick(true, drawerLayout));

        listItems.add(new LKRightMenuListItem(getString(R.string.help), 0,
                new MapFragment(), fragmentMgr, this, true).closeDrawerOnClick(true, drawerLayout));

        listItems.add(new LKRightMenuListItem(getString(R.string.wc), 0,
                new MapFragment(), fragmentMgr, this, true).closeDrawerOnClick(true, drawerLayout));

        listItems.add(new LKRightMenuListItem(getString(R.string.show_all), 0,
                new MapFragment(), fragmentMgr, this, true).closeDrawerOnClick(true, drawerLayout));



        adapter = new LKRightMenuArrayAdapter(this, listItems);
        bottomMenuList.setAdapter(adapter);
        bottomMenuList.setOnItemClickListener(adapter);
    }

}
