package se.lundakarnevalen.extern.activities;

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

import se.lundakarnevalen.extern.android.R;
import se.lundakarnevalen.extern.map.MapLoader;

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

        moveCloud(R.id.cloud1, width / 4, 2900, rootView);
        moveCloud(R.id.cloud2, 200 / 4, 2900, rootView);
        moveCloud(R.id.cloud3, width / 4, 2900, rootView);
        moveCloud(R.id.cloud4, 250 / 4, 2900, rootView);
        moveCloud(R.id.cloud9, width / 5, 2900, rootView);
    }

    private void moveCloud(int id, int dx, int dur, View rootView) {
        ImageView cloud = (ImageView) rootView.findViewById(id);
        Animation a = new TranslateAnimation(0, dx, 0 , 0);
        a.setAnimationListener(new CloudStartListner(cloud));
        a.setDuration(dur);
        a.setFillAfter(true);
        a.setInterpolator(new LinearInterpolator());
        cloud.startAnimation(a);
}

    public <T> T find(int id, Class<T> clz) {
        return clz.cast(findViewById(id));
    }
}
