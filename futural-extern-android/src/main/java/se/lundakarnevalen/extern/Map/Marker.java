package se.lundakarnevalen.extern.map;

import android.util.Log;

/**
 * Created by Filip on 2014-04-18.
 */
public class Marker {
    public static final String LOG_TAG = "Marker";
    public float x = -1;
    public float y = -1;
    public float lat;
    public float lng;
    public int picture;
    public int type;

    public Marker(float lat, float lng, int picture, int type) {
        this.lat = lat;
        this.lng = lng;
        this.picture = picture;
        this.type = type;
    }

    // scale to??
    public boolean isClose(float relativeX, float relativeY) {
        if ((x - relativeX) * (x - relativeX) + (y - relativeY) * (y - relativeY) < 600) {
            Log.d(LOG_TAG, "yeah close");
            return true;
        }
        Log.d(LOG_TAG, "dist" + ((x - relativeX) * (x - relativeX) + (y - relativeY) * (y - relativeY)));
        return false;
    }
}
