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
import se.lundakarnevalen.extern.widget.LKListAdapter;
import se.lundakarnevalen.extern.widget.LKListElement;

/**
 * Created by Markus on 2014-04-16.
 */
public class OtherFragment extends LKFragment{
    private ArrayList<LKListElement> other = new ArrayList<LKListElement>();


    // Every time you switch to this fragment.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_other, null);
        ListView lv = (ListView) rootView.findViewById(R.id.fragment_other_list);
        if(other.isEmpty()) {
            addAllOther();
        }


        ArrayList<LKListAdapter.LKListRow> items = new ArrayList<LKListAdapter.LKListRow>();

        for(int i = 0;i< other.size();i=i+2){
            if(i+1<other.size()) {
                items.add(new LKListAdapter.LKListRow(other.get(i),other.get(i+1)));
            } else {
                items.add(new LKListAdapter.LKListRow(other.get(i),null));
            }

        }
        lv.setAdapter(new LKListAdapter(getContext(), items,getActivity()));


        return rootView;
    }


    private void addAllOther() {
        LKListElement music = new LKListElement(
                "",
                getString(R.string.radio_title),
                "",
                0, 0,
                0,
                R.drawable.radio_logo,
                "");
        music.isMusic = true;
        other.add(music);

        LKListElement otherElement = new LKListElement(
                "",
                getString(R.string.security_title),
                "",
                0, 0,
                0,
                R.drawable.security_logo,
                "");
        otherElement.isOther = true;
        other.add(otherElement);

        otherElement = new LKListElement(
                "",
                getString(R.string.atm_title),
                "",
                0, 0,
                0,
                R.drawable.atm_logo,
                "");
        otherElement.isOther = true;
        other.add(otherElement);

        otherElement = new LKListElement(
                "",
                getString(R.string.parking_title),
                "",
                0, 0,
                0,
                R.drawable.parking_logo,
                "");
        otherElement.isOther = true;
        other.add(otherElement);

        otherElement = new LKListElement(
                "",
                getString(R.string.care_title),
                "",
                0, 0,
                0,
                R.drawable.health_logo,
                "");
        otherElement.isOther = true;
        other.add(otherElement);

        otherElement = new LKListElement(
                "",
                getString(R.string.toilets_title),
                "",
                0, 0,
                0,
                R.drawable.toilets_logo,
                "");
        otherElement.isOther = true;
        other.add(otherElement);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
