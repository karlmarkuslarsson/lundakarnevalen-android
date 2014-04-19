package se.lundakarnevalen.extern.fragments;

import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

import se.lundakarnevalen.extern.map.MarkerType;
import se.lundakarnevalen.extern.android.R;
import se.lundakarnevalen.extern.widget.LKSchemeMenuArrayAdapter;

/**
 * Created by Markus on 2014-04-16.
 */
public class SchemeFragment extends LKFragment{

    private LKSchemeMenuArrayAdapter adapter;
    private ListView list;

    // Every time you switch to this fragment.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_scheme, null);
        list = (ListView) rootView.findViewById(R.id.list_scheme);

        populateMenu();
        return rootView;
    }



    /**
     * Sets up the ListView in the navigationdrawer menu.
     */
    private void populateMenu() {
        // Create logo and sigill objects.
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //View menuBottom = inflater.inflate(R.layout., null);


        NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);

        SharedPreferences sharedPref = getContext().getSharedPreferences("lundkarnevalen",Context.MODE_PRIVATE);
        String set = sharedPref.getString("notifications", "");
        String split[] = set.split(";");
        HashSet<String> activated = new HashSet<String>();

        for(int i = 0;i<split.length;i++) {
            activated.add(split[i]);
        }

        //
        Date date = new Date();
        date.setDate(20);
        date.setHours(5);
        date.setMinutes(5);
        date.setSeconds(5);
        date.setMonth(5);

        ArrayList<LKSchemeMenuArrayAdapter.LKSchemeMenuListItem> listItems = new ArrayList<LKSchemeMenuArrayAdapter.LKSchemeMenuListItem>();

        LKSchemeMenuArrayAdapter.LKSchemeMenuListItem foodItem = new LKSchemeMenuArrayAdapter.LKSchemeMenuListItem("KUNGEN","BÄST",R.drawable.test_nojen,date,date,activated);
        listItems.add(foodItem);
        LKSchemeMenuArrayAdapter.LKSchemeMenuListItem randomItem = new LKSchemeMenuArrayAdapter.LKSchemeMenuListItem("FILIP","HEJ",R.drawable.test_spexet,date,date,activated);
        listItems.add(randomItem);


//        Invigning, stora scenen 13:00-14:00
//        Lat: 55°42'20.53"N Long: 13°11'37.55"O

        Date myDate;

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, Calendar.MAY);
        cal.set(Calendar.DATE, 16);
        cal.set(Calendar.YEAR, 2014);
        cal.set(Calendar.MINUTE,00);
        cal.set(Calendar.SECOND,00);

        cal.set(Calendar.HOUR_OF_DAY,13);

        myDate = cal.getTime();
        cal.set(Calendar.HOUR_OF_DAY,14);

        Date myDateEnd;
        myDateEnd = cal.getTime();
        /*
        Fredag:
        Invigning, stora scenen 13:00-14:00
        Lat: 55°42'20.53"N Long: 13°11'37.55"O
        */
        LKSchemeMenuArrayAdapter.LKSchemeMenuListItem invigning = new LKSchemeMenuArrayAdapter.LKSchemeMenuListItem(getString(R.string.big_scene),getString(R.string.invigning),R.drawable.test_nojen,myDate,myDateEnd,activated);
        listItems.add(invigning);
/*
        Fredag:
        Orkesterkamp, stora scenen 14:30-15:30
        Lat: 55°42'20.53"N Long: 13°11'37.55"O
        */
        cal.set(Calendar.MINUTE,30);
        cal.set(Calendar.HOUR_OF_DAY,14);
        myDate = cal.getTime();
        cal.set(Calendar.HOUR_OF_DAY,15);
        myDateEnd = cal.getTime();

        LKSchemeMenuArrayAdapter.LKSchemeMenuListItem orkester = new LKSchemeMenuArrayAdapter.LKSchemeMenuListItem(getString(R.string.big_scene),getString(R.string.orkester),R.drawable.test_nojen,myDate,myDateEnd,activated);
        listItems.add(orkester);

/*
        Lördag:
        Tåget avgår 13:00, åter ca 15:00
        Se tågväg bifogad om ni vill lägga in den i appen i någon kartfunktion
*/
        cal.set(Calendar.MINUTE,00);
        cal.set(Calendar.DATE,17);
        cal.set(Calendar.HOUR_OF_DAY,13);
        myDate = cal.getTime();
        cal.set(Calendar.HOUR_OF_DAY,15);
        myDateEnd = cal.getTime();

        LKSchemeMenuArrayAdapter.LKSchemeMenuListItem train = new LKSchemeMenuArrayAdapter.LKSchemeMenuListItem(getString(R.string.place_train),getString(R.string.train),R.drawable.test_nojen,myDate,myDateEnd,activated);
        listItems.add(train);


        /*
        Söndag:

        Tåget avgår 13:00, åter ca 15:00
        Se tågväg bifogad om ni vill lägga in den i appen i någon kartfunktion
        Se bifogat dokument om generell tidsplan för detta, artisterna och dess tider samt öppettider.
                Koordinater lilla scenen:
        Lat: 55°42'26.07"N Long: 13°11'45.45"O
*/
        cal.set(Calendar.MINUTE,00);
        cal.set(Calendar.DATE,18);
        cal.set(Calendar.HOUR_OF_DAY,13);
        myDate = cal.getTime();
        cal.set(Calendar.HOUR_OF_DAY,15);
        myDateEnd = cal.getTime();

        LKSchemeMenuArrayAdapter.LKSchemeMenuListItem train2 = new LKSchemeMenuArrayAdapter.LKSchemeMenuListItem(getString(R.string.place_train),getString(R.string.train),R.drawable.test_nojen,myDate,myDateEnd,activated);
        listItems.add(train2);


        adapter = new LKSchemeMenuArrayAdapter(getContext(), listItems);
        Log.d("adapter", "" + adapter);
        Log.d("list", "" + list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(adapter);

    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
