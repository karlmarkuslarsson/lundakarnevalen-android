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
import java.util.List;
import java.util.Random;

import se.lundakarnevalen.extern.android.R;
import se.lundakarnevalen.extern.map.Marker;
import se.lundakarnevalen.extern.map.Markers;
import se.lundakarnevalen.extern.util.Logf;

import static android.graphics.Matrix.*;

/**
 * Created by Filip on 2014-04-27.
 */
public class LKMapView extends SVGView {
    private static final String LOG_TAG = LKMapView.class.getSimpleName();

    private List<Marker> markers = new ArrayList<Marker>();

    private Paint mBlackInk;
    private Paint mLightBlueInk;
    private Paint mBlueInk;

    private Picture gpsMarker;
    private Picture bubble;
    private float mGpsMarkerSize;
    private float mGpsMarkerBlueRadius;
    private float mGpsMarkerLightBlueRadius;

    private PointF lastPos = new PointF(320, 260);
    private RectF dest = new RectF();
    private Random r = new Random();

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
        mBlackInk = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBlackInk.setColor(Color.BLACK);

        mBlueInk = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBlueInk.setColor(Color.rgb(74,139,244));

        mLightBlueInk = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLightBlueInk.setColor(Color.rgb(97,157,229));
        mLightBlueInk.setShadowLayer(1.5f, 0f, 0f, Color.BLACK);

        try {
            gpsMarker = SVG.getFromResource(context, R.raw.gps_marker).renderToPicture();
            bubble = SVG.getFromResource(context, R.raw.bubble).renderToPicture();
        } catch (SVGParseException e) {
            e.printStackTrace();
        }

        mGpsMarkerSize = dpToPx(context, 80);
        mGpsMarkerBlueRadius = dpToPx(context, 8);
        mGpsMarkerLightBlueRadius = dpToPx(context, 6);

        Markers.addMarkers(markers);
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

        for (Marker marker : markers) {
            //canvas.drawCircle(marker.x, marker.y, 10, mBlackInk);
        }

        // Do a random walk +-2 steps
        lastPos.set(r.nextInt(5) - 2 + lastPos.x, r.nextInt(5) - 2 + lastPos.y);
/*
        //Blue dot
        dest.set(lastPos.x,
                lastPos.y,
                lastPos.x + mGpsMarkerSize/scale,
                lastPos.y + mGpsMarkerSize/scale);
        canvas.drawCircle((dest.left + dest.right) * 0.5f, dest.bottom - 8f/scale, mGpsMarkerBlueRadius/scale, mBlueInk);
        canvas.drawCircle((dest.left + dest.right) * 0.5f, dest.bottom - 8f/scale, mGpsMarkerLightBlueRadius/scale, mLightBlueInk);

*/
        dest.set(lastPos.x,
                lastPos.y,
                lastPos.x + mGpsMarkerSize/scale,
                lastPos.y + mGpsMarkerSize/scale);
        canvas.drawPicture(gpsMarker, dest);
    }
}
