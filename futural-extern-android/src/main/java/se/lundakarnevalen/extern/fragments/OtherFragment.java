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

public class OtherFragment extends LKFragment{
    private List<DataElement> other = new ArrayList<>();
    private final int ID = 4;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_other, container, false);
        ListView lv = (ListView) root.findViewById(R.id.fragment_other_list);

        ContentActivity activity = ContentActivity.class.cast(getActivity());
        activity.allBottomsUnfocus();
        activity.focusBottomItem(ID);

        if (other.isEmpty()) {
            other.addAll(DataMultiContainer.getAllOtherMultiData());
            other.addAll(DataContainer.getDataOfType(DataType.BILJETTERIET));

        }

        ArrayList<LKListRow> items = new ArrayList<>();

        for (int i = 0; i < other.size(); i = i+2) {
            if(i + 1 < other.size()) {
                items.add(new LKListRow(other.get(i), other.get(i+1)));
            } else {
                items.add(new LKListRow(other.get(i), null));
            }
        }

        lv.setAdapter(new LKListAdapter(getContext(), items,getActivity()));



        return root;
    }
}
