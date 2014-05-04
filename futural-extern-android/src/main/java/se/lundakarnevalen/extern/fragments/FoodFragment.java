package se.lundakarnevalen.extern.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import se.lundakarnevalen.extern.android.R;
import se.lundakarnevalen.extern.data.DataContainer;
import se.lundakarnevalen.extern.data.DataElement;
import se.lundakarnevalen.extern.data.DataType;
import se.lundakarnevalen.extern.widget.LKListAdapter;
import se.lundakarnevalen.extern.widget.LKListRow;

/**
 * Created by Markus on 2014-04-16.
 */
public class FoodFragment extends LKFragment {


    private List<DataElement> food = DataContainer.getDataOfType(DataType.FOOD);

    // Every time you switch to this fragment.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_food, null);
        ListView lv = (ListView) rootView.findViewById(R.id.fragment_food_list);

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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}
