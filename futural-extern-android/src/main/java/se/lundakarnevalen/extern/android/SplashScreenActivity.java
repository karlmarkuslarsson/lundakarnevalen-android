package se.lundakarnevalen.extern.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Bundle;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;

import static se.lundakarnevalen.extern.util.ViewUtil.*;

public class SplashScreenActivity extends Activity {
    private static final int SPLASH_TIME_OUT = 300;

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
        }, SPLASH_TIME_OUT);

        if(Build.VERSION.SDK_INT >= 11){
            find(R.id.imgLogo, ImageView.class).animate()
                .rotationBy(360)
                .setDuration(SPLASH_TIME_OUT)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .start();
        }
    }

    public <T> T find(int id, Class<T> clz) {
        return clz.cast(findViewById(id));
    }
}
