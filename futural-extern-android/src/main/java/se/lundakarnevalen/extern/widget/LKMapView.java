package se.lundakarnevalen.extern.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import se.lundakarnevalen.extern.android.R;
import se.lundakarnevalen.extern.map.Marker;
import se.lundakarnevalen.extern.map.MarkerType;
import se.lundakarnevalen.extern.map.Markers;
import se.lundakarnevalen.extern.util.Logf;

import static android.graphics.Matrix.*;

/**
 * Created by Filip on 2014-04-27.
 */
public class LKMapView extends SVGView {
    private static final String LOG_TAG = LKMapView.class.getSimpleName();

    private Map<Integer,Boolean> active = new HashMap<Integer, Boolean>();
    private List<Marker> markers = new ArrayList<Marker>();

    private Paint mShadowInk;
    private Paint mLightBlueInk;
    private Paint mBlueInk;

    private Picture gpsMarker;
    private Picture bubble;
    private float mGpsMarkerSize;
    private float mGpsShadowXRadius;
    private float mGpsShadowYRadius;

    private PointF lastPos = new PointF(320, 260);
    private RectF dest = new RectF();
    private Random r = new Random();

    private static final float startLonMap = 13.1910161648f;
    private static final float startLatMap = 55.7034716513f;

    private static final float endLonMap = 13.1979612196f;
    private static final float endLatMap = 55.707371322f;

    private static final float diffLon = endLonMap - startLonMap;
    private static final float diffLat = endLatMap - startLatMap;

    public LKMapView(Context context) {
        super(context);
        initMap(context);
    }

    public LKMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initMap(context);
    }

    public LKMapView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initMap(context);
    }

    private void initMap(Context context) {
        if(isInEditMode()) return;
        mShadowInk = new Paint(Paint.ANTI_ALIAS_FLAG);
        mShadowInk.setColor(Color.argb(128, 128, 128, 128));

        mBlueInk = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBlueInk.setColor(Color.rgb(74, 139, 244));

        mLightBlueInk = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLightBlueInk.setColor(Color.rgb(97, 157, 229));
        mLightBlueInk.setShadowLayer(1.5f, 0f, 0f, Color.BLACK);

        try {
            gpsMarker = SVG.getFromResource(context, R.raw.gps_marker).renderToPicture();
            bubble = SVG.getFromResource(context, R.raw.bubble).renderToPicture();
        } catch (SVGParseException e) {
            e.printStackTrace();
        }

        mGpsMarkerSize = dpToPx(context, 80);
        mGpsShadowXRadius = dpToPx(context, 10);
        mGpsShadowYRadius = dpToPx(context, 6);

        Markers.addMarkers(markers);

        if(active.size() == 0) {
            active.put(MarkerType.FOOD, true);
            active.put(MarkerType.FUN,  true);
            active.put(MarkerType.HELP, true);
            active.put(MarkerType.WC,   true);
        }
    }

    public static int dpToPx(Context c, int dp) {
        DisplayMetrics displayMetrics = c.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    @Override
    protected boolean onClick(float xInSvg, float yInSvg) {
        Logf.d(LOG_TAG, "click(%f, %f)", xInSvg, yInSvg);
        return true;
    }

    @Override
    protected void onDrawObjects(Canvas canvas) {
        super.onDrawObjects(canvas); // Must be called to draw map
        float scale = mMatrixValues[MSCALE_X];

        int pictureHeight = mPicture.getHeight();
        int pictureWidth = mPicture.getWidth();
/*
        for (Marker m : markers) {
            //canvas.drawCircle(marker.x, marker.y, 10, mShadowInk);
            if(active.get(m.type) != null && active.get(m.type)) {
                if (m.x == -1) {
                    float lat = (m.lat - startLatMap) / diffLat;
                    float lon = (m.lng - startLonMap) / diffLon;
                    m.x = lon * pictureWidth;
                    m.y = pictureHeight - lat * pictureHeight;
                }
                canvas.drawCircle(m.x, m.y, mGpsShadowXRadius/scale, mBlueInk);
            }
        }
*/
        float lat1 = (55.705655f - startLatMap) / diffLat;
        float lon1 = (13.194277f - startLonMap) / diffLon;
        float m1x = lon1 * pictureWidth;
        float m1y = pictureHeight-lat1 * pictureHeight;
        canvas.drawCircle(m1x, m1y, mGpsShadowXRadius/scale, mBlueInk);

        canvas.drawCircle(0, 0, mGpsShadowXRadius/scale, mBlueInk);
        canvas.drawCircle(512, 512, mGpsShadowXRadius/scale, mBlueInk);


        float lat2 = (55.705439f - startLatMap) / diffLat; //y
        float lon2 = (13.193153f - startLonMap) / diffLon;
        float m2x = lon2 * pictureWidth;
        float m2y = pictureHeight-lat2 * pictureHeight;
        canvas.drawCircle(m2x, m2y, mGpsShadowXRadius/scale, mLightBlueInk);

        // Do a random walk +-2 steps
        lastPos.set(r.nextInt(5) - 2 + lastPos.x, r.nextInt(5) - 2 + lastPos.y);

        dest.set(lastPos.x +(mGpsMarkerSize * 0.5f - mGpsShadowXRadius) /scale,
                lastPos.y + (mGpsMarkerSize - 2.0f * mGpsShadowYRadius) /scale,
                lastPos.x + (mGpsMarkerSize * 0.5f + mGpsShadowXRadius) /scale,
                lastPos.y + (mGpsMarkerSize - 0    * mGpsShadowYRadius) /scale);
        canvas.drawOval(dest, mShadowInk);

        dest.set(lastPos.x,
                lastPos.y,
                lastPos.x + mGpsMarkerSize/scale,
                lastPos.y + mGpsMarkerSize/scale);
        canvas.drawPicture(gpsMarker, dest);




    }
}
