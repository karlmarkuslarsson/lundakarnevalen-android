package se.lundakarnevalen.extern.fragments;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import se.lundakarnevalen.extern.android.ContentActivity;
import se.lundakarnevalen.extern.android.R;

/**
 * Created by Markus on 2014-04-16.
 */
public class FunFragment extends LKFragment{

    private HashSet<Fun> fun = new HashSet<Fun>();

    private static class FunItem {
        String text1;
        String text2;

        private FunItem(String text1, String text2) {
            this.text1 = text1;
            this.text2 = text2;
        }
    }

    // Every time you switch to this fragment.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_fun, null);

        ListView lv = (ListView) rootView.findViewById(R.id.fragment_fun_list);
        if(fun.isEmpty()) {
            addAllFun();
        }

        lv.setAdapter(new SimpleAdapter(getContext(),
                new ArrayList<Map<String, String>>() {{
                    add(new HashMap<String, String>() {{
                        put("title", "FuturalSpex");
                        put("time", "17:00 - 19:00");
                    }});
                    add(new HashMap<String, String>() {{
                        put("title", "FuturalSpex");
                        put("time", "21:00 - 23:00");
                    }});
                }}, R.layout.element_listitem,
                new String[]{"title", "time"},
                new int[]{android.R.id.text1, android.R.id.text2}
        ));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ContentActivity.class
                        .cast(getActivity())
                        .loadFragment(LandingPageFragment.create(getString(R.string.filmen_title),"Lindqvist",false,true,true,23,21,R.drawable.monk,R.drawable.monk,getString(R.string.filmen_title),getString(R.string.filmen_info)), true);
            }
        });
        return rootView;
    }

    private void addAllFun() {
        fun.add(new Fun(getString(R.string.kabare_place),getString(R.string.kabare_title),getString(R.string.kabare_info),55.7042667f,13.193833333333334f,R.drawable.header_kabare,R.drawable.header_kabare,getString(R.string.kabare_question)));

        // add all fun here...

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    private class Fun {
        String title;
        String place;
        float lat;
        float lng;
        int headerPicture;
        int picture;
        String question;
        String info;

        private Fun(String place, String title, String info, float lat, float lng, int headerPicture, int picture, String question) {
            this.place = place;
            this.title = title;
            this.info = info;
            this.lat = lat;
            this.lng = lng;
            this.headerPicture = headerPicture;
            this.picture = picture;
            this.question = question;
        }


    }



}
