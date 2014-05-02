package se.lundakarnevalen.extern.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Bundle;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;

import se.lundakarnevalen.extern.fragments.MapFragment;
import se.lundakarnevalen.extern.fragments.TrainMapFragment;

public class SplashScreenActivity extends Activity {
    private static final String LOG_TAG = SplashScreenActivity.class.getSimpleName();

    private static final int TOTAL_SPLASH_TIME_OUT = 1600;
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

        if(Build.VERSION.SDK_INT >= 11){
            find(R.id.imgLogo, ImageView.class)
                .animate()
                .rotationBy(360 * 3)
                .setStartDelay(PRE_ANIMATION_DELAY)
                .setDuration(TOTAL_SPLASH_TIME_OUT - POST_ANIMATION_DELAY)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .start();
        }

        MapFragment.preload(this);
        TrainMapFragment.preload(this);
    }

    public <T> T find(int id, Class<T> clz) {
        return clz.cast(findViewById(id));
    }
}
