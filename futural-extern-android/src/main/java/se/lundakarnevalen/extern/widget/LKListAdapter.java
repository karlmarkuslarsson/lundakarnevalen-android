package se.lundakarnevalen.extern.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import se.lundakarnevalen.extern.android.R;

/**
 * Created by Markus on 2014-04-20.
 */
public class LKListAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] items;

        public LKListAdapter(Context context, String[] items) {
            super(context, R.layout.element_lk_list, items);
            this.context = context;
            this.items = items;
         }


        @Override
        public View getView(int pos, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.element_lk_list, parent, false);
            TextView textView = (TextView) rowView.findViewById(R.id.text1);
            TextView textView2 = (TextView) rowView.findViewById(R.id.text2);
            ImageView imageView = (ImageView) rowView.findViewById(R.id.image);
            textView.setText(items[pos]);
            // Change the icon for Windows and iPhone
            String s = items[pos];

            return rowView;

        }
    }

