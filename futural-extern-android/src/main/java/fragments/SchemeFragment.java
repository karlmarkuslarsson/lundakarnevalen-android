package fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import se.lundakarnevalen.extern.Map.MarkerType;
import se.lundakarnevalen.extern.android.R;
import se.lundakarnevalen.extern.widget.LKSchemeMenuArrayAdapter;

/**
 * Created by Markus on 2014-04-16.
 */
public class SchemeFragment extends LKFragment{

    private LKSchemeMenuArrayAdapter adapter;
    private ListView list;

    // Every time you switch to this fragment.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_scheme, null);
        list = (ListView) rootView.findViewById(R.id.list_scheme);

        populateMenu();
        return rootView;
    }



    /**
     * Sets up the ListView in the navigationdrawer menu.
     */
    private void populateMenu() {
        // Create logo and sigill objects.
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //View menuBottom = inflater.inflate(R.layout., null);

        ArrayList<LKSchemeMenuArrayAdapter.LKSchemeMenuListItem> listItems = new ArrayList<LKSchemeMenuArrayAdapter.LKSchemeMenuListItem>();

        LKSchemeMenuArrayAdapter.LKSchemeMenuListItem foodItem = new LKSchemeMenuArrayAdapter.LKSchemeMenuListItem(getString(R.string.food),0, MarkerType.FOOD);
        listItems.add(foodItem);
        LKSchemeMenuArrayAdapter.LKSchemeMenuListItem randomItem = new LKSchemeMenuArrayAdapter.LKSchemeMenuListItem(getString(R.string.other),0, MarkerType.HELP);
        listItems.add(randomItem);

        adapter = new LKSchemeMenuArrayAdapter(getContext(), listItems);
        Log.d("adapter", "" + adapter);
        Log.d("list", "" + list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(adapter);

    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
