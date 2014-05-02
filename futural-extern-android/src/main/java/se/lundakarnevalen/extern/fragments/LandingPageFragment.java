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
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import se.lundakarnevalen.extern.android.ContentActivity;
import se.lundakarnevalen.extern.android.R;
import se.lundakarnevalen.extern.widget.LKListElement;
import se.lundakarnevalen.extern.widget.LKListElementType;
import se.lundakarnevalen.extern.widget.LKSchemeAdapter;
import se.lundakarnevalen.extern.widget.LKTimeObject;

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

        final LKListElement element = bundle.getParcelable("element");
        get(rootView,R.id.name,TextView.class).setText(element.title);
        get(rootView,R.id.place,TextView.class).setText(element.place);

        Calendar c = Calendar.getInstance();

        switch (c.DAY_OF_MONTH) {
            case 23:
                get(rootView,R.id.open_info,TextView.class).setText(element.timeFriday);
                //open(c.)
                //if(c.)
                break;
            case 24:
                if(c.HOUR_OF_DAY < 6) {
                    get(rootView,R.id.open_info,TextView.class).setText(element.timeFriday);
                }
                get(rootView,R.id.open_info,TextView.class).setText(element.timeSaturday);
                break;
            case 25:
                if(c.HOUR_OF_DAY < 6) {
                    get(rootView,R.id.open_info,TextView.class).setText(element.timeSaturday);
                }
                get(rootView,R.id.open_info,TextView.class).setText(element.timeSunday);
                break;
            default:
                get(rootView,R.id.open_info,TextView.class).setText(element.timeSunday);
                break;
        }

        if(element.cash == 1) {
           // get(rootView,R.id.cash_picture,ImageView.class).setImageResource(R.drawable.cash_true);
        } else {
            // get(rootView,R.id.cash_picture,ImageView.class).setImageResource(R.drawable.cash_false);
        }
        if(element.card==1) {
            // get(rootView,R.id.card_picture,ImageView.class).setImageResource(R.drawable.card_true);
        } else {
            // get(rootView,R.id.card_picture,ImageView.class).setImageResource(R.drawable.card_false);
        }
        lat = element.lat;
        lng = element.lng;
        int type = element.type;

        get(rootView,R.id.picture,ImageView.class).setImageResource(element.picture);
        get(rootView,R.id.header_background,ImageView.class).setImageResource(element.headerPicture);

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


        if(type==LKListElementType.FUN) {
            get(rootView, R.id.question, TextView.class).setText(element.question);
            get(rootView, R.id.text, TextView.class).setText(element.info);
//        rootView.findViewById(R.id.name).;
        } else if(type == LKListElementType.FOOD) {
            get(rootView, R.id.question, TextView.class).setText(element.question);
            get(rootView, R.id.text, TextView.class).setText(element.info);
            get(rootView, R.id.middleLayout, RelativeLayout.class).setBackgroundResource(R.color.green_background);

            if(element.menu != null) {
                ListView lv = (ListView) rootView.findViewById(R.id.menu_food_list);



        lv.setAdapter(new SimpleAdapter(getContext(),
                new ArrayList<Map<String, String>>() {{

                    for (int i = 0;i<element.menu.size();i++) {
                        final int j = i;
                        add(new HashMap<String, String>() {{
                            put("name", element.menu.get(j));
                            put("price", element.menuPrice.get(j));
                        }});

                    }
                }}, R.layout.menu_food_element,
                new String[]{"name", "price"},
                new int[]{android.R.id.text1, android.R.id.text2}
        ));

            }

        }
            return rootView;
    }


    @Override
    public void onStart() {
        super.onStart();
        //ContentActivity.class.cast(getActivity()).hideBottomMenu();

        ContentActivity.class.cast(getActivity()).allBottomsActive();
    }

    @Override
    public void onStop() {
        //ContentActivity.class.cast(getActivity()).showBottomMenu();
        super.onStop();
    }

    public static LandingPageFragment create(LKListElement element) {
        LandingPageFragment fragment = new LandingPageFragment();
        Bundle bundle = new Bundle();
    /*
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
    */
        bundle.putParcelable("element",element);

        fragment.setArguments(bundle);
        // Add arguments
        return fragment;
    }
}
