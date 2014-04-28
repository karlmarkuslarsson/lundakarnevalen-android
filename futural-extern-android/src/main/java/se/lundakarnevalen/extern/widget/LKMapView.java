package se.lundakarnevalen.extern.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;

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

    private Paint blackInk;
    private Paint redInk;

    private Picture gpsMarker;
    private Picture bubble;

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
        blackInk = new Paint(Paint.ANTI_ALIAS_FLAG);
        blackInk.setColor(Color.BLACK);
        redInk = new Paint(Paint.ANTI_ALIAS_FLAG);
        redInk.setColor(Color.RED);
        if(!isInEditMode()) {
            redInk.setShadowLayer(3f, 0f, 0f, Color.BLACK);
            try {
                gpsMarker = SVG.getFromResource(context, R.raw.gps_marker).renderToPicture();
                bubble = SVG.getFromResource(context, R.raw.bubble).renderToPicture();
            } catch (SVGParseException e) {
                e.printStackTrace();
            }
        }
        Markers.addMarkers(markers);
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
            //canvas.drawCircle(marker.x, marker.y, 10, blackInk);
        }

        //test draw in middle
        lastPos.set(r.nextInt(5) - 2 + lastPos.x, r.nextInt(5) - 2 + lastPos.y);
        dest.set(lastPos.x,
                 lastPos.y,
                 lastPos.x + 150.0f/scale,
                 lastPos.y + 150.0f/scale);
        canvas.drawPicture(gpsMarker, dest);
    }

    private PointF lastPos = new PointF(320, 260);
    private RectF dest = new RectF();
    private Random r = new Random();
}
