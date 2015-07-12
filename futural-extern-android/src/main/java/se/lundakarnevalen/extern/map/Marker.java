package se.lundakarnevalen.extern.map;

import android.graphics.RectF;

import se.lundakarnevalen.extern.data.DataElement;


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

    private static final RectF dst = new RectF();

    public float distance(float relativeX, float relativeY, float bubbleSize) {
        dst.set(x,
                y,
                x + bubbleSize,
                y + bubbleSize);
        dst.offset(-0.5f * dst.width(), -dst.height());
        float dx = dst.centerX() - relativeX;
        float dy = dst.centerY() - relativeY;
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
