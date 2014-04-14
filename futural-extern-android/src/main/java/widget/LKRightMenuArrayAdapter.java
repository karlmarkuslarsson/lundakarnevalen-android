package widget;


        import java.util.List;

        import android.content.Context;
        import android.support.v4.app.Fragment;
        import android.support.v4.app.FragmentManager;
        import android.support.v4.widget.DrawerLayout;
        import android.util.Log;
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

    private final String LOG_TAG = "ArrayAdapter";
    private LayoutInflater inflater;

    public LKRightMenuArrayAdapter(Context context, List<LKRightMenuListItem> items){
      //  super(context, android.R.layout.simple_list_item_1, items);
        super(context, android.R.layout.activity_list_item, items);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void deleteItem(LKRightMenuListItem item) {
        remove(item);
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent){
        final LKRightMenuListItem item = getItem(pos);

        if(item.isStatic) {
            return item.staticView;
        }
        RelativeLayout wrapper = null;
        if(item.title.equals(getContext().getString(R.string.show_all))) {
            wrapper = (RelativeLayout) inflater.inflate(R.layout.menu_bottom, null);
        } else {
           wrapper = (RelativeLayout) inflater.inflate(R.layout.menu_element, null);
        }
        if(item.isActive){
            wrapper.setSelected(true);
            Log.d(LOG_TAG, "was selecete");
        }

        item.text = (TextView) wrapper.findViewById(R.id.text);
        item.text.setText(item.title);


        if(item.isActive) {
            wrapper.setSelected(true);
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
            if(item.navDrawer != null && item.closeDrawerOnClick)
                item.navDrawer.closeDrawers();
            Log.d(LOG_TAG, "click");
        }
        else
            Log.d(LOG_TAG, "no listener for list item");
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
        public RelativeLayout buttonLayout;
        public int icon;
        public String title;
        OnClickListener listener;
        DrawerLayout navDrawer;
        boolean isStatic = false;
        View staticView;
        boolean closeDrawerOnClick = false;
        boolean isInboxRow = false;
        boolean isMapRow = false;
        boolean isActive = false;

        public RelativeLayout rowLayout;

        public RelativeLayout inboxCounterWrapper;
        public TextView inboxCounter;
        public TextView text;
        public boolean enable;

        /**
         * std. constr.
         */
        public LKRightMenuListItem(){

        }


        /**
         * Creates list item with no click listener.
         * @param title
         * @param icon
         */
        public LKRightMenuListItem(String title, int icon){
            this.title = title;
            this.icon = icon;
        }

        public LKRightMenuListItem isActive(boolean isActive){
            this.isActive = isActive;
            return this;
        }


        /**
         * To be used with statics in listview.
         * @param isStatic
         * @return
         */
        public LKRightMenuListItem isStatic(boolean isStatic){
            this.isStatic = isStatic;
            return this;
        }

        /**
         * If isStatic is true, this view will be shown.
         * @param view
         * @return
         */
        public LKRightMenuListItem showView(View view){
            this.staticView = view;
            return this;
        }

        /**
         * Only to use with inbox fragment
         * @param isInboxRow sets to show the inbox counter.
         */
        public LKRightMenuListItem isInboxRow(boolean isInboxRow){
            this.isInboxRow = isInboxRow;
            return this;
        }

        /**
         * Only to use with map fragment
         * @param isMapRow sets to show the map !.
         */
        public LKRightMenuListItem isMapRow(boolean isMapRow){
            this.isMapRow = isMapRow;
            return this;
        }

        /**
         * Creates list item with custom click listener that is called when list item is clicked.
         * @param title Text in menu to show
         * @param icon Icon next to text
         * @param listener Listener to use to handle click events.
         */
        public LKRightMenuListItem(String title, int icon, OnClickListener listener, boolean enabled){
            this.title = title;
            this.icon = icon;
            this.listener = listener;
            this.enable = enabled;
        }

        /**
         * Creates list item with click listener that starts a fragment.
         * @param title Text in menu to show
         * @param icon Icon next to text
         * @param fragment Fragment to show
         */
        public LKRightMenuListItem(final String title, int icon, final Fragment fragment, final FragmentManager fragmentMgr, final Context context, boolean enabled){
            this.title = title;
            this.icon = icon;
            this.enable = enabled;




            this.listener = new OnClickListener() {

                @Override
                public void onClick(View v) {

                    clearBackStack(fragmentMgr);

                    fragmentMgr.beginTransaction().replace(R.id.content_frame, fragment).commit();
                }

                private void clearBackStack(FragmentManager fragmentMgr) {

                    for(int i = 0; i < fragmentMgr.getBackStackEntryCount(); i++) {
                        Log.d("ContentActivity", "Removed from backstack");
                        fragmentMgr.popBackStack();
                    }
                }
            };
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

    }
}