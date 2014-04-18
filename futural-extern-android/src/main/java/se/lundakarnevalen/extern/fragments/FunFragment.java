package se.lundakarnevalen.extern.fragments;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import se.lundakarnevalen.extern.android.R;

/**
 * Created by Markus on 2014-04-16.
 */
public class FunFragment extends LKFragment{

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

        lv.setAdapter(new SimpleAdapter(getContext(),
        new ArrayList<Map<String, String>>(){{
            add(new HashMap<String, String>(){{
                put("title","FuturalSpex");
                put("time", "17:00 - 19:00");
            }});
            add(new HashMap<String, String>(){{
                put("title","FuturalSpex");
                put("time", "21:00 - 23:00");
            }});
        }}, R.layout.element_listitem,
            new String[]{ "title", "time" },
            new int[]{ android.R.id.text1, android.R.id.text2 }
        ));
        return rootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }






}