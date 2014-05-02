package se.lundakarnevalen.extern.widget;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import se.lundakarnevalen.extern.android.R;
import static se.lundakarnevalen.extern.util.ViewUtil.get;


/**
 * Created by Markus on 2014-05-02.
 */
public class LKFoodMenuAdapter extends ArrayAdapter<LKFoodMenuElement> {
    private final Context context;
    private Activity activity;


    public LKFoodMenuAdapter(Context context, List<LKFoodMenuElement> items, Activity activity) {
        super(context, R.layout.menu_food_element, items);
        this.context = context;
        this.activity = activity;
    }


    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        LKFoodMenuElement item = getItem(pos);


        View layout = convertView;

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = inflater.inflate(R.layout.menu_food_element, parent, false);
        }



        TextView name = (TextView) layout.findViewById(R.id.name);
        TextView price = (TextView) layout.findViewById(R.id.price);
        Log.d("Name:", item.name);
        Log.d("Name2:", ""+name);
        name.setText(item.name);
        price.setText(item.price);
        return layout;

    }


}












