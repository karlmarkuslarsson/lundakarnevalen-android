package se.lundakarnevalen.extern.widget;


        import java.util.List;

        import android.content.Context;
        import android.support.v4.widget.DrawerLayout;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.View.OnClickListener;
        import android.view.ViewGroup;
        import android.widget.AdapterView;
        import android.widget.AdapterView.OnItemClickListener;
        import android.widget.ArrayAdapter;
        import android.widget.RelativeLayout;
        import android.widget.TextView;

        import se.lundakarnevalen.extern.android.R;

/**
 *
 */
public class LKRightMenuArrayAdapter extends ArrayAdapter<LKRightMenuArrayAdapter.LKRightMenuListItem> implements OnItemClickListener {
   private LayoutInflater inflater;

    public LKRightMenuArrayAdapter(Context context, List<LKRightMenuListItem> items){
        super(context, android.R.layout.activity_list_item, items);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public View getView(int pos, View convertView, ViewGroup parent){
        final LKRightMenuListItem item = getItem(pos);

        if(item.isStatic) {

            return item.staticView;
        }
        RelativeLayout wrapper;
        if(item.title.equals(getContext().getString(R.string.show_all))) {
            item.isOn = true;

            wrapper = (RelativeLayout) inflater.inflate(R.layout.menu_bottom, null);
            item.button = wrapper.findViewById(R.id.button);
            if(item.isActive && wrapper != null){
                wrapper.setSelected(true);
            }
            item.button.setBackgroundColor(getContext().getResources().getColor(R.color.right_menu_button_selected));
            item.isOn = true;


        } else {
            wrapper = (RelativeLayout) inflater.inflate(R.layout.menu_element, null);
            item.button = wrapper.findViewById(R.id.button);
            if(item.isActive && wrapper != null){
                wrapper.setSelected(true);
            }

        }

        if(wrapper != null) {
            item.text = (TextView) wrapper.findViewById(R.id.bottom_menu_item);
            item.text.setText(item.title);
        }

        return wrapper;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
        final LKRightMenuListItem item = getItem(pos);
        if(!item.enable) {
            return;
        }

        OnClickListener listener = item.listener;
        if(listener != null){
            listener.onClick(view);
            view.setSelected(true);
            if(item.navDrawer != null && item.closeDrawerOnClick){
                item.navDrawer.closeDrawers();
            }
        }
    }

    @Override
    public boolean isEnabled(int pos){
        // Make statics no enabled.

        return !getItem(pos).isStatic;
    }

    /**
     * Class representing a single row in the menu. Used with the LKMenuArrayAdapter.
     * @author alexander
     *
     */
    public static class LKRightMenuListItem {
        public int icon;
        public String title;
        OnClickListener listener;
        DrawerLayout navDrawer;
        boolean isStatic = false;
        View staticView;
        boolean closeDrawerOnClick = false;

        boolean isActive = false;
        public boolean isOn = true;

        public int markerType;

        public TextView text;
        public boolean enable = true;

        public View button;

        /**
         * std. constr.
         */
        public LKRightMenuListItem(){

        }

        /**
         * To be used with statics in listview.
         * @param isStatic true if static
         * @return list item
         */
        public LKRightMenuListItem isStatic(boolean isStatic, View view){
            this.isStatic = isStatic;
            this.staticView = view;
            return this;
        }


        /**
         * Creates list item..
         * @param title Text in menu to show
         * @param icon Icon next to text
         */
        public LKRightMenuListItem(String title, int icon, int markerType){
            this.title = title;
            this.icon = icon;
            this.markerType = markerType;

        }

        /**
         * Call this to close the navigationdrawer when item is clicked.
         * @param closeDrawerOnClick If true the drawer will close.
         * @param layout The drawerlayout to be closed.
         */
        public LKRightMenuListItem closeDrawerOnClick(boolean closeDrawerOnClick, DrawerLayout layout){
            this.closeDrawerOnClick = closeDrawerOnClick;
            this.navDrawer = layout;
            return this;
        }

        public void setOnClickListener(OnClickListener listener) {
            this.listener = listener;
        }
    }
}