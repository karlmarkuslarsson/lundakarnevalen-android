package se.lundakarnevalen.extern.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import se.lundakarnevalen.extern.map.Marker;

/**
 * Created by Filip on 2014-04-27.
 */
public class LKMapView extends SVGView {

    private List<Marker> markers = Collections.emptyList();
    private Paint blackInk;
    private Paint redInk;

    public LKMapView(Context context) {
        super(context);
        init();
    }

    public LKMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LKMapView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        blackInk = new Paint(Paint.ANTI_ALIAS_FLAG);
        blackInk.setColor(Color.BLACK);
        redInk = new Paint(Paint.ANTI_ALIAS_FLAG);
        redInk.setColor(Color.RED);
        if(!isInEditMode()) {
            redInk.setShadowLayer(3f, 0f, 0f, Color.BLACK);
            //Do stuff
        }
    }

    @Override
    protected boolean onClick(float xInSvg, float yInSvg) {
        return true;
    }

    @Override
    protected void onDrawObjects(Canvas canvas) {
        super.onDrawObjects(canvas); // Must be called to draw map
        int pictureHeight = mPicture.getHeight();
        int pictureWidth = mPicture.getWidth();
        for (Marker marker : markers) {
            canvas.drawCircle(marker.x, marker.y, 10, blackInk);
        }
        //test draw in middle
        final float cx = r.nextInt(12) - 6 + pictureWidth / 2f;
        final float cy = r.nextInt(12) - 6 + pictureHeight / 2f;
        canvas.drawCircle(
                cx,
                cy,
                pictureWidth * 0.02f, redInk);
        canvas.drawCircle(
                cx,
                cy,
                pictureWidth * 0.005f, blackInk);

    }

    private Random r = new Random();
}
