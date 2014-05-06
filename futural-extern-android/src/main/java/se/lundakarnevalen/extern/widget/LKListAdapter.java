package se.lundakarnevalen.extern.widget;


import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import se.lundakarnevalen.extern.android.ContentActivity;
import se.lundakarnevalen.extern.android.R;
import se.lundakarnevalen.extern.data.DataElement;
import se.lundakarnevalen.extern.fragments.LandingPageFragment;
import se.lundakarnevalen.extern.fragments.MelodyFragment;
import se.lundakarnevalen.extern.fragments.MusicFragment;
import se.lundakarnevalen.extern.fragments.TrainMapFragment;


/**
 * Created by Markus on 2014-04-20.
 */
public class LKListAdapter extends ArrayAdapter<LKListRow> {

    private final Context context;
    private Activity activity;


    public LKListAdapter(Context context, List<LKListRow> items, Activity activity) {
        super(context, R.layout.element_lk_list, items);
        this.context = context;
        this.activity = activity;
    }


    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        LKListRow item = getItem(pos);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.element_lk_list, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.text1);
        ImageView image1 = (ImageView) rowView.findViewById(R.id.image1);
        textView.setText(item.element1.title);
        image1.setImageResource(item.element1.picture_list);
        image1.setOnClickListener(new ItemListener(item.element1, activity));

        TextView textView2 = (TextView) rowView.findViewById(R.id.text2);

        if (item.element2 != null) {
            ImageView image2 = (ImageView) rowView.findViewById(R.id.image2);
            textView2.setText(item.element2.title);
            image2.setImageResource(item.element2.picture_list);
            image2.setOnClickListener(new ItemListener(item.element2, activity));
        } else {
            textView2.setText("");

        }
        return rowView;

    }


    private class ItemListener implements View.OnClickListener {
        DataElement element;
        Activity activity;

        public ItemListener(DataElement element, Activity activity) {
            this.element = element;
            this.activity = activity;
        }

        @Override
        public void onClick(View view) {
            if (Build.VERSION.SDK_INT >= 11) {
                view.setAlpha(0.5f);
            }
            ContentActivity contentActivity = ContentActivity.class.cast(activity);

            switch (element.type) {
                case PLAYER_RADIO:
                    contentActivity.loadFragmentAddingBS(new MusicFragment());
                    break;
                case PLAYER_FUTURAL:
                    contentActivity.loadFragmentAddingBS(new MelodyFragment());
                    break;
                case TRAIN:
                    contentActivity.loadFragmentAddingBS(TrainMapFragment.create());
                    break;
                case OTHER:
                     /*
                    contentActivity.loadFragmentAddingBS(
                                    MapFragment.create(
                                            "Filip", "Lindqvist",
                                            false, true, true,
                                            56.055876056f, 12.9737800f,
                                            R.drawable.monk, R.drawable.monk,
                                            "VAD ÄR FILIP BRA PÅ?", "ÄTA KEBAB!"));
                    */
                    break;
                default:
                    contentActivity.loadFragmentAddingBS(LandingPageFragment.create(element));
                    break;
            }
        }
    }
}
