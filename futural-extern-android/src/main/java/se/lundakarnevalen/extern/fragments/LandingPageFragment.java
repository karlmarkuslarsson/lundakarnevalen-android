package se.lundakarnevalen.extern.fragments;

import static se.lundakarnevalen.extern.util.ViewUtil.*;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

import se.lundakarnevalen.extern.android.ContentActivity;
import se.lundakarnevalen.extern.android.R;
import se.lundakarnevalen.extern.widget.LKSchemeMenuArrayAdapter;

/**
 * Created by Markus on 2014-04-16.
 */
public class LandingPageFragment extends LKFragment{

    private LKSchemeMenuArrayAdapter adapter;
    private ListView list;
    private float lat;
    private float lng;

    public LandingPageFragment() {}

    // Every time you switch to this fragment.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_landing_page, null);

        Bundle bundle = getArguments();
        get(rootView,R.id.name,TextView.class).setText(bundle.getString("name"));
        get(rootView,R.id.place,TextView.class).setText(bundle.getString("place"));
        if(bundle.getBoolean("open")) {
            //get(rootView,R.id.open_box,RelativeLayout.class)
            get(rootView,R.id.open_info,TextView.class).setText(R.string.open);
        } else {
            //get(rootView,R.id.open_box,RelativeLayout.class)
            get(rootView,R.id.open_info,TextView.class).setText(R.string.closed);
        }
        if(bundle.getBoolean("cash")) {
           // get(rootView,R.id.cash_picture,ImageView.class).setImageResource(R.drawable.cash_true);
        } else {
            // get(rootView,R.id.cash_picture,ImageView.class).setImageResource(R.drawable.cash_false);
        }
        if(bundle.getBoolean("card")) {
            // get(rootView,R.id.card_picture,ImageView.class).setImageResource(R.drawable.card_true);
        } else {
            // get(rootView,R.id.card_picture,ImageView.class).setImageResource(R.drawable.card_false);
        }
        lat = bundle.getFloat("lat");
        lng = bundle.getFloat("lng");
        get(rootView,R.id.picture,ImageView.class).setImageResource(bundle.getInt("picture"));
        get(rootView,R.id.header_background,ImageView.class).setImageResource(bundle.getInt("top_picture"));
        get(rootView,R.id.question,TextView.class).setText(bundle.getString("question"));
        get(rootView,R.id.text,TextView.class).setText(bundle.getString("desc"));
//        rootView.findViewById(R.id.name).;

        ImageView mapView = (ImageView) rootView.findViewById(R.id.map_picture);
        mapView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("lat,lng","Lat: "+lat+"lng: "+lng);
                MapFragment mapFragment = ContentActivity.class
                        .cast(getActivity()).mapFragment;
                mapFragment.zoomInto(lat,lng);
                ContentActivity.class
                        .cast(getActivity())
                        .loadFragment( mapFragment
                                ,true);
                //
                /*ContentActivity.class
                        .cast(getActivity())
                        .loadFragment(MapFragment.create(true,lat,lng) ,true);
                // Add something ...
            */
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public static LandingPageFragment create(String name, String place, boolean open, boolean cash, boolean card, float lat, float lng, int picture,int topPicture, String question, String desc) {
        LandingPageFragment fragment = new LandingPageFragment();
        Bundle bundle = new Bundle();

        bundle.putString("name",name);
        bundle.putString("place",place);
        bundle.putBoolean("open", open);
        bundle.putBoolean("cash", cash);
        bundle.putBoolean("card", card);
        bundle.putFloat("lat", lat);
        bundle.putFloat("lng", lng);
        bundle.putInt("picture", picture);
        bundle.putInt("top_picture", topPicture);
        bundle.putString("question", question);
        bundle.putString("desc", desc);

        fragment.setArguments(bundle);
        // Add arguments
        return fragment;
    }
}
