package se.lundakarnevalen.extern.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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

public class FunFragment extends LKFragment {

    private List<DataElement> fun = new ArrayList<>();
    private final int ID = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ContentActivity activity = ContentActivity.class.cast(getActivity());
        View root = inflater.inflate(R.layout.fragment_fun, container, false);

        ListView lv = (ListView) root.findViewById(R.id.fragment_fun_list);

        activity.allBottomsUnfocus();
        activity.focusBottomItem(ID);


        if (fun.isEmpty()) {
            fun.addAll(DataContainer.getDataOfType(DataType.FUN));
            fun.addAll(DataContainer.getDataOfType(DataType.SCENE));
            fun.addAll(DataMultiContainer.getAllFunMultiData());
        }

        ArrayList<LKListRow> items = new ArrayList<>();

        for(int i = 0;i< fun.size();i=i+2){
            if(i+1<fun.size()) {
                items.add(new LKListRow(fun.get(i),fun.get(i+1)));
            } else {
                items.add(new LKListRow(fun.get(i),null));
            }

        }
        lv.setAdapter(new LKListAdapter(getContext(), items,getActivity()));

        return root;
    }
}
