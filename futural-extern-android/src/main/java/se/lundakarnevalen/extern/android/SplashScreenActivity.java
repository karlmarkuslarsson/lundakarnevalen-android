package se.lundakarnevalen.extern.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import se.lundakarnevalen.extern.map.MapLoader;
import se.lundakarnevalen.extern.map.TrainMapLoader;

import static com.nineoldandroids.view.ViewPropertyAnimator.animate;

public class SplashScreenActivity extends Activity {
    private static final String LOG_TAG = SplashScreenActivity.class.getSimpleName();

    private static final int TOTAL_SPLASH_TIME_OUT = 2500;
    private static final int POST_ANIMATION_DELAY = 300;
    public static final int PRE_ANIMATION_DELAY = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashScreenActivity.this, ContentActivity.class);
                startActivity(i);
                if(Build.VERSION.SDK_INT >= 11) {
                    overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                }
                finish();
            }
        }, TOTAL_SPLASH_TIME_OUT);

        animate(find(R.id.imgLogo, ImageView.class))
            .rotationBy(360 * 3)
            .setStartDelay(PRE_ANIMATION_DELAY)
            .setDuration(TOTAL_SPLASH_TIME_OUT - POST_ANIMATION_DELAY)
            .setInterpolator(new AccelerateDecelerateInterpolator())
            .start();

        startMovingClouds(findViewById(android.R.id.content));
        MapLoader.startPreLoading(getApplicationContext());
    }


    private class CloudStartListner implements Animation.AnimationListener {

        private ImageView view;

        public CloudStartListner(ImageView view) {
            this.view = view;
        }

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {

        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    private void startMovingClouds(View rootView) {
        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth();  // deprecated
        int height = display.getHeight();
        ImageView cloud = (ImageView) rootView.findViewById(R.id.cloud1);
        Animation a = new TranslateAnimation(0,width/4,0 ,0);

        //Animation a2 = new TranslateAnimation(0,width+(-lp.leftMargin),0 ,0);
        //a2.setInterpolator(new AccelerateDecelerateInterpolator());
        //a2.setInterpolator(new LinearInterpolator());

        //a2.setRepeatCount(Animation.INFINITE);
        //a2.setDuration(2800);
        a.setAnimationListener(new CloudStartListner(cloud));
        a.setDuration(2400);
        a.setFillAfter(true);
        a.setInterpolator(new LinearInterpolator());
        cloud.startAnimation(a);
        cloud = (ImageView) rootView.findViewById(R.id.cloud2);
        //ObjectAnimator.
        //animX2.setDuration(3000);
        a  = new TranslateAnimation(0,200/4,0,0);
        a.setInterpolator(new LinearInterpolator());
        a.setFillAfter(true);
        a.setDuration(2400);
        //  a2.setInterpolator(new AccelerateDecelerateInterpolator());
       // a2.setInterpolator(new LinearInterpolator());

      //  a2.setRepeatCount(Animation.INFINITE);
      //  a2.setDuration(13000);
        a.setAnimationListener(new CloudStartListner(cloud));
        cloud.startAnimation(a);


        //animX2.setRepeatCount(Animation.INFINITE);
        //animX2.start();

        cloud = (ImageView) rootView.findViewById(R.id.cloud3);
        a = new TranslateAnimation(0,width/4,0 ,0);
        a.setInterpolator(new LinearInterpolator());
        a.setFillAfter(true);
        a.setDuration(2900);
        cloud.startAnimation(a);

        cloud = (ImageView) rootView.findViewById(R.id.cloud4);
        a  = new TranslateAnimation(0,250/4,0,0);
        a.setInterpolator(new LinearInterpolator());
        a.setFillAfter(true);
        a.setDuration(2400);
        //   a2 = new TranslateAnimation(0,width+(-lp.leftMargin),0 ,0);
     //   a2.setInterpolator(new AccelerateDecelerateInterpolator());
     //   a2.setInterpolator(new LinearInterpolator());

      //  a2.setRepeatCount(Animation.INFINITE);
      //  a2.setDuration(8000);
        a.setAnimationListener(new CloudStartListner(cloud));
        cloud.startAnimation(a);

        cloud = (ImageView) rootView.findViewById(R.id.cloud9);
        //ObjectAnimator.
        //animX2.setDuration(3000);
        a  = new TranslateAnimation(0,width/5,0,0);
        a.setInterpolator(new LinearInterpolator());
        a.setFillAfter(true);
        a.setDuration(2400);
        a.setAnimationListener(new CloudStartListner(cloud));
        cloud.startAnimation(a);


    }

    public <T> T find(int id, Class<T> clz) {
        return clz.cast(findViewById(id));
    }
}
