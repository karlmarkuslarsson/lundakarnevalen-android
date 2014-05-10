package se.lundakarnevalen.extern.fragments;

import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import se.lundakarnevalen.extern.android.ContentActivity;
import se.lundakarnevalen.extern.android.R;

/**
 * Created by Markus on 2014-05-07.
 */
public class ParkingFragment extends LKFragment{


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_parking, null);
        startMovingClouds(rootView);
        ContentActivity.class.cast(getActivity()).allBottomsUnfocus();


        return rootView;
    }


    @Override
    public void onDestroyView () {
        super.onDestroyView();
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private class CloudStartListner implements Animation.AnimationListener {

        ImageView view;
        ImageView movingCloud;
        Animation a2;
        CloudStartListner(ImageView view) {
            this.view = view;
        }

        public CloudStartListner(ImageView view, ImageView movingCloud, Animation a2) {
            this.view = view;
            this.movingCloud = movingCloud;
            this.a2 = a2;
        }

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            view.setVisibility(View.INVISIBLE);
            if(a2!=null)  {

                movingCloud.startAnimation(a2);
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    private void startMovingClouds(View rootView) {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        int width = display.getWidth();  // deprecated
        int height = display.getHeight();
        ImageView cloud = (ImageView) rootView.findViewById(R.id.cloud1);
        Animation a = new TranslateAnimation(0,width+150,0 ,0);
        ImageView movingCloud = (ImageView) rootView.findViewById(R.id.cloud5);

        RelativeLayout.LayoutParams lp =
                (RelativeLayout.LayoutParams) movingCloud.getLayoutParams();
        Animation a2 = new TranslateAnimation(0,width+(-lp.leftMargin),0 ,0);
        a2.setInterpolator(new AccelerateDecelerateInterpolator());
        a2.setInterpolator(new LinearInterpolator());

        a2.setRepeatCount(Animation.INFINITE);
        a2.setDuration(10000);
        a.setAnimationListener(new CloudStartListner(cloud, movingCloud, a2));
        a.setDuration(10000);
        a.setInterpolator(new AccelerateDecelerateInterpolator());
        a.setInterpolator(new LinearInterpolator());
        cloud.startAnimation(a);
        cloud = (ImageView) rootView.findViewById(R.id.cloud2);
        //ObjectAnimator.
        //animX2.setDuration(3000);
        a  = new TranslateAnimation(0,230,0,0);
        a.setInterpolator(new AccelerateDecelerateInterpolator());
        a.setInterpolator(new LinearInterpolator());

        a.setDuration(4000);
        movingCloud = (ImageView) rootView.findViewById(R.id.cloud6);
        lp =
                (RelativeLayout.LayoutParams) movingCloud.getLayoutParams();
        a2 = new TranslateAnimation(0,width+(-lp.leftMargin),0 ,0);
        a2.setInterpolator(new AccelerateDecelerateInterpolator());
        a2.setInterpolator(new LinearInterpolator());

        a2.setRepeatCount(Animation.INFINITE);
        a2.setDuration(13000);
        a.setAnimationListener(new CloudStartListner(cloud, movingCloud, a2));
        cloud.startAnimation(a);


        //animX2.setRepeatCount(Animation.INFINITE);
        //animX2.start();

        cloud = (ImageView) rootView.findViewById(R.id.cloud3);
        a = new TranslateAnimation(0,width+30,0 ,0);
        a.setInterpolator(new AccelerateDecelerateInterpolator());
        a.setInterpolator(new LinearInterpolator());

        a.setDuration(13000);
        movingCloud = (ImageView) rootView.findViewById(R.id.cloud7);

        lp =
                (RelativeLayout.LayoutParams) movingCloud.getLayoutParams();
        a2 = new TranslateAnimation(0,width+(-lp.leftMargin),0 ,0);
        a2.setInterpolator(new AccelerateDecelerateInterpolator());
        a2.setInterpolator(new LinearInterpolator());

        a2.setRepeatCount(Animation.INFINITE);
        a2.setDuration(15000);
        a.setAnimationListener(new CloudStartListner(cloud, movingCloud, a2));
        cloud.startAnimation(a);

        cloud = (ImageView) rootView.findViewById(R.id.cloud4);
        a  = new TranslateAnimation(0,250+55,0,0);
        a.setInterpolator(new AccelerateDecelerateInterpolator());
        a.setInterpolator(new LinearInterpolator());

        a.setDuration(4000);
        movingCloud = (ImageView) rootView.findViewById(R.id.cloud8);
        lp =
                (RelativeLayout.LayoutParams) movingCloud.getLayoutParams();
        a2 = new TranslateAnimation(0,width+(-lp.leftMargin),0 ,0);
        a2.setInterpolator(new AccelerateDecelerateInterpolator());
        a2.setInterpolator(new LinearInterpolator());

        a2.setRepeatCount(Animation.INFINITE);
        a2.setDuration(8000);
        a.setAnimationListener(new CloudStartListner(cloud, movingCloud, a2));
        cloud.startAnimation(a);

        cloud = (ImageView) rootView.findViewById(R.id.cloud9);
        //ObjectAnimator.
        //animX2.setDuration(3000);
        a  = new TranslateAnimation(0,width,0,0);
        a.setInterpolator(new AccelerateDecelerateInterpolator());
        a.setInterpolator(new LinearInterpolator());

        a.setDuration(16000);
        a.setAnimationListener(new CloudStartListner(cloud, movingCloud, null));
        cloud.startAnimation(a);


    }


}









