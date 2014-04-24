package se.lundakarnevalen.extern.fragments;

import static se.lundakarnevalen.extern.util.ViewUtil.*;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Calendar;

import se.lundakarnevalen.extern.android.ContentActivity;
import se.lundakarnevalen.extern.android.R;
import se.lundakarnevalen.extern.widget.LKSchemeAdapter;

/**
 * Created by Markus on 2014-04-16.
 */
public class LandingPageFragment extends LKFragment{

    private LKSchemeAdapter adapter;
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
        Calendar c = Calendar.getInstance();
        int open = bundle.getInt("open");
        int close = bundle.getInt("close");
        if(isOpen(c.HOUR_OF_DAY,open,close)) {
            get(rootView,R.id.open_info,TextView.class).setText(R.string.open);
        } else {
            get(rootView,R.id.open_box,RelativeLayout.class).setBackgroundColor(getResources().getColor(R.color.red_button_background));
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
        int type = bundle.getInt("type");

        get(rootView,R.id.picture,ImageView.class).setImageResource(bundle.getInt("picture"));
        get(rootView,R.id.header_background,ImageView.class).setImageResource(bundle.getInt("top_picture"));

        ImageView mapView = (ImageView) rootView.findViewById(R.id.map_picture);
        mapView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("lat,lng","Lat: "+lat+"lng: "+lng);
                ContentActivity contentActivity = ContentActivity.class.cast(getActivity());

                MapFragment mapFragment = contentActivity.mapFragment;
                //mapFragment.zoomInto(lat,lng);

                contentActivity.loadFragmentWithAdd(mapFragment);
                //
                /*ContentActivity.class
                        .cast(getActivity())
                        .loadFragmentWithAdd(MapFragment.create(true,lat,lng) ,true);
                // Add something ...
            */
            }
        });


        if(type==1) {
            get(rootView, R.id.question, TextView.class).setText(bundle.getString("question"));
            get(rootView, R.id.text, TextView.class).setText(bundle.getString("desc"));
//        rootView.findViewById(R.id.name).;
        } else if(type == 2) {
            get(rootView, R.id.question, TextView.class).setText(bundle.getString("question"));
            get(rootView, R.id.text, TextView.class).setText(bundle.getString("desc"));
            get(rootView, R.id.middleLayout, RelativeLayout.class).setBackgroundResource(R.color.green_background);

//
        }
            return rootView;
    }

    private boolean isOpen(int hourOfDay, int open, int close) {
        if(close < open) {
            if(hourOfDay < close) {
                return true;
            } else if(hourOfDay >= open) {
                return true;
            } else {
                return false;
            }

        } else {
            if(hourOfDay >= open) {
                if(hourOfDay < close) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        ContentActivity.class.cast(getActivity()).hideBottomMenu();
    }

    @Override
    public void onStop() {
        ContentActivity.class.cast(getActivity()).showBottomMenu();
        super.onStop();
    }

    public static LandingPageFragment create(String name, String place, boolean cash, boolean card, float lat, float lng, int picture,int topPicture, String question, String desc,int open, int close, int type) {
        LandingPageFragment fragment = new LandingPageFragment();
        Bundle bundle = new Bundle();

        bundle.putString("name",name);
        bundle.putString("place",place);
        bundle.putBoolean("cash", cash);
        bundle.putBoolean("card", card);
        bundle.putFloat("lat", lat);
        bundle.putFloat("lng", lng);
        bundle.putInt("picture", picture);
        bundle.putInt("top_picture", topPicture);
        bundle.putString("question", question);
        bundle.putString("desc", desc);
        bundle.putInt("type", type);
        bundle.putInt("open", open);
        bundle.putInt("close",close);
        fragment.setArguments(bundle);
        // Add arguments
        return fragment;
    }
}
