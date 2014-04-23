package se.lundakarnevalen.extern.util;

import android.util.Log;

/**
 * Created by Filip on 2014-03-27.
 */
public class Timer {
    private long start;

    public Timer() {
        reset();
    }

    public void reset() {
        this.start = System.currentTimeMillis();
    }

    public void tick(String tag, String msg){
        long delta = System.currentTimeMillis() - start;
        Log.d(tag, String.format("%s (%d ms)",msg, delta));
        start = System.currentTimeMillis();
    }
}
