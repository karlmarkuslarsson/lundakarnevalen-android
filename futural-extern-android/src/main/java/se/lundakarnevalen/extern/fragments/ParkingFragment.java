package se.lundakarnevalen.extern.fragments;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import se.lundakarnevalen.extern.activities.ContentActivity;
import se.lundakarnevalen.extern.android.R;

public class ParkingFragment extends LKFragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_parking, container, false);
        startMovingClouds(rootView);
        ContentActivity.class.cast(getActivity()).allBottomsUnfocus();
        return rootView;
    }

    private class CloudStartListener implements Animation.AnimationListener {

        private ImageView view;
        private ImageView movingCloud;
        private Animation a2;

        public CloudStartListener(ImageView view, ImageView movingCloud, Animation a2) {
            this.view = view;
            this.movingCloud = movingCloud;
            this.a2 = a2;
        }

        @Override
        public void onAnimationStart(Animation animation) {}

        @Override
        public void onAnimationEnd(Animation animation) {
            view.setVisibility(View.INVISIBLE);
            if(a2!=null)  {

                movingCloud.startAnimation(a2);
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {}
    }

    private void startMovingClouds(View rootView) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;

        moveCloud(R.id.cloud1, R.id.cloud5, width + 200,    width, 10000,   10000, rootView);
        moveCloud(R.id.cloud2, R.id.cloud6, 350,            width, 4000,    13000, rootView);
        moveCloud(R.id.cloud3, R.id.cloud7, width + 100,     width, 13000,   15000, rootView);
        moveCloud(R.id.cloud4, R.id.cloud8, 400,            width, 4000,    8000, rootView);
        moveCloud(R.id.cloud9, R.id.cloud10, width,         width, 8000,    17000, rootView);
    }

    private void moveCloud(int cloud1, int cloud2, int dx1, int width, int dur1, int dur2, View rootView) {

        ImageView cloud = (ImageView) rootView.findViewById(cloud1);
        ImageView movingCloud = (ImageView) rootView.findViewById(cloud2);

        Animation a = new TranslateAnimation(0, dx1, 0, 0);

        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) movingCloud.getLayoutParams();

        Animation a2 = new TranslateAnimation(0, width + (-lp.leftMargin),0 ,0);
        a2.setInterpolator(new AccelerateDecelerateInterpolator());
        a2.setInterpolator(new LinearInterpolator());
        a2.setRepeatCount(Animation.INFINITE);
        a2.setDuration(dur2);

        a.setAnimationListener(new CloudStartListener(cloud, movingCloud, a2));
        a.setDuration(dur1);
        a.setInterpolator(new AccelerateDecelerateInterpolator());
        a.setInterpolator(new LinearInterpolator());
        cloud.startAnimation(a);
    }
}









