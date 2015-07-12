package se.lundakarnevalen.extern.widget;

import android.content.Context;
import android.os.Build;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import se.lundakarnevalen.extern.activities.ContentActivity;
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
        item.setSelected(mContext, item.isSelected);
        if(isFilterAllItem(item)) {
            item.image.setVisibility(View.GONE);
            item.layout.setGravity(Gravity.CENTER);
        } else {
            item.image.setVisibility(View.VISIBLE);
            item.layout.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        }
        int bg = item.isSelected ? R.color.right_menu_button_selected : R.color.right_menu_button;
        item.layout.setBackgroundColor(getContext().getResources().getColor(bg));

        int textColor = item.isSelected ? R.color.white : R.color.white_unselected;
        item.text.setTextColor(getContext().getResources().getColor(textColor));

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            float alpha = item.isSelected ? 1f : 0.7f;
            item.image.setAlpha(alpha);
        }

        return layout;
    }

    private boolean isFilterAllItem(LKRightMenuListItem item) {
        return item.title.equals(mContext.getString(R.string.show_all));
    }

    @Override
    public void notifyDataSetChanged() {
        final List<LKRightMenuListItem> types = selectedItems();
        final LKRightMenuListItem filterAll = findFilterAllItem();
        if(filterAll.isSelected) {
            filterAll.isSelected = (selectedItems().size() < 2); // if all filter selected + 1 more deselect
        } else {
            filterAll.isSelected = (types.isEmpty()); // Nothing selected
        }
        super.notifyDataSetChanged(); //Invalidate views, to update colors
        ContentActivity.class.cast(getContext()).updateMapView(selectedDataTypes());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
        // mDrawerLayout.closeDrawers();
        LKRightMenuListItem item = getItem(pos);
        Context c = parent.getContext();

        if (isFilterAllItem(item)) { // is showAll item
            deselectEverything();
            notifyDataSetChanged(); // Will select every one again
        } else {
            item.setSelected(c, !item.isSelected); // Flip state of current
            notifyDataSetChanged(); // Will deselect ALL
        }
    }

    public void deselectEverything() {
        for (int i = 0; i < getCount(); i++) {
            getItem(i).isSelected = false;
        }
        //notifyDataSetChanged();
    }

    public List<DataType> selectedDataTypes(){
        List<DataType> markerTypes = new ArrayList<DataType>();
        for (int i = 0; i < getCount(); i++) {
            final LKRightMenuListItem item = getItem(i);
            if(item.isSelected) {
                markerTypes.addAll(Arrays.asList(item.markerType));
            }
        }
        return markerTypes;
    }

    private List<LKRightMenuListItem> selectedItems(){
        List<LKRightMenuListItem> items = new ArrayList<LKRightMenuListItem>();
        for (int i = 0; i < getCount(); i++) {
            final LKRightMenuListItem item = getItem(i);
            if(item.isSelected) {
                items.add(item);
            }
        }
        return items;
    }

    private LKRightMenuListItem findFilterAllItem() {
        for (int i = 0; i < getCount(); i++) {
            if(isFilterAllItem(getItem(i))) return getItem(i);
        }
        return null;
    }

    public void add(String title, int logo, DataType[] types, boolean selected) {
        add(new LKRightMenuListItem(title, logo, types, selected));
    }

    public void ensureSelectedFilters(DataType[] types) {
        deselectEverything();
        List<LKRightMenuListItem> items = new ArrayList<LKRightMenuListItem>();
        for (int i = 0; i < getCount(); i++) {
            items.add(getItem(i));
        }
        Collections.sort(items, new Comparator<LKRightMenuListItem>() { // Sort by size to ensure least amount is selected
            @Override
            public int compare(LKRightMenuListItem lhs, LKRightMenuListItem rhs) {
            return lhs.markerType.length < rhs.markerType.length ? -1 : 0;
            }
        });
        for (DataType toBeSelected : types) {
            boolean found = false;
            for (LKRightMenuListItem i : items) {
                for (DataType t : i.markerType) {
                    if(t == toBeSelected) {
                        i.isSelected = true;
                        found = true;
                        break;
                    }
                }
                if(found) break; // found in the smallest set so go on with the next one
            }
        }
        notifyDataSetChanged();
    }

    public static class LKRightMenuListItem {
        private final DataType[] markerType;
        private final int icon;
        private final String title;

        private boolean isSelected = false;

        private LinearLayout layout;
        private TextView text;
        private ImageView image;

        public LKRightMenuListItem(String title, int icon, DataType[] markerType, boolean selected) {
            this.title = title;
            this.icon = icon;
            this.markerType = markerType;
            this.isSelected = selected;
        }

        private void bindValues(View root ) {
            this.layout = get(root, R.id.menu_right_button_layout, LinearLayout.class);
            this.text = get(root, R.id.menu_right_button_text, TextView.class);
            this.image = get(root, R.id.menu_right_button_image, ImageView.class);
            this.image.setImageResource(icon);
            this.text.setText(title);
        }

        public void setSelected(Context c, boolean selected) {
            this.isSelected = selected;
            int bg = selected ? R.color.right_menu_button_selected : R.color.right_menu_button;
            if (this.layout != null) {
                this.layout.setBackgroundColor(c.getResources().getColor(bg));
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("LKRightMenuArrayAdapter{");
        for (int i = 0; i < getCount(); i++) {
            final LKRightMenuListItem item = getItem(i);
            sb.append(item.title).append(": ").append(item.isSelected).append("\n");
        }
        return sb.toString();
    }
}