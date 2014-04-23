package se.lundakarnevalen.extern.widget;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
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

import se.lundakarnevalen.extern.android.ContentActivity;
import se.lundakarnevalen.extern.android.R;

public class LKSchemeAdapter extends ArrayAdapter<LKSchemeAdapter.LKSchemeItem> implements OnItemClickListener {
    private LayoutInflater inflater;
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");

    public LKSchemeAdapter(Context context, List<LKSchemeItem> items){
        super(context, android.R.layout.simple_list_item_1, items);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent){
        final LKSchemeItem item = getItem(pos);

        RelativeLayout wrapper = (RelativeLayout) inflater.inflate(R.layout.scheme_element, null);

        ImageView image = (ImageView) wrapper.findViewById(R.id.bottom_menu_image);
        image.setImageResource(item.icon);

        TextView start = (TextView) wrapper.findViewById(R.id.time1);
        TextView end = (TextView) wrapper.findViewById(R.id.time2);
        start.setText(item.getStartTime());
        end.setText(item.getEndTime());

        TextView place = (TextView) wrapper.findViewById(R.id.place);
        TextView name = (TextView) wrapper.findViewById(R.id.name);

        place.setText(item.place);
        name.setText(item.name);

        ImageView heart = (ImageView) wrapper.findViewById(R.id.heart_image);
        if(item.reminder) {
            heart.setImageResource(R.drawable.heart_clicked);
        } else {
            heart.setImageResource(R.drawable.heart_not_clicked);
        }
        heart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(item.reminder) {
                    item.reminder = false;
                    ((ImageView)view).setImageResource(R.drawable.heart_not_clicked);

                    NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);

                    SharedPreferences sharedPref = getContext().getSharedPreferences("lundkarnevalen",Context.MODE_PRIVATE);
                    String set = sharedPref.getString("notifications", "");
                    String split[] = set.split(";");
                    set = "";
                    for(int i = 0;i<split.length;i++) {
                        Log.d(split[i],item.getStartTime()+item.place+item.name);
                        if(!split[i].equals(item.getStartTime() + item.place + item.name)) {
                            set+=split[i]+";";
                        } else {
                        }
                    }

                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("notifications", set);
                    editor.apply();
                    notificationManager.cancel(item.getStartTime()+item.place+item.name,0);

                } else {
                    item.reminder = true;
                    ((ImageView)view).setImageResource(R.drawable.heart_clicked);

                    //Intent intent = new Intent(this, NotificationReceiverActivity.class);
                    //PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

                    // Build notification
                    // Actions are just fake

                    Intent intent = new Intent(getContext(), ContentActivity.class);
                    PendingIntent pIntent = PendingIntent.getActivity(getContext(), 0, intent, 0);


                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext());

                    builder = builder
                            .setContentIntent(pIntent)
                            .setContentTitle(item.name)
                            .setContentText(item.place+" "+item.getStartTime())
                            .setAutoCancel(true)
                            .setSmallIcon(R.drawable.test_spexet)
                            .setSound(Uri.parse("android.resource://"
                                    + getContext().getPackageName() + "/" + R.raw.futufutu));


                    builder.build();
                    // .setWhen(item.startDate.getTime()-1000*60*60)
                    // .setSound() add cool sound

                    Notification notification = builder.getNotification();
                    NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
                    // hide the notification after its selected
                    builder.getNotification().flags |= Notification.FLAG_AUTO_CANCEL;
                    notificationManager.notify(item.getStartTime()+item.place+item.name,0,notification);


                    SharedPreferences sharedPref = getContext().getSharedPreferences("lundkarnevalen",Context.MODE_PRIVATE);
                    String set = sharedPref.getString("notifications", "");
                    set+=";"+item.getStartTime()+item.place+item.name;
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("notifications", set);
                    editor.apply();
                }
            }
        });
        return wrapper;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
        final LKSchemeItem item = getItem(pos);
        OnClickListener listener = item.listener;
        listener.onClick(view);
        view.setSelected(true);
     }


    /**
     * Class representing a single row in the menu. Used with the LKMenuArrayAdapter.
     * @author alexander
     *
     */
    public static class LKSchemeItem {
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
        public LKSchemeItem(String place, String name, int icon, Date startDate, Date endDate, HashSet<String> activated) {

            this.place = place;
            this.name = name;
            this.icon = icon;
            this.startDate = startDate;
            this.endDate = endDate;
            if(activated.contains(getStartTime() + place + name)) {
                reminder = true;
            } else {
                reminder = false;
            }
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

        public String getStartTime() {return dateFormat.format(startDate);}
        public String getEndTime() {
            return dateFormat.format(endDate);
        }
        public void setOnClickListener(OnClickListener listener) {
            this.listener = listener;
        }
    }
}