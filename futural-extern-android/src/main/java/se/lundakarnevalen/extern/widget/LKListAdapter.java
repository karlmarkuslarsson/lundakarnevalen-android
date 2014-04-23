package se.lundakarnevalen.extern.widget;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import se.lundakarnevalen.extern.android.ContentActivity;
import se.lundakarnevalen.extern.android.R;
import se.lundakarnevalen.extern.fragments.LandingPageFragment;

/**
 * Created by Markus on 2014-04-20.
 */
public class LKListAdapter extends ArrayAdapter<LKListAdapter.LKListRow> {
    private final Context context;
    private Activity activity;

        public LKListAdapter(Context context, List<LKListRow> items, Activity activity) {
            super(context, R.layout.element_lk_list, items);
            this.context = context;
            this.activity = activity;
           }


        @Override
        public View getView(int pos, View convertView, ViewGroup parent) {
            final LKListRow item = getItem(pos);

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.element_lk_list, parent, false);
            TextView textView = (TextView) rowView.findViewById(R.id.text1);
            ImageView image1 = (ImageView) rowView.findViewById(R.id.image1);
            textView.setText(item.element1.title);
            image1.setImageResource(item.element1.picture);
            image1.setOnClickListener(new ItemListener(item.element1, activity));

            TextView textView2 = (TextView) rowView.findViewById(R.id.text2);

            if(item.element2 != null) {
                ImageView image2 = (ImageView) rowView.findViewById(R.id.image2);
                textView2.setText(item.element2.title);
                image2.setImageResource(item.element2.picture);
                image2.setOnClickListener(new ItemListener(item.element2, activity));
            } else {
                textView2.setText("");

            }
            return rowView;

        }

   public static class LKListRow {
        String name;
       String place;
       int header;
       int image;
       String name1;
       int image1;
       String name2;
       int image2;
       LKListElement element1;
       LKListElement element2;
       boolean isFun = false;


       public LKListRow(int image1, String name1,int image2, String name2) {
           this.image1 = image1;
           this.name1 = name1;
           this.image2 = image2;
           this.name2 = name2;
           this.place = place;
           this.header = header;
       }
       public LKListRow(LKListElement element1, LKListElement element2) {
            this.element1 = element1;
           this.element2 = element2;

       }

       public LKListRow(int image, String name, String place, int header) {
           this.image = image;
           this.name = name;
           this.place = place;
           this.header = header;
       }
   }

    private class ItemListener implements View.OnClickListener {
        LKListElement element;
        Activity activity;

        public ItemListener(LKListElement element, Activity activity) {
            this.element = element;
            this.activity = activity;
        }

        @Override
        public void onClick(View view) {
            ContentActivity.class
                    .cast(activity)
                    .loadFragmentWithAdd(
                            LandingPageFragment.create(
                                    element.title,
                                    element.place,
                                    true, true, true,
                                    element.lat,
                                    element.lng,
                                    element.picture,
                                    element.headerPicture,
                                    element.question,
                                    element.info)
                    );
        }

    }


}

