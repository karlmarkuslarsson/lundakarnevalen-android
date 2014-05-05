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
public class OtherFragment extends LKFragment{
    private List<DataElement> other = new ArrayList<DataElement>();

    // Every time you switch to this fragment.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_other, null);
        ListView lv = (ListView) rootView.findViewById(R.id.fragment_other_list);

        if (other.isEmpty()) {
            other.addAll(DataContainer.getDataOfType(DataType.TRAIN));
            other.addAll(DataContainer.getDataOfType(DataType.RADIO));
            other.addAll(DataContainer.getDataOfType(DataType.OTHER));
        }

        ArrayList<LKListRow> items = new ArrayList<LKListRow>();

        for(int i = 0;i< other.size();i=i+2){
            if(i+1<other.size()) {
                items.add(new LKListRow(other.get(i),other.get(i+1)));
            } else {
                items.add(new LKListRow(other.get(i),null));
            }

        }

        lv.setAdapter(new LKListAdapter(getContext(), items,getActivity()));


        return rootView;
    }

}
