package se.lundakarnevalen.extern.fragments;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;

import se.lundakarnevalen.extern.activities.ContentActivity;
import se.lundakarnevalen.extern.android.R;
import se.lundakarnevalen.extern.scheme.Event;
import se.lundakarnevalen.extern.scheme.Events;
import se.lundakarnevalen.extern.widget.BounceListView;
import se.lundakarnevalen.extern.widget.LKSchemeAdapter;


@SuppressWarnings("ResourceType")
public class SchemeFragment extends LKFragment {

    private ViewPager vp;
    private final int ID = 3;
    private ArrayList<Event> fridayEvents;
    private ArrayList<Event> saturdayEvents;

    private int currentDay;
    private ArrayList<Event> sundayEvents;
    private RelativeLayout leftArrowLayout;
    private RelativeLayout rightArrowLayout;
    boolean myScheme;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myScheme = false;
        ContentActivity activity = ContentActivity.class.cast(getActivity());

        View view = inflater.inflate(R.layout.fragment_scheme, container, false);
        vp = get(view, R.id.scheme_viewpager, ViewPager.class);

        final RelativeLayout rl = get(view,R.id.heartText,RelativeLayout.class);
        rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentDay = vp.getCurrentItem();
                if(myScheme){
                    get(rl,R.id.myScheme,TextView.class).setText(R.string.my_scheme);
                    get(rl,R.id.myScheme,TextView.class).setTextColor(getResources().getColor(R.color.red));
                    get(rl,R.id.heartText,RelativeLayout.class).setBackgroundColor(getResources().getColor(R.color.white));
                    get(rl,R.id.myHeart,ImageView.class).setImageResource(R.drawable.heart_clicked);
                    vp.setAdapter(new SchemeViewAdapter());
                    vp.setCurrentItem(currentDay);
                    myScheme = false;
                } else {
                    get(rl,R.id.myScheme,TextView.class).setText(R.string.full_list);
                    get(rl,R.id.myScheme,TextView.class).setTextColor(getResources().getColor(R.color.white));
                    get(rl,R.id.heartText,RelativeLayout.class).setBackgroundColor(getResources().getColor(R.color.red));
                    get(rl,R.id.myHeart,ImageView.class).setImageResource(R.drawable.heart_not_clicked);
                    vp.setAdapter(new MySchemeViewAdapter());
                    vp.setCurrentItem(currentDay);
                    myScheme = true;
                }
           }
        });

        activity.allBottomsUnfocus();
        activity.focusBottomItem(ID);
        ContentActivity.class.cast(getActivity()).inactivateTrainButton();
        leftArrowLayout = get(view, R.id.left_arrow, RelativeLayout.class);
        rightArrowLayout = get(view, R.id.right_arrow, RelativeLayout.class);
        final TextView header = get(view, R.id.dayText, TextView.class);

        currentDay = getCurrentDay();
        vp.setBackgroundColor(Color.TRANSPARENT);
        vp.setAdapter(new SchemeViewAdapter());
        vp.setCurrentItem(currentDay);
        vp.setPageTransformer(true, new ZoomOutPageTransformer());

        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setDayText(header, position);
                updateLeftArrow(leftArrowLayout, vp);
                updateRightArrow(rightArrowLayout, vp);
            }

            @Override
            public void onPageScrollStateChanged(int state) {}

        });

        leftArrowLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateLeftArrow(view, vp);
                currentDay = vp.getCurrentItem()-1;
                vp.setCurrentItem(vp.getCurrentItem()-1, true);
            }
        });

        rightArrowLayout.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                updateRightArrow(view, vp);
                currentDay = vp.getCurrentItem()+1;
                vp.setCurrentItem(vp.getCurrentItem()+1, true);
            }
        });

        setDayText(header, currentDay);
        updateRightArrow(rightArrowLayout, vp);
        updateLeftArrow(leftArrowLayout, vp);

        return view;
    }

    private void updateLeftArrow(View layout, ViewPager vp) {
        ImageView arrowPlaceholder = get(layout, R.id.left_arrow_text, ImageView.class);
        int currentItem = vp.getCurrentItem();
        if(currentItem - 1 < 0){
            arrowPlaceholder.setEnabled(false);
            leftArrowLayout.setVisibility(View.INVISIBLE);
        } else {
            leftArrowLayout.setVisibility(View.VISIBLE);
            arrowPlaceholder.setEnabled(true);
        }
    }

    private void updateRightArrow(View layout, ViewPager vp) {
        ImageView arrowPlaceholder = get(layout, R.id.right_arrow_text, ImageView.class);
        int count = vp.getAdapter().getCount();
        int currentItem = vp.getCurrentItem();
        if(currentItem + 1 == count){
            arrowPlaceholder.setEnabled(false);
            rightArrowLayout.setVisibility(View.INVISIBLE);
        } else {
            rightArrowLayout.setVisibility(View.VISIBLE);
            arrowPlaceholder.setEnabled(true);
        }
    }

    public void setDayText(TextView header, int day) {
        switch(day)
        {
            case 0:
                header.setText(R.string.friday); break;
            case 1:
                header.setText(R.string.saturday); break;
            case 2:
                header.setText(R.string.sunday); break;
        }
        header.setTag(day);
    }

    /**
     * Sets up the ListView in the navigationdrawer menu.
     */
    private ArrayList<LKSchemeAdapter.LKSchemeItem> getSchemeForDay(int day, boolean myScheme) {
        HashSet<String> activated = getActiveNotifications();
        ArrayList<LKSchemeAdapter.LKSchemeItem> listItems = new ArrayList<>();

        listItems.add(new LKSchemeAdapter.LKSchemeItem());

        if (day == 0) {
            if (fridayEvents == null) {
                fridayEvents = new ArrayList<>();
                Events.getFridayEvents(fridayEvents, getContext());
            }
            for (Event e : fridayEvents) {
                if(myScheme) {
                    LKSchemeAdapter.LKSchemeItem item = new LKSchemeAdapter.LKSchemeItem(e.place, e.title, e.image, e.startDate, e.endDate, activated, e.id);

                    if(activated.contains(item.getStartTime()+item.place+item.name)) {
                        listItems.add(item);

                    }
                } else {
                    LKSchemeAdapter.LKSchemeItem item = new LKSchemeAdapter.LKSchemeItem(e.place, e.title, e.image, e.startDate, e.endDate, activated, e.id);
                    listItems.add(item);
                }
            }

        } else if (day == 1) {
            if (saturdayEvents == null) {
                saturdayEvents = new ArrayList<>();
                Events.getSaturdayEvents(saturdayEvents, getContext());
            }
            for (Event e : saturdayEvents) {
                if(myScheme) {
                    LKSchemeAdapter.LKSchemeItem item = new LKSchemeAdapter.LKSchemeItem(e.place, e.title, e.image, e.startDate, e.endDate, activated, e.id);
                    if(activated.contains(item.getStartTime()+item.place+item.name)) {
                        listItems.add(item);

                    }
                } else {
                    LKSchemeAdapter.LKSchemeItem item = new LKSchemeAdapter.LKSchemeItem(e.place, e.title, e.image, e.startDate, e.endDate, activated, e.id);
                    listItems.add(item);
                }
            }
        } else {
            if (sundayEvents == null) {
                sundayEvents = new ArrayList<>();
                Events.getSundayEvents(sundayEvents, getContext());
            }
            for (Event e : sundayEvents) {
                if(myScheme) {
                    LKSchemeAdapter.LKSchemeItem item = new LKSchemeAdapter.LKSchemeItem(e.place, e.title, e.image, e.startDate, e.endDate, activated, e.id);
                    if(activated.contains(item.getStartTime()+item.place+item.name)) {
                        listItems.add(item);

                    }
                } else {
                    LKSchemeAdapter.LKSchemeItem item = new LKSchemeAdapter.LKSchemeItem(e.place, e.title, e.image, e.startDate, e.endDate, activated, e.id);
                    listItems.add(item);
                }
            }

        }
        listItems.add(new LKSchemeAdapter.LKSchemeItem());
        return listItems;
    }



    private HashSet<String> getActiveNotifications() {
        SharedPreferences sharedPref = getContext().getSharedPreferences("lundkarnevalen", Context.MODE_PRIVATE);
        String set = sharedPref.getString("notifications", "");
        String split[] = set.split(";");
        HashSet<String> activated = new HashSet<>();

        Collections.addAll(activated, split);
        return activated;
    }

    private int getCurrentDay() {
        Date d = new Date();
        switch(d.getDate()) {
            case 16:
                return 0;
            case 17:
                return 1;
            case 18:
                return 2;
            default:
                return 0;
        }

    }

    private class SchemeViewAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            LayoutInflater inflater = LayoutInflater.from(container.getContext());

            View view = inflater.inflate(R.layout.scheme_list, container, false);
            container.addView(view);

            BounceListView lv = get(view, R.id.scheme_list, BounceListView.class);
            lv.setCacheColorHint(0); //For keeping the background (not black) while scrolling on API 10
            LKSchemeAdapter schemeAdapter = new LKSchemeAdapter(container.getContext(), getSchemeForDay(position,false));
            lv.setAdapter(schemeAdapter);
            lv.setOnItemClickListener(schemeAdapter);
            view.setTag(position);
            return position;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(container.findViewWithTag(object));
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.getTag() == object;
        }
    }


    private class MySchemeViewAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            LayoutInflater inflater = LayoutInflater.from(container.getContext());

            View view = inflater.inflate(R.layout.scheme_list, container, false);
            container.addView(view);

            BounceListView lv = get(view, R.id.scheme_list, BounceListView.class);
            lv.setCacheColorHint(0); //For keeping the background (not black) while scrolling on API 10
            LKSchemeAdapter schemeAdapter = new LKSchemeAdapter(container.getContext(), getSchemeForDay(position,true));
            lv.setAdapter(schemeAdapter);
            lv.setOnItemClickListener(schemeAdapter);
            view.setTag(position);
            return position;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(container.findViewWithTag(object));
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.getTag() == object;
        }
    }


    /**
     * Fancy animation needs android 11+
     */
    @SuppressLint("NewApi")
    private static class ZoomOutPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.85f;
        private static final float MIN_ALPHA = 0.5f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();

            if (position < -1) {
                view.setAlpha(0);

            } else if (position <= 1) {
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }

                // Scale the page down (between MIN_SCALE and 1)
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

                // Fade the page relative to its size.
                view.setAlpha(MIN_ALPHA +
                        (scaleFactor - MIN_SCALE) /
                                (1 - MIN_SCALE) * (1 - MIN_ALPHA));

            } else {
                view.setAlpha(0);
            }
        }
    }
}
