package se.lundakarnevalen.extern.map;

import se.lundakarnevalen.extern.data.DataElement;

/**
 * Created by Filip on 2014-04-18.
 */
public class Marker implements Comparable<Marker>{
    public static final String LOG_TAG = "Marker";

    public float x = -1;
    public float y = -1;

    public int picture;
    public DataElement element;

    public boolean isFocusedInMap = false;

    public Marker(DataElement elm ) {
        this.element = elm;
        this.picture = elm.picture;
    }

    public Marker(float v, float v1, int barnevalen_logo, int fun) {}

    public float distance(float relativeX, float relativeY) {
        float dx = x - relativeX;
        float dy = y - relativeY;
        return dx * dx + dy * dy;
    }

    @Override
    public int compareTo(Marker marker) {
        if(element.lat < marker.element.lat) {
            return 1;
        } else {
            return -1;
        }
    }
}
