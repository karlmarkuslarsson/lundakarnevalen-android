package se.lundakarnevalen.extern.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import se.lundakarnevalen.extern.android.ContentActivity;
import se.lundakarnevalen.extern.android.R;
import se.lundakarnevalen.extern.data.DataContainer;
import se.lundakarnevalen.extern.data.DataElement;
import se.lundakarnevalen.extern.data.DataMultiContainer;
import se.lundakarnevalen.extern.data.DataType;
import se.lundakarnevalen.extern.widget.LKListAdapter;
import se.lundakarnevalen.extern.widget.LKListRow;

/**
 * Created by Markus on 2014-04-16.
 */
public class OtherFragment extends LKFragment{
    private List<DataElement> other = new ArrayList<DataElement>();
    private final int ID = 4;
    // Every time you switch to this fragment.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_other, container, false);
        ListView lv = (ListView) root.findViewById(R.id.fragment_other_list);

        ContentActivity activity = ContentActivity.class.cast(getActivity());
        activity.allBottomsUnfocus();
        activity.focusBottomItem(ID);

        if (other.isEmpty()) {
            other.addAll(DataContainer.getDataOfType(DataType.PLAYER_FUTURAL));
            other.addAll(DataContainer.getDataOfType(DataType.TRAIN));
            other.addAll(DataContainer.getDataOfType(DataType.PLAYER_RADIO));
            other.addAll(DataContainer.getDataOfType(DataType.BILJETTERIET));
            other.addAll(DataContainer.getDataOfType(DataType.ATM));
            other.addAll(DataContainer.getDataOfType(DataType.PARKING));
            other.addAll(DataContainer.getDataOfType(DataType.SECURITY));
            other.addAll(DataMultiContainer.getAllOtherMultiData());
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



        return root;
    }

}
