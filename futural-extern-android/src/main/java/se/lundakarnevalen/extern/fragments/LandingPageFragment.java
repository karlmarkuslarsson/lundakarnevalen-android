package se.lundakarnevalen.extern.fragments;

import static se.lundakarnevalen.extern.util.ViewUtil.*;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import se.lundakarnevalen.extern.android.ContentActivity;
import se.lundakarnevalen.extern.android.R;
import se.lundakarnevalen.extern.data.DataElement;
import se.lundakarnevalen.extern.data.DataType;
import se.lundakarnevalen.extern.widget.LKRightMenuArrayAdapter;
import se.lundakarnevalen.extern.widget.LKSchemeAdapter;
import se.lundakarnevalen.extern.widget.SVGView;

/**
 * Created by Markus on 2014-04-16.
 */
public class LandingPageFragment extends LKFragment{

    private LKSchemeAdapter adapter;
    private ListView list;
    private float lat;
    private float lng;

    public LandingPageFragment() {}

    // Every time you switch to this fragment.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_landing_page, null);

        Bundle bundle = getArguments();

        final DataElement element = bundle.getParcelable("element");
        get(rootView,R.id.name,TextView.class).setText(element.title);
        get(rootView,R.id.place,TextView.class).setText(element.place);

        Calendar c = Calendar.getInstance();

        switch (c.DAY_OF_MONTH) {
            case 23:
                get(rootView,R.id.open_info,TextView.class).setText(element.timeFriday);
                //open(c.)
                //if(c.)
                break;
            case 24:
                if(c.HOUR_OF_DAY < 6) {
                    get(rootView,R.id.open_info,TextView.class).setText(element.timeFriday);
                }
                get(rootView,R.id.open_info,TextView.class).setText(element.timeSaturday);
                break;
            case 25:
                if(c.HOUR_OF_DAY < 6) {
                    get(rootView,R.id.open_info,TextView.class).setText(element.timeSaturday);
                }
                get(rootView,R.id.open_info,TextView.class).setText(element.timeSunday);
                break;
            default:
                if(c.DAY_OF_MONTH > 25) {
                    get(rootView, R.id.open_info, TextView.class).setText(element.timeSunday);
                } else {
                    get(rootView,R.id.open_info,TextView.class).setText(element.timeFriday);
                }
                    break;
        }

        if(element.cash == 1) {
           // get(rootView,R.id.cash_picture,ImageView.class).setImageResource(R.drawable.cash_true);
        } else {
            // get(rootView,R.id.cash_picture,ImageView.class).setImageResource(R.drawable.cash_false);
        }
        if(element.card==1) {
            // get(rootView,R.id.card_picture,ImageView.class).setImageResource(R.drawable.card_true);
        } else {
            // get(rootView,R.id.card_picture,ImageView.class).setImageResource(R.drawable.card_false);
        }
        lat = element.lat;
        lng = element.lng;
        DataType type = element.type;

        get(rootView,R.id.picture,ImageView.class).setImageResource(element.picture_list);
        get(rootView,R.id.header_background,ImageView.class).setImageResource(element.headerPicture);

        ImageView mapView = (ImageView) rootView.findViewById(R.id.map_picture);
        mapView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Set<DataType> multiContainers = new HashSet<DataType>();
                multiContainers.add(DataType.TENT_FUN);
                multiContainers.add(DataType.TOMBOLAN);
                multiContainers.add(DataType.MUSIC);
                multiContainers.add(DataType.FOODSTOCK);
                multiContainers.add(DataType.SNACKS);
                multiContainers.add(DataType.TOILETS);
                multiContainers.add(DataType.SECURITY);
                multiContainers.add(DataType.CARE);
                multiContainers.add(DataType.TRASHCAN);
                multiContainers.add(DataType.ENTRANCE);

                if(element.type == DataType.TRAIN) {
                    ContentActivity.class.cast(getActivity()).loadFragmentAddingBS(TrainMapFragment.create());
                } else if (multiContainers.contains(element.type)) {
                    ContentActivity.class.cast(getActivity()).showMapAndPanTo(lat, lng, SVGView.HALF_ZOOM);
                    LKRightMenuArrayAdapter adapter = ((LKRightMenuArrayAdapter)ContentActivity.class.cast(getActivity()).mRightMenuList.getAdapter());
                    adapter.deselectEverything();
                    //adapter.notifyDataSetChanged();
                    int index = -1;
                    switch (element.type) {
                        case TENT_FUN:
                            index = adapter.getIndexForIcon(R.drawable.tent_logo);
                            break;
                        case TOMBOLAN:
                            index = adapter.getIndexForIcon(R.drawable.tombola_logo);
                            break;
                        case MUSIC:
                            index = adapter.getIndexForIcon(R.drawable.musik_logo);
                            break;
                        case FOODSTOCK: case SNACKS:
                            index = adapter.getIndexForIcon(R.drawable.food_logo);
                            break;
                        case TOILETS:
                            index = adapter.getIndexForIcon(R.drawable.wc_logo);
                            break;
                        case SECURITY: case CARE:
                            index = adapter.getIndexForIcon(R.drawable.help_logo);
                            break;
                        case TRASHCAN:
                            index = adapter.getIndexForIcon(R.drawable.soptunna_filter_icon);
                            break;
                        case ENTRANCE:
                            index = adapter.getIndexForIcon(R.drawable.entrance_filter_icon);
                            break;
                    }


                    ContentActivity.class.cast(getActivity()).mRightMenuList.performItemClick(null,index,0);
                    //LKRightMenuArrayAdapter.LKRightMenuListItem item = (LKRightMenuArrayAdapter.LKRightMenuListItem) ContentActivity.class.cast(getActivity()).mRightMenuList.getAdapter().getItem(index);
                    //item.selected = true;
                }

                else {
                    ContentActivity.class.cast(getActivity()).showMapAndPanTo(lat, lng, SVGView.MAX_ZOOM);
                }
            }
        });


        if(type== DataType.FUN) {
            get(rootView, R.id.question, TextView.class).setText(element.question);
            get(rootView, R.id.text, TextView.class).setText(Html.fromHtml(getString(element.info)));

        } else if(type == DataType.FOOD || type == DataType.FOODSTOCK) {
            get(rootView, R.id.question, TextView.class).setText(element.question);
            get(rootView, R.id.text, TextView.class).setText(element.info);
            get(rootView, R.id.middleLayout, RelativeLayout.class).setBackgroundResource(R.color.green_background);

            if (element.menu != null) {
                get(rootView,R.id.menu,RelativeLayout.class).setVisibility(View.VISIBLE);
                LinearLayout ll = (LinearLayout) rootView.findViewById(R.id.menu_food_list);

                //LayoutInflater inflater2 = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                //View layout = inflater.inflate(R.layout.menu_food_element, parent, false);

                for(int i = 0;i< element.menu.size();i++){
                    View view = inflater.inflate(R.layout.menu_food_element, ll, false);
                    ((TextView)view.findViewById(R.id.name)).setText((i+1)+". "+getString(element.menu.get(i)));
                    ((TextView)view.findViewById(R.id.price)).setText(element.menuPrice.get(i));
                    ll.addView(view);
                }
            }
        } else if(type == DataType.BILJETTERIET || type == DataType.SHOPPEN|| type == DataType.TRASHCAN|| type == DataType.ENTRANCE) {
            get(rootView, R.id.question, TextView.class).setText(element.question);
            get(rootView, R.id.text, TextView.class).setText(Html.fromHtml(getString(element.info)));
            get(rootView, R.id.middleLayout, RelativeLayout.class).setBackgroundResource(R.color.blue_dark);
            get(rootView, R.id.middleView, View.class).setVisibility(View.INVISIBLE);

        } else if(type == DataType.TENT_FUN || type == DataType.SMALL_FUN || type == DataType.TOMBOLAN ||type == DataType.MUSIC ||type == DataType.SCENE) {
            get(rootView, R.id.question, TextView.class).setText(element.question);
            get(rootView, R.id.text, TextView.class).setText(element.info);
            get(rootView, R.id.middleView, View.class).setVisibility(View.INVISIBLE);

        } else if(type == DataType.SNACKS) {
            get(rootView, R.id.question, TextView.class).setVisibility(View.GONE);
            get(rootView, R.id.text, TextView.class).setText(element.info);
            get(rootView, R.id.middleLayout, RelativeLayout.class).setBackgroundResource(R.color.green_background);
            get(rootView, R.id.middleView, View.class).setVisibility(View.INVISIBLE);
        }else if(type == DataType.TOILETS || type == DataType.SECURITY || type == DataType.CARE) {
            get(rootView, R.id.question, TextView.class).setVisibility(View.GONE);
            get(rootView, R.id.text, TextView.class).setText(element.info);
            get(rootView, R.id.middleLayout, RelativeLayout.class).setBackgroundResource(R.color.blue_dark);
            get(rootView, R.id.middleView, View.class).setVisibility(View.INVISIBLE);
        }else if(type == DataType.TRAIN) {
            get(rootView, R.id.question, TextView.class).setText(element.question);
            get(rootView, R.id.text, TextView.class).setText(element.info);
            get(rootView, R.id.middleLayout, RelativeLayout.class).setBackgroundResource(R.color.blue_dark);
            get(rootView,R.id.card_box,RelativeLayout.class).setVisibility(View.INVISIBLE);
            get(rootView,R.id.cash_box,RelativeLayout.class).setVisibility(View.INVISIBLE);
            get(rootView,R.id.map_info,TextView.class).setText(R.string.to_traint);
        }


        return rootView;
    }


    @Override
    public void onStart() {
        super.onStart();
        //ContentActivity.class.cast(getActivity()).hideBottomMenu();
        ContentActivity.class.cast(getActivity()).allBottomsUnfocus();
    }

    @Override
    public void onStop() {
        //ContentActivity.class.cast(getActivity()).showBottomMenu();
        super.onStop();
    }

    public static LandingPageFragment create(DataElement element) {
        LandingPageFragment fragment = new LandingPageFragment();
        Bundle bundle = new Bundle();
    /*
        bundle.putString("name",name);
        bundle.putString("place",place);
        bundle.putBoolean("cash", cash);
        bundle.putBoolean("card", card);
        bundle.putFloat("lat", lat);
        bundle.putFloat("lng", lng);
        bundle.putInt("picture", picture);
        bundle.putInt("top_picture", topPicture);
        bundle.putString("question", question);
        bundle.putString("desc", desc);
        bundle.putInt("type", type);
        bundle.putInt("open", open);
        bundle.putInt("close",close);
    */
        bundle.putParcelable("element",element);

        fragment.setArguments(bundle);
        // Add arguments
        return fragment;
    }


}


