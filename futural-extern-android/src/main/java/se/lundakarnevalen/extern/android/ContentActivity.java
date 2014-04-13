package se.lundakarnevalen.extern.android;

import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import fragments.LKFragment;
import fragments.MapFragment;

public class ContentActivity extends ActionBarActivity implements LKFragment.Messanger {
    private FragmentManager fragmentMgr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        fragmentMgr = getSupportFragmentManager();
        loadFragment(new MapFragment(), false);
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

}
