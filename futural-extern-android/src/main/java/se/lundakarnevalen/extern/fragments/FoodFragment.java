package se.lundakarnevalen.extern.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import se.lundakarnevalen.extern.activities.ContentActivity;
import se.lundakarnevalen.extern.android.R;
import se.lundakarnevalen.extern.data.DataContainer;
import se.lundakarnevalen.extern.data.DataElement;
import se.lundakarnevalen.extern.data.DataMultiContainer;
import se.lundakarnevalen.extern.data.DataType;
import se.lundakarnevalen.extern.widget.LKListAdapter;
import se.lundakarnevalen.extern.widget.LKListRow;

public class FoodFragment extends LKFragment {
    private final int ID = 1;

    private List<DataElement> food = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ContentActivity activity = ContentActivity.class.cast(getActivity());

        View root = inflater.inflate(R.layout.fragment_food, container, false);
        ListView lv = (ListView) root.findViewById(R.id.fragment_food_list);
        activity.focusBottomItem(ID);
        if (food.isEmpty()) {
            food.addAll(DataContainer.getDataOfType(DataType.FOOD));
            food.addAll(DataMultiContainer.getAllFoodMultiData());
        }

        ArrayList<LKListRow> items = new ArrayList<>();
        for(int i = 0;i< food.size();i=i+2){
            if(i+1<food.size()) {
                items.add(new LKListRow(food.get(i),food.get(i+1)));
            } else {
                items.add(new LKListRow(food.get(i),null));
            }

        }
        lv.setAdapter(new LKListAdapter(getContext(), items,getActivity()));
        return root;
    }


}
