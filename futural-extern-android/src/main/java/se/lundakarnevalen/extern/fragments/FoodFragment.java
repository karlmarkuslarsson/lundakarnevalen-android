package se.lundakarnevalen.extern.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import se.lundakarnevalen.extern.android.ContentActivity;
import se.lundakarnevalen.extern.android.R;

/**
 * Created by Markus on 2014-04-16.
 */
public class FoodFragment extends LKFragment{


    // Every time you switch to this fragment.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_food, null);
        ListView lv = (ListView) rootView.findViewById(R.id.fragment_food_list);

        lv.setAdapter(new SimpleAdapter(getContext(),
                new ArrayList<Map<String, String>>(){{
                    add(new HashMap<String, String>() {{
                        put("title","Fine Dine");
                        put("plats", "Område: D7");
                    }});
                    add(new HashMap<String, String>() {{
                        put("title","KarneKörv!");
                        put("plats", "Område: F3");
                    }});
                }}, R.layout.element_listitem,
                new String[]{ "title", "plats" },
                new int[]{ android.R.id.text1, android.R.id.text2 }
        ));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ContentActivity.class
                        .cast(getActivity())
                        .loadFragment(LandingPageFragment.create("Filip","Lindqvist",false,true,true,23,21,R.drawable.monk,R.drawable.monk,"VAD ÄR FILIP BRA PÅ?","ÄTA PIZZA!"), true);
            }
        });
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
     }
}
