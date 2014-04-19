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
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

import se.lundakarnevalen.extern.android.R;
import se.lundakarnevalen.extern.widget.LKSchemeMenuArrayAdapter;

import static se.lundakarnevalen.extern.util.ViewUtil.get;

/**
 * Created by Markus on 2014-04-16.
 */
public class SchemeFragment extends LKFragment {

    private Date startDate;
    private LKSchemeMenuArrayAdapter adapter;
    private ListView list;
    private View rootView;
    private int day = -1; // 0 = friday, 1 =  saturday, 2 = sunday

    // Every time you switch to this fragment.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_scheme, null);
        list = (ListView) rootView.findViewById(R.id.list_scheme);

        RelativeLayout leftArrow = get(rootView, R.id.left_arrow, RelativeLayout.class);
        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (day > 0) {
                    day--;
                    populateMenu(get(rootView, R.id.dayText, TextView.class));
                }
                // shift day...
            }
        });
        RelativeLayout rightArrow = get(rootView, R.id.right_arrow, RelativeLayout.class);
        rightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (day != 2) {
                    day++;
                    populateMenu(get(rootView, R.id.dayText, TextView.class));
                    // shift day...
                }
            }
        });


        populateMenu(get(rootView, R.id.dayText, TextView.class));
        return rootView;
    }


    /**
     * Sets up the ListView in the navigationdrawer menu.
     */
    private void populateMenu(TextView textView) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, Calendar.MAY);
        cal.set(Calendar.DATE, 16);
        cal.set(Calendar.YEAR, 2014);
        cal.set(Calendar.SECOND, 00);

        // Create logo and sigill objects.
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //View menuBottom = inflater.inflate(R.layout., null);
        if (startDate == null) {
            cal.set(Calendar.MINUTE, 00);
            cal.set(Calendar.HOUR_OF_DAY, 1);
            startDate = cal.getTime();
        }


        if (day == -1) {
            Date currentDate = new Date();
            if (currentDate.before(startDate)) {
                day = 0;
            } else {
                switch (currentDate.getDate()) {
                    case 16:
                        day = 0;
                        break;
                    case 17:
                        day = 1;
                        break;

                    default:
                        day = 2;
                        break;
                }
            }
        }


        NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);

        SharedPreferences sharedPref = getContext().getSharedPreferences("lundkarnevalen", Context.MODE_PRIVATE);
        String set = sharedPref.getString("notifications", "");
        String split[] = set.split(";");
        HashSet<String> activated = new HashSet<String>();

        for (int i = 0; i < split.length; i++) {
            activated.add(split[i]);
        }

        //

        ArrayList<LKSchemeMenuArrayAdapter.LKSchemeMenuListItem> listItems = new ArrayList<LKSchemeMenuArrayAdapter.LKSchemeMenuListItem>();


//        Invigning, stora scenen 13:00-14:00
//        Lat: 55°42'20.53"N Long: 13°11'37.55"O

        Date myDate;
        Date myDateEnd;
        if (day == 0) {
            textView.setText(R.string.friday);
            cal.set(Calendar.MINUTE, 00);
            cal.set(Calendar.HOUR_OF_DAY, 13);

            myDate = cal.getTime();
            cal.set(Calendar.HOUR_OF_DAY, 14);

            myDateEnd = cal.getTime();
        /*
        Fredag:
        Invigning, stora scenen 13:00-14:00
        Lat: 55°42'20.53"N Long: 13°11'37.55"O
        */
            LKSchemeMenuArrayAdapter.LKSchemeMenuListItem invigning =
                    new LKSchemeMenuArrayAdapter.LKSchemeMenuListItem(getString(R.string.big_scene),
                            getString(R.string.inauguration),
                            R.drawable.test_nojen, myDate, myDateEnd, activated);
            listItems.add(invigning);
/*
        Fredag:
        Orkesterkamp, stora scenen 14:30-15:30
        Lat: 55°42'20.53"N Long: 13°11'37.55"O
        */
            cal.set(Calendar.MINUTE, 30);
            cal.set(Calendar.HOUR_OF_DAY, 14);
            myDate = cal.getTime();
            cal.set(Calendar.HOUR_OF_DAY, 15);
            myDateEnd = cal.getTime();

            LKSchemeMenuArrayAdapter.LKSchemeMenuListItem orkester = new LKSchemeMenuArrayAdapter.LKSchemeMenuListItem(getString(R.string.big_scene), getString(R.string.orchestra), R.drawable.test_nojen, myDate, myDateEnd, activated);
            listItems.add(orkester);

            /*
        Fredag:
        Per Hammar, lilla scenen 17:15
            Lat: 55°42'26.07"N  Long:  13°11'45.45"O
            55.7072417f, 13.195958333333333f
        */
            cal.set(Calendar.MINUTE, 15);
            cal.set(Calendar.HOUR_OF_DAY, 17);
            myDate = cal.getTime();
            cal.set(Calendar.HOUR_OF_DAY, 19);
            myDateEnd = cal.getTime();

            LKSchemeMenuArrayAdapter.LKSchemeMenuListItem hammar = new LKSchemeMenuArrayAdapter.LKSchemeMenuListItem(getString(R.string.small_scene), getString(R.string.hammar), R.drawable.test_nojen, myDate, myDateEnd, activated);
            listItems.add(hammar);


/*
        Fredag:
        Sousou, Maher Cissoko, stora scenen 17:15
        Lat: 55°42'20.53"N Long: 13°11'37.55"O
        */
            cal.set(Calendar.MINUTE, 15);
            cal.set(Calendar.HOUR_OF_DAY, 17);
            myDate = cal.getTime();
            cal.set(Calendar.HOUR_OF_DAY, 19);
            myDateEnd = cal.getTime();

            LKSchemeMenuArrayAdapter.LKSchemeMenuListItem sousou = new LKSchemeMenuArrayAdapter.LKSchemeMenuListItem(getString(R.string.big_scene), getString(R.string.sousou), R.drawable.test_nojen, myDate, myDateEnd, activated);
            listItems.add(sousou);
/*
        Fredag:
        Bo Kaspers, stora scenen 19:15
        Lat: 55°42'20.53"N Long: 13°11'37.55"O
        */
            cal.set(Calendar.MINUTE, 15);
            cal.set(Calendar.HOUR_OF_DAY, 19);
            myDate = cal.getTime();
            cal.set(Calendar.HOUR_OF_DAY, 21);
            myDateEnd = cal.getTime();

            LKSchemeMenuArrayAdapter.LKSchemeMenuListItem boKasper = new LKSchemeMenuArrayAdapter.LKSchemeMenuListItem(getString(R.string.big_scene), getString(R.string.bo_kasper), R.drawable.test_nojen, myDate, myDateEnd, activated);
            listItems.add(boKasper);


/*
        Fredag:
        Bo Kaspers, stora scenen 21:15
        Lat: 55°42'20.53"N Long: 13°11'37.55"O
        */
            cal.set(Calendar.MINUTE, 15);
            cal.set(Calendar.HOUR_OF_DAY, 21);
            myDate = cal.getTime();
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 00);
            myDateEnd = cal.getTime();

            LKSchemeMenuArrayAdapter.LKSchemeMenuListItem lucyLove = new LKSchemeMenuArrayAdapter.LKSchemeMenuListItem(getString(R.string.big_scene), getString(R.string.lucy_love), R.drawable.test_nojen, myDate, myDateEnd, activated);
            listItems.add(lucyLove);

/*
        Fredag:
        E-type, stora scenen 23:00
        Lat: 55°42'20.53"N Long: 13°11'37.55"O
        */
            cal.set(Calendar.MINUTE, 00);
            cal.set(Calendar.HOUR_OF_DAY, 23);
            myDate = cal.getTime();
            cal.set(Calendar.HOUR_OF_DAY, 24);
            cal.set(Calendar.MINUTE, 00);
            myDateEnd = cal.getTime();

            LKSchemeMenuArrayAdapter.LKSchemeMenuListItem eType = new LKSchemeMenuArrayAdapter.LKSchemeMenuListItem(getString(R.string.big_scene), getString(R.string.e_type), R.drawable.test_nojen, myDate, myDateEnd, activated);
            listItems.add(eType);


        } else if (day == 1) {
            textView.setText(R.string.saturday);

/*
        Lördag:
        Tåget avgår 13:00, åter ca 15:00
        Se tågväg bifogad om ni vill lägga in den i appen i någon kartfunktion
*/
            cal.set(Calendar.MINUTE, 00);
            cal.set(Calendar.DATE, 17);
            cal.set(Calendar.HOUR_OF_DAY, 13);
            myDate = cal.getTime();

            cal.set(Calendar.HOUR_OF_DAY, 15);
            myDateEnd = cal.getTime();

            LKSchemeMenuArrayAdapter.LKSchemeMenuListItem train = new LKSchemeMenuArrayAdapter.LKSchemeMenuListItem(getString(R.string.place_train), getString(R.string.train), R.drawable.test_nojen, myDate, myDateEnd, activated);
            listItems.add(train);


            /*
        Lördag:
        Sandra Mosh, lilla scenen 17:15
            Lat: 55°42'26.07"N  Long:  13°11'45.45"O
            55.7072417f, 13.195958333333333f
        */
            cal.set(Calendar.MINUTE, 15);
            cal.set(Calendar.HOUR_OF_DAY, 17);
            myDate = cal.getTime();
            cal.set(Calendar.HOUR_OF_DAY, 19);
            cal.set(Calendar.MINUTE, 00);
            myDateEnd = cal.getTime();

            LKSchemeMenuArrayAdapter.LKSchemeMenuListItem sandra = new LKSchemeMenuArrayAdapter.LKSchemeMenuListItem(getString(R.string.small_scene), getString(R.string.sandra), R.drawable.test_nojen, myDate, myDateEnd, activated);
            listItems.add(sandra);


            /*
        Lördag:
        Ida Redig, stora scenen 17:15
        Lat: 55°42'20.53"N Long: 13°11'37.55"O
        */
            cal.set(Calendar.MINUTE, 15);
            cal.set(Calendar.HOUR_OF_DAY, 17);
            myDate = cal.getTime();
            cal.set(Calendar.HOUR_OF_DAY, 19);
            cal.set(Calendar.MINUTE, 00);
            myDateEnd = cal.getTime();

            LKSchemeMenuArrayAdapter.LKSchemeMenuListItem ida = new LKSchemeMenuArrayAdapter.LKSchemeMenuListItem(getString(R.string.big_scene), getString(R.string.ida), R.drawable.test_nojen, myDate, myDateEnd, activated);
            listItems.add(ida);
            /*
        Lördag:
        Hurricane Love, stora scenen 19:00
        Lat: 55°42'20.53"N Long: 13°11'37.55"O
        */
            cal.set(Calendar.MINUTE, 00);
            cal.set(Calendar.HOUR_OF_DAY, 19);
            myDate = cal.getTime();
            cal.set(Calendar.HOUR_OF_DAY, 21);
            cal.set(Calendar.MINUTE, 00);
            myDateEnd = cal.getTime();

            LKSchemeMenuArrayAdapter.LKSchemeMenuListItem hurricane = new LKSchemeMenuArrayAdapter.LKSchemeMenuListItem(getString(R.string.big_scene), getString(R.string.hurricane), R.drawable.test_nojen, myDate, myDateEnd, activated);
            listItems.add(hurricane);
/*
        Lördag:
        Linnea, stora scenen 21:00
        Lat: 55°42'20.53"N Long: 13°11'37.55"O
        */
            cal.set(Calendar.MINUTE, 00);
            cal.set(Calendar.HOUR_OF_DAY, 21);
            myDate = cal.getTime();
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 00);
            myDateEnd = cal.getTime();

            LKSchemeMenuArrayAdapter.LKSchemeMenuListItem linnea = new LKSchemeMenuArrayAdapter.LKSchemeMenuListItem(getString(R.string.big_scene), getString(R.string.linnea), R.drawable.test_nojen, myDate, myDateEnd, activated);
            listItems.add(linnea);
/*
        Lördag:
        MOVITS!, stora scenen 23:00
        Lat: 55°42'20.53"N Long: 13°11'37.55"O
        */
            cal.set(Calendar.MINUTE, 00);
            cal.set(Calendar.HOUR_OF_DAY, 23);
            myDate = cal.getTime();
            cal.set(Calendar.HOUR_OF_DAY, 24);
            cal.set(Calendar.MINUTE, 00);
            myDateEnd = cal.getTime();

            LKSchemeMenuArrayAdapter.LKSchemeMenuListItem movits = new LKSchemeMenuArrayAdapter.LKSchemeMenuListItem(getString(R.string.big_scene), getString(R.string.movits), R.drawable.test_nojen, myDate, myDateEnd, activated);
            listItems.add(movits);

        } else {
            textView.setText(R.string.sunday);
        /*
        Söndag:

        Tåget avgår 13:00, åter ca 15:00
        Se tågväg bifogad om ni vill lägga in den i appen i någon kartfunktion
        Se bifogat dokument om generell tidsplan för detta, artisterna och dess tider samt öppettider.
                Koordinater lilla scenen:
        Lat: 55°42'26.07"N Long: 13°11'45.45"O
*/
            cal.set(Calendar.MINUTE, 00);
            cal.set(Calendar.DATE, 18);
            cal.set(Calendar.HOUR_OF_DAY, 13);
            myDate = cal.getTime();
            cal.set(Calendar.HOUR_OF_DAY, 15);
            myDateEnd = cal.getTime();

            LKSchemeMenuArrayAdapter.LKSchemeMenuListItem train2 = new LKSchemeMenuArrayAdapter.LKSchemeMenuListItem(getString(R.string.place_train), getString(R.string.train), R.drawable.test_nojen, myDate, myDateEnd, activated);
            listItems.add(train2);

            /*
        Söndag:
        Partiet, lilla scenen 17:15
            Lat: 55°42'26.07"N  Long:  13°11'45.45"O
            55.7072417f, 13.195958333333333f
        */
            cal.set(Calendar.MINUTE, 15);
            cal.set(Calendar.HOUR_OF_DAY, 17);
            myDate = cal.getTime();
            cal.set(Calendar.HOUR_OF_DAY, 18);
            cal.set(Calendar.MINUTE, 30);
            myDateEnd = cal.getTime();

            LKSchemeMenuArrayAdapter.LKSchemeMenuListItem partiet = new LKSchemeMenuArrayAdapter.LKSchemeMenuListItem(getString(R.string.small_scene), getString(R.string.partiet), R.drawable.test_nojen, myDate, myDateEnd, activated);
            listItems.add(partiet);


            /*
        Söndag:
        FRIDA SUNDEMO, stora scenen 17:00
        Lat: 55°42'20.53"N Long: 13°11'37.55"O
        */
            cal.set(Calendar.MINUTE, 00);
            cal.set(Calendar.HOUR_OF_DAY, 17);
            myDate = cal.getTime();
            cal.set(Calendar.HOUR_OF_DAY, 18);
            cal.set(Calendar.MINUTE, 30);
            myDateEnd = cal.getTime();

            LKSchemeMenuArrayAdapter.LKSchemeMenuListItem frida = new LKSchemeMenuArrayAdapter.LKSchemeMenuListItem(getString(R.string.big_scene), getString(R.string.frida), R.drawable.test_nojen, myDate, myDateEnd, activated);
            listItems.add(frida);

            /*
        Söndag:
        WINTERGATAN, stora scenen 18:30
        Lat: 55°42'20.53"N Long: 13°11'37.55"O
        */
            cal.set(Calendar.MINUTE, 30);
            cal.set(Calendar.HOUR_OF_DAY, 18);
            myDate = cal.getTime();
            cal.set(Calendar.HOUR_OF_DAY, 20);
            cal.set(Calendar.MINUTE, 30);
            myDateEnd = cal.getTime();

            LKSchemeMenuArrayAdapter.LKSchemeMenuListItem wintergatan = new LKSchemeMenuArrayAdapter.LKSchemeMenuListItem(getString(R.string.big_scene), getString(R.string.wintergatan), R.drawable.test_nojen, myDate, myDateEnd, activated);
            listItems.add(wintergatan);
/*
        Söndag:
        TIMBAKTU, stora scenen 20:30
        Lat: 55°42'20.53"N Long: 13°11'37.55"O
        */
            cal.set(Calendar.MINUTE, 30);
            cal.set(Calendar.HOUR_OF_DAY, 20);
            myDate = cal.getTime();
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 00);
            myDateEnd = cal.getTime();

            LKSchemeMenuArrayAdapter.LKSchemeMenuListItem timbaktu = new LKSchemeMenuArrayAdapter.LKSchemeMenuListItem(getString(R.string.big_scene), getString(R.string.timbaktu), R.drawable.test_nojen, myDate, myDateEnd, activated);
            listItems.add(timbaktu);

            /*
        Söndag:
        Discoteka, stora scenen 23:00
        Lat: 55°42'20.53"N Long: 13°11'37.55"O
        */
            cal.set(Calendar.MINUTE, 00);
            cal.set(Calendar.HOUR_OF_DAY, 23);
            myDate = cal.getTime();
            cal.set(Calendar.HOUR_OF_DAY, 24);
            cal.set(Calendar.MINUTE, 00);
            myDateEnd = cal.getTime();

            LKSchemeMenuArrayAdapter.LKSchemeMenuListItem discoteka = new LKSchemeMenuArrayAdapter.LKSchemeMenuListItem(getString(R.string.big_scene), getString(R.string.discoteka), R.drawable.test_nojen, myDate, myDateEnd, activated);
            listItems.add(discoteka);

        }
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
