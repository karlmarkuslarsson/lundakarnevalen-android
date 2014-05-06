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
public class FunFragment extends LKFragment {

    private List<DataElement> fun = new ArrayList<DataElement>();

    // Every time you switch to this fragment.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_fun, container, false);

        ListView lv = (ListView) root.findViewById(R.id.fragment_fun_list);

        if (fun.isEmpty()) {
            fun.addAll(DataContainer.getDataOfType(DataType.FUN));
        }

        /*
        lv.setAdapter(new SimpleAdapter(getContext(),
                new ArrayList<Map<String, String>>() {{
                    for(final Fun f: fun) {
                        add(new HashMap<String, String>() {{
                            put("title", f.title);
                            put("place", f.place);
                            put("picture", ""+f.headerPicture);
                            put("picture", ""+f.headerPicture);
                        }});
                    }
                }}, R.layout.element_lk_list,
                new String[]{"title", "place", "picture"},
                new int[]{R.id.text1, R.id.text2, R.id.image}
        ));
        */
        ArrayList<LKListRow> items = new ArrayList<LKListRow>();

        for(int i = 0;i< fun.size();i=i+2){
            if(i+1<fun.size()) {
                items.add(new LKListRow(fun.get(i),fun.get(i+1)));
            } else {
                items.add(new LKListRow(fun.get(i),null));
            }

        }
        lv.setAdapter(new LKListAdapter(getContext(), items,getActivity()));

        /*
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ContentActivity.class
                        .cast(getActivity())
                        .loadFragmentAddingBS(
                                LandingPageFragment.create(
                                        fun.get(position).title,
                                        fun.get(position).place,
                                        true, true, true,
                                        fun.get(position).lat,
                                        fun.get(position).lng,
                                        fun.get(position).picture,
                                        fun.get(position).headerPicture,
                                        fun.get(position).question,
                                        fun.get(position).info)
                        );
            }
        });
        */
        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private static class FunItem {
        String text1;
        String text2;

        private FunItem(String text1, String text2) {
            this.text1 = text1;
            this.text2 = text2;
        }
    }



}
