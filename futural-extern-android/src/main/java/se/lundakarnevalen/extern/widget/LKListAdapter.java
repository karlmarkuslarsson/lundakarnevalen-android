package se.lundakarnevalen.extern.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import se.lundakarnevalen.extern.android.R;

/**
 * Created by Markus on 2014-04-20.
 */
public class LKListAdapter extends ArrayAdapter<LKListAdapter.LKListElement> {
    private final Context context;

        public LKListAdapter(Context context, List<LKListElement> items) {
            super(context, R.layout.element_lk_list, items);
            this.context = context;
           }


        @Override
        public View getView(int pos, View convertView, ViewGroup parent) {
            final LKListElement item = getItem(pos);

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.element_lk_list, parent, false);
            TextView textView = (TextView) rowView.findViewById(R.id.text1);
            TextView textView2 = (TextView) rowView.findViewById(R.id.text2);
            RelativeLayout header = (RelativeLayout) rowView.findViewById(R.id.image_background);
            ImageView image = (ImageView) rowView.findViewById(R.id.image);

            textView.setText(item.name);
            textView2.setText(item.place);
            header.setBackgroundResource(item.header);
            image.setImageResource(item.image);
            // Change the icon for Windows and iPhone


            return rowView;

        }

   public static class LKListElement {
        String name;
       String place;
       int header;
       int image;


       public LKListElement(int image, String name, String place, int header) {
           this.image = image;
           this.name = name;
           this.place = place;
           this.header = header;
       }
   }
    }

