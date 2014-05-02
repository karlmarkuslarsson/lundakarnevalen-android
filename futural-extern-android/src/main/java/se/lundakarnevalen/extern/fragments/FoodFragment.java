package se.lundakarnevalen.extern.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import se.lundakarnevalen.extern.android.ContentActivity;
import se.lundakarnevalen.extern.android.R;
import se.lundakarnevalen.extern.widget.LKListAdapter;
import se.lundakarnevalen.extern.widget.LKListElement;
import se.lundakarnevalen.extern.widget.LKListElementType;
import se.lundakarnevalen.extern.widget.LKListRow;

/**
 * Created by Markus on 2014-04-16.
 */
public class FoodFragment extends LKFragment {


    private ArrayList<LKListElement> food = new ArrayList<LKListElement>();

    // Every time you switch to this fragment.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_food, null);
        ListView lv = (ListView) rootView.findViewById(R.id.fragment_food_list);

        if (food.isEmpty()) {
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

        ArrayList<LKListRow> items = new ArrayList<LKListRow>();

        for(int i = 0;i< food.size();i=i+2){
            if(i+1<food.size()) {
                items.add(new LKListRow(food.get(i),food.get(i+1)));
            } else {
                items.add(new LKListRow(food.get(i),null));
            }

        }
        lv.setAdapter(new LKListAdapter(getContext(), items,getActivity()));

        return rootView;
    }

    private void addAllFood() {
        food.add(new LKListElement(
                getString(R.string.cocktail_place),
                getString(R.string.cocktail_title),
                getString(R.string.cocktail_info),
                55.706362f, 13.195165f,
                R.drawable.monk,
                R.drawable.header_kabare,
                getString(R.string.cocktail_question),
                "15:30-22:30","15:30-23:30","15:30-22:30",
                LKListElementType.FOOD
                ));

        food.add(new LKListElement(
                getString(R.string.hipp_baren_place),
                getString(R.string.hipp_baren_title),
                getString(R.string.hipp_baren_info),
                55.706521f, 13.195431f,
                R.drawable.monk,
                R.drawable.header_kabare,
                getString(R.string.hipp_baren_question),
                "15:30-22:30","15:30-23:30","15:30-22:30",
                LKListElementType.FOOD));


        food.add(new LKListElement(
                getString(R.string.folkan_place),
                getString(R.string.folkan_title),
                getString(R.string.folkan_info),
                55.706841f, 13.196030f,
                R.drawable.monk,
                R.drawable.header_kabare,
                getString(R.string.folkan_question),
                "15:30-22:30","15:30-23:30","15:30-22:30",
                LKListElementType.FOOD));

        ArrayList<String> menu = new ArrayList<String>();
        ArrayList<String> menuPrice = new ArrayList<String>();
        menu.add("hejburgare");
        menu.add("tomteburgare");
        menuPrice.add("12");
        menuPrice.add("16");

        food.add(new LKListElement(
                getString(R.string.krog_undervatten_place),
                getString(R.string.krog_undervatten_title),
                getString(R.string.krog_undervatten_info),
                55.705154f, 13.19458f,
                R.drawable.monk,
                R.drawable.undervatten_logo,
                getString(R.string.krog_undervatten_question),
                "15:30-22:30","15:30-23:30","15:30-22:30",
                LKListElementType.FOOD, menu, menuPrice));

        food.add(new LKListElement(
                getString(R.string.krog_lajka_place),
                getString(R.string.krog_lajka_title),
                getString(R.string.krog_lajka_info),
                55.705697f, 13.194630f,
                R.drawable.monk,
                R.drawable.undervatten_logo,
                getString(R.string.krog_lajka_question),
                "12:00-01:00","12:00-01:00","12:00-24:00",
                LKListElementType.FOOD));


        food.add(new LKListElement(
                getString(R.string.krog_thyme_place),
                getString(R.string.krog_thyme_title),
                getString(R.string.krog_lajka_info),
                55.705697f, 13.194630f,
                R.drawable.monk,
                R.drawable.undervatten_logo,
                getString(R.string.krog_thyme_question),
                "14:00-02:00","14:00-02:00","14:00-02:00",
                LKListElementType.FOOD));

        // add all fun here...

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}
