package se.lundakarnevalen.extern.map;

import android.util.Log;

import se.lundakarnevalen.extern.data.DataElement;
import se.lundakarnevalen.extern.data.DataType;

/**
 * Created by Filip on 2014-04-18.
 */
public class Marker {
    public static final float CLOSE_THRESHOLD = 240.0f;
    public static final String LOG_TAG = "Marker";

    public float x = -1;
    public float y = -1;

    public float lat;
    public float lng;

    public int picture;
    public DataType type;

    public boolean isFocusedInMap = false;

    public Marker(DataElement elm ) {
        this.lat = elm.lat;
        this.lng = elm.lng;
        this.type = elm.type;
        this.picture = elm.picture;
    }

    public Marker(float v, float v1, int barnevalen_logo, int fun) {

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
