package se.lundakarnevalen.extern.widget;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import se.lundakarnevalen.extern.android.R;
import se.lundakarnevalen.extern.util.SchemeAlarm;

public class LKSchemeAdapter extends ArrayAdapter<LKSchemeAdapter.LKSchemeItem> implements OnItemClickListener {
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
    private PendingIntent mAlarmSender;
    private LayoutInflater inflater;
    private Context context;

    public LKSchemeAdapter(Context context, List<LKSchemeItem> items){
        super(context, android.R.layout.simple_list_item_1, items);
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
    }

    private static class ViewHolder{
        private final ImageView image;
        private final ImageView heart;
        private final TextView start;
        private final TextView end;
        private final TextView place;
        private final TextView name;

        private ViewHolder(View root) {
            start = (TextView) root.findViewById(R.id.time1);
            end = (TextView) root.findViewById(R.id.time2);
            place = (TextView) root.findViewById(R.id.place);
            name = (TextView) root.findViewById(R.id.name);
            image = (ImageView) root.findViewById(R.id.scheme_element_image);
            heart = (ImageView) root.findViewById(R.id.heart_image);
        }
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent){
        final LKSchemeItem item = getItem(pos);

        if(item.dot) {
            int id = (pos == 0) ? R.layout.scheme_element_top : R.layout.scheme_element_bottom;
            return inflater.inflate(id, parent, false);
        }

        ViewHolder vh;
        if(convertView == null || convertView.getTag() == null) {
            convertView = inflater.inflate(R.layout.scheme_element, parent, false);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        int heartIcon = item.reminder ? R.drawable.heart_clicked : R.drawable.heart_not_clicked;

        vh.image.setImageResource(item.icon);
        vh.heart.setImageResource(heartIcon);
        vh.start.setText(item.getStartTime());
        vh.end.setText(item.getEndTime());
        vh.place.setText(item.place);
        vh.name.setText(item.name);
        return convertView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
        final LKSchemeItem item = getItem(pos);
        final ViewHolder vh = (ViewHolder) view.getTag();
        if(item.reminder) {
            item.reminder = false;
            final Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.heart_out);
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}

                @Override
                public void onAnimationEnd(Animation animation) {
                    vh.heart.setImageResource(R.drawable.heart_not_clicked);
                    vh.heart.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.heart_in));
                }

                @Override
                public void onAnimationRepeat(Animation animation) {}
            });
            vh.heart.startAnimation(anim);

            SharedPreferences sharedPref = getContext().getSharedPreferences("lundkarnevalen",Context.MODE_PRIVATE);
            String set = sharedPref.getString("notifications", "");
            String split[] = set.split(";");
            set = "";
            for(int i = 0;i<split.length;i++) {
                Log.d(split[i],item.getStartTime()+item.place+item.name);
                if(!split[i].equals(item.getStartTime() + item.place + item.name)) {
                    set+=split[i]+";";
                }
            }

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("notifications", set);
            editor.apply();
            //notificationManager.cancel(item.getStartTime()+item.place+item.name,0);

            Intent alarmIntent = new Intent(context, SchemeAlarm.class);

            alarmIntent.putExtra("Title", item.name);
            alarmIntent.putExtra("Desc", item.place+" "+item.getStartTime());
            mAlarmSender = PendingIntent.getBroadcast(context, item.id, alarmIntent, 0);
            AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            am.cancel(mAlarmSender);
        } else {
            item.reminder = true;
            final Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.heart_out);
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}

                @Override
                public void onAnimationEnd(Animation animation) {
                    vh.heart.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.heart_in));
                    vh.heart.setImageResource(R.drawable.heart_clicked);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {}
            });
            vh.heart.startAnimation(anim);


            Date d = new Date();
            if(item.startDate.after(d)) {

                if (d.before(new Date(item.startDate.getTime() - 1000 * 30 * 60))) {
                    context.getApplicationContext();
                    CharSequence text = context.getString(R.string.heart_selected);
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }

                Intent alarmIntent = new Intent(context, SchemeAlarm.class);
                alarmIntent.putExtra("Title", item.name);
                alarmIntent.putExtra("Desc", item.place + " " + item.getStartTime());
                mAlarmSender = PendingIntent.getBroadcast(context, item.id, alarmIntent, 0);
                startAlarm(item.startDate);
            }

            SharedPreferences sharedPref = getContext().getSharedPreferences("lundkarnevalen",Context.MODE_PRIVATE);
            String set = sharedPref.getString("notifications", "");
            set+=";"+item.getStartTime()+item.place+item.name;
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("notifications", set);
            editor.apply();
        }
    }

    public static class LKSchemeItem {
        public int icon;
        public String name;
        public String place;
        public Date startDate;
        public Date endDate;
        boolean reminder = false;
        boolean dot = false;
        public int id;

        public LKSchemeItem() {
            this.dot = true;
        }

        /**
         * Creates list item..
         * @param icon Icon next to text
         */
        public LKSchemeItem(String place, String name, int icon, Date startDate, Date endDate, HashSet<String> activated, int id) {
            this.place = place;
            this.name = name;
            this.icon = icon;
            this.startDate = startDate;
            this.endDate = endDate;
            this.id = id;

            if(activated.contains(getStartTime() + place + name)) {
                reminder = true;
            } else {
                reminder = false;
            }
        }

        public String getStartTime() {return dateFormat.format(startDate);}
        public String getEndTime() {
            return dateFormat.format(endDate);
        }
    }

    public void startAlarm(Date d){
        //Set the alarm to 10 seconds from now
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.add(Calendar.MINUTE, -30);
        long firstTime = c.getTimeInMillis();
        // Schedule the alarm!
        AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, firstTime, mAlarmSender);

    }
}