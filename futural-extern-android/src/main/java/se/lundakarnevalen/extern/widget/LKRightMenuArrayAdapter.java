package se.lundakarnevalen.extern.widget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import se.lundakarnevalen.extern.android.ContentActivity;
import se.lundakarnevalen.extern.android.R;

import static se.lundakarnevalen.extern.util.ViewUtil.get;

public class LKRightMenuArrayAdapter extends ArrayAdapter<LKRightMenuArrayAdapter.LKRightMenuListItem> implements OnItemClickListener {
    private static final String LOG_TAG = LKRightMenuArrayAdapter.class.getSimpleName();

    private final LayoutInflater mInflater;
    private final Context mContext;

    public LKRightMenuArrayAdapter(Context context, List<LKRightMenuListItem> items){
        super(context, android.R.layout.activity_list_item, items);
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent){
        final LKRightMenuListItem item = getItem(pos);

        View layout = convertView;

        if(convertView == null) {
            layout = mInflater.inflate(R.layout.menu_right_element, parent, false);
        }

        item.bindValues(layout);

        if(item.title.equals(mContext.getString(R.string.show_all))) {
            item.setSelected(mContext, true);
            item.image.setVisibility(View.GONE);
            item.layout.setGravity(Gravity.CENTER);
        } else {
            item.setSelected(mContext, false);
        }

        return layout;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
        // mDrawerLayout.closeDrawers();
        LKRightMenuListItem item = getItem(pos);
        Context c = parent.getContext();

        int showAllIdx = getCount() - 1;
        boolean showAllSelected = showAllIdx == pos;
        if (showAllSelected) { // is showAll item
            deselectAll();
            getItem(showAllIdx).setSelected(c, true);
        } else {
            item.setSelected(c, !item.selected); // Flip state
            getItem(showAllIdx).setSelected(c, false);
        }

        ContentActivity activity = ContentActivity.class.cast(c);
        if (activity != null) {
            activity.updateMapView(selectedTypes());
        } else {
            Log.wtf(LOG_TAG, "ContentActivity was null");
        }
    }

    private void deselectAll() {
        for (int i = 0; i < getCount(); i++) {
            getItem(i).setSelected(getContext(), false);
        }
    }

    public List<Integer> selectedTypes(){
        List<Integer> markerTypes = new ArrayList<Integer>();
        for (int i = 0; i < getCount(); i++) {
            LKRightMenuListItem item = getItem(i);
            if(item.selected){
                markerTypes.add(item.markerType);
            }
        }
        return markerTypes;
    }

    /**
     * Class representing a single row in the menu. Used with the LKMenuArrayAdapter.
     * @author alexander
     */
    public static class LKRightMenuListItem {
        private final int markerType;
        private final int icon;
        private final String title;

        private boolean selected = false;

        private LinearLayout layout;
        private TextView text;
        private ImageView image;

        public LKRightMenuListItem(String title, int icon, int markerType){
            this.title = title;
            this.icon = icon;
            this.markerType = markerType;
        }

        private void bindValues(View layout) {
            this.layout = get(layout, R.id.menu_right_button_layout, LinearLayout.class);
            this.text = get(layout, R.id.menu_right_button_text, TextView.class);
            this.image = get(layout, R.id.menu_right_button_image, ImageView.class);
            this.image.setImageResource(icon);
            this.text.setText(title);
        }

        public void setSelected(Context c, boolean selected) {
            this.selected = selected;
            int bg = selected ? R.color.right_menu_button_selected : R.color.right_menu_button;
            this.layout.setBackgroundColor(c.getResources().getColor(bg));
        }
    }
}