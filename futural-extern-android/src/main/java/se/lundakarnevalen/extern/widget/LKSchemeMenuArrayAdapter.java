package se.lundakarnevalen.extern.widget;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import se.lundakarnevalen.extern.android.R;

public class LKSchemeMenuArrayAdapter extends ArrayAdapter<LKSchemeMenuArrayAdapter.LKSchemeMenuListItem> implements OnItemClickListener {
    private LayoutInflater inflater;
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");

    public LKSchemeMenuArrayAdapter(Context context, List<LKSchemeMenuListItem> items){
        super(context, android.R.layout.simple_list_item_1, items);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent){
        final LKSchemeMenuListItem item = getItem(pos);

        RelativeLayout wrapper;
        wrapper = (RelativeLayout) inflater.inflate(R.layout.scheme_element, null);

        ImageView image = (ImageView) wrapper.findViewById(R.id.image);
        image.setImageResource(item.icon);

        TextView start = (TextView) wrapper.findViewById(R.id.time1);
        TextView end = (TextView) wrapper.findViewById(R.id.time2);
        start.setText(item.getStartTime());
        end.setText(item.getEndTime());

        TextView place = (TextView) wrapper.findViewById(R.id.place);
        TextView name = (TextView) wrapper.findViewById(R.id.name);

        place.setText(item.place);
        name.setText(item.name);

        // add heart..

        return wrapper;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
        final LKSchemeMenuListItem item = getItem(pos);
        OnClickListener listener = item.listener;
        listener.onClick(view);
        view.setSelected(true);
     }


    /**
     * Class representing a single row in the menu. Used with the LKMenuArrayAdapter.
     * @author alexander
     *
     */
    public static class LKSchemeMenuListItem {
        public int icon;
        public String name;
        public String place;
        public Date startDate;
        public Date endDate;
        private OnClickListener listener;
        boolean reminder = false;


        /**
         * Creates list item..
         * @param icon Icon next to text
         */
        public LKSchemeMenuListItem(String place, String name, int icon, Date startDate, Date endDate, boolean reminder){
            this.place = place;
            this.name = name;
            this.icon = icon;
            this.startDate = startDate;
            this.endDate = endDate;
            this.reminder = reminder;
            this.listener = new OnClickListener() {

                @Override
                public void onClick(View v) {

                    //            clearBackStack(fragmentMgr);

                    //          fragmentMgr.beginTransaction().replace(R.id.content_frame, fragment).commit();
                }

                private void clearBackStack(FragmentManager fragmentMgr) {

                    for(int i = 0; i < fragmentMgr.getBackStackEntryCount(); i++) {
                        Log.d("ContentActivity", "Removed from backstack");
                        fragmentMgr.popBackStack();
                    }
                }
            };

        }

        public String getStartTime() {
            return dateFormat.format(startDate);
        }
        public String getEndTime() {
            return dateFormat.format(endDate);
        }

        public void setOnClickListener(OnClickListener listener) {
            this.listener = listener;
        }
    }
}