package se.lundakarnevalen.extern.widget;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
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
import se.lundakarnevalen.extern.data.DataType;

import static se.lundakarnevalen.extern.util.ViewUtil.get;

public class LKRightMenuArrayAdapter extends ArrayAdapter<LKRightMenuArrayAdapter.LKRightMenuListItem> implements OnItemClickListener {
    private static final String LOG_TAG = LKRightMenuArrayAdapter.class.getSimpleName();

    private final LayoutInflater mInflater;
    private final Context mContext;

    public LKRightMenuArrayAdapter(Context context){
        super(context, android.R.layout.activity_list_item, new ArrayList<LKRightMenuListItem>());
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
            item.setSelected(mContext, true); //TODO: check if any other filters are active
            item.image.setVisibility(View.GONE);
            item.layout.setGravity(Gravity.CENTER);
        } else {
            item.setSelected(mContext, item.selected); //TODO: check if it was selected before.
            item.image.setVisibility(View.VISIBLE);
            item.layout.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
            int bg = item.selected ? R.color.right_menu_button_selected : R.color.right_menu_button;
            item.layout.setBackgroundColor(getContext().getResources().getColor(bg));
        }

        return layout;
    }

    @Override
    public void notifyDataSetChanged() {
        final List<DataType> types = selectedTypes();
        int showAllElementIdx = getCount() - 1;

        if(types.isEmpty()) {
            //deselectAll(); // Causes crash at first launch
            getItem(showAllElementIdx).selected = true;
        } else {
            getItem(showAllElementIdx).selected = false;
        }
        super.notifyDataSetChanged();

        ContentActivity.class.cast(getContext()).updateMapView(types);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
        // mDrawerLayout.closeDrawers();
        LKRightMenuListItem item = getItem(pos);
        Context c = parent.getContext();

        int showAllElementIdx = getCount() - 1;
        boolean showAllSelected = (showAllElementIdx == pos);

        if (showAllSelected) { // is showAll item
            deselectAll();
            getItem(showAllElementIdx).setSelected(c, true);
        } else {
            item.setSelected(c, !item.selected); // Flip state
            getItem(showAllElementIdx).selected = false;
            //getItem(showAllElementIdx).setSelected(c, false);
            notifyDataSetChanged();
        }

        final List<DataType> types = selectedTypes();

        if(types.isEmpty()) {
            deselectAll();
            getItem(showAllElementIdx).setSelected(c, true);
        }

        if (c != null) {
            ContentActivity.class.cast(c).updateMapView(types);
        } else {
            Log.wtf(LOG_TAG, "ContentActivity was null");
        }
    }

    public void deselectAll() {
        for (int i = 0; i < getCount(); i++) {
            getItem(i).selected = false;
        }
        //notifyDataSetChanged();
    }

    public List<DataType> selectedTypes(){
        List<DataType> markerTypes = new ArrayList<DataType>();
        for (int i = 0; i < getCount(); i++) {
            LKRightMenuListItem item = getItem(i);
            if(item.selected){
                markerTypes.addAll(Arrays.asList(item.markerType));
            }
        }
        return markerTypes;
    }

    public int getIndexForIcon(int icon){
        for (int i = 0; i < getCount(); i++) {
            LKRightMenuListItem item = getItem(i);
            if (item.icon == icon) {
                return i;
            }
        }
        return -1;
    }

    public void addItem(String title, int logo, DataType[] types, boolean selected) {
        add(new LKRightMenuListItem(title, logo, types, selected));
    }

    public static class LKRightMenuListItem {
        private final DataType[] markerType;
        private final int icon;
        private final String title;

        public boolean selected = false;

        private LinearLayout layout;
        private TextView text;
        private ImageView image;

        public LKRightMenuListItem(String title, int icon, DataType[] markerType, boolean selected) {
            this.title = title;
            this.icon = icon;
            this.markerType = markerType;
            this.selected = selected;
        }

        private void bindValues(View root ) {
            this.layout = get(root, R.id.menu_right_button_layout, LinearLayout.class);
            this.text = get(root, R.id.menu_right_button_text, TextView.class);
            this.image = get(root, R.id.menu_right_button_image, ImageView.class);
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