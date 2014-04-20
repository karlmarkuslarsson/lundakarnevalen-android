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

/**
 * Created by Markus on 2014-04-16.
 */
public class FoodFragment extends LKFragment{


    private ArrayList<Food> food = new ArrayList<Food>();

    // Every time you switch to this fragment.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_food, null);
        ListView lv = (ListView) rootView.findViewById(R.id.fragment_food_list);

        if(food.isEmpty()) {
            addAllFood();
        }

/*
        lv.setAdapter(new SimpleAdapter(getContext(),
                new ArrayList<Map<String, String>>(){{
                   for(final Food f: food) {
                       add(new HashMap<String, String>() {{
                           put("title",f.title);
                           put("plats", f.place);
                       }});

                   }
                }}, R.layout.element_listitem,
                new String[]{ "title", "plats" },
                new int[]{ android.R.id.text1, android.R.id.text2 }
        ));
  */

        ArrayList<LKListAdapter.LKListElement> items = new ArrayList<LKListAdapter.LKListElement>();

        for(final Food f: food) {
            items.add(new LKListAdapter.LKListElement(f.picture, f.title,f.place,f.headerPicture));

        }
        lv.setAdapter(new LKListAdapter(getContext(),items));


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ContentActivity.class
                        .cast(getActivity())
                        .loadFragment(LandingPageFragment.create(food.get(position).title,food.get(position).place,true,food.get(position).cash,food.get(position).card,food.get(position).lat,food.get(position).lng,food.get(position).picture,food.get(position).headerPicture,food.get(position).question,food.get(position).info), true);
            }
        });
        return rootView;
    }

    private void addAllFood() {
        food.add(new Food(
                getString(R.string.cocktail_place),
                getString(R.string.cocktail_title),
                getString(R.string.cocktail_info),
                55.706362f, 13.195165f,
                R.drawable.monk,
                R.drawable.header_kabare,
                getString(R.string.cocktail_question),true,true));

        food.add(new Food(
                getString(R.string.hipp_baren_place),
                getString(R.string.hipp_baren_title),
                getString(R.string.hipp_baren_info),
                55.706521f, 13.195431f,
                R.drawable.monk,
                R.drawable.header_kabare,
                getString(R.string.hipp_baren_question),true,true));


        food.add(new Food(
                getString(R.string.folkan_place),
                getString(R.string.folkan_title),
                getString(R.string.folkan_info),
                55.706841f, 13.196030f,
                R.drawable.monk,
                R.drawable.header_kabare,
                getString(R.string.folkan_question),true,true));






        // add all fun here...

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
     }

    private class Food {
        String title;
        String place;
        float lat;
        float lng;
        int headerPicture;
        int picture;
        String question;
        String info;
        boolean cash;
        boolean card;

        private Food(String place, String title, String info, float lat, float lng, int headerPicture, int picture, String question, boolean cash, boolean card) {
            this.place = place;
            this.title = title;
            this.info = info;
            this.lat = lat;
            this.lng = lng;
            this.headerPicture = headerPicture;
            this.picture = picture;
            this.question = question;
            this.card = card;
            this.cash = cash;
        }

    }
}
