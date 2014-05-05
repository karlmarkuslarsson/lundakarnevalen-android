package se.lundakarnevalen.extern.map;

import android.util.Log;

/**
 * Created by Filip on 2014-04-18.
 */
public class Marker {
    public static final float CLOSE_THRESHOLD = 200.0f;
    public static final String LOG_TAG = "Marker";

    public float x = -1;
    public float y = -1;

    public float lat;
    public float lng;

    public int picture;
    public int type;

    public boolean isFocusedInMap = false;

    public Marker(float lat, float lng, int picture, int type) {
        this.lat = lat;
        this.lng = lng;
        this.picture = picture;
        this.type = type;
    }

    public boolean isClose(float relativeX, float relativeY) {
        float dx = x - relativeX;
        float dy = y - relativeY - 20;
        float dist = dx * dx + dy * dy;
        if (dist < CLOSE_THRESHOLD) {
            Log.d(LOG_TAG, "isClose: dist="+dist);
            return true;
        }
        return false;
    }
}
