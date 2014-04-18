package se.lundakarnevalen.extern.fragments;

import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

import se.lundakarnevalen.extern.android.R;
import se.lundakarnevalen.extern.widget.LKSchemeMenuArrayAdapter;

/**
 * Created by Markus on 2014-04-16.
 */
public class LandingPageFragment extends LKFragment{

    private LKSchemeMenuArrayAdapter adapter;
    private ListView list;

    public LandingPageFragment() {}

    // Every time you switch to this fragment.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_landing_page, null);

        Bundle bundle = getArguments();
//        rootView.findViewById(R.id.name).;
        bundle.getString("name");

        ImageView mapView = (ImageView) rootView.findViewById(R.id.map_picture);
        mapView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Add something ...
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public static LandingPageFragment create(String name, String place, boolean open, boolean cash, boolean card, float lat, float lng, int picture, String question, String desc) {
        LandingPageFragment fragment = new LandingPageFragment();
        Bundle bundle = new Bundle();
        bundle.putString("name",name);
        bundle.putString("place",place);
        bundle.putBoolean("open",open);
        bundle.putBoolean("cash",cash);
        bundle.putBoolean("card",card);
        bundle.putFloat("lat",lat);
        bundle.putFloat("lng",lng);
        bundle.putInt("picture",picture);
        bundle.putString("question",question);
        bundle.putString("desc",desc);

        fragment.setArguments(bundle);
        // Add arguments
        return fragment;
    }
}
