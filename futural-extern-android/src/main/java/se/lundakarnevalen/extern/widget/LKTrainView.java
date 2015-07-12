package se.lundakarnevalen.extern.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;
import com.nineoldandroids.animation.PropertyValuesHolder;
import com.nineoldandroids.animation.ValueAnimator;

import se.lundakarnevalen.extern.android.R;
import se.lundakarnevalen.extern.map.LocationTracker;
import se.lundakarnevalen.extern.util.BitmapUtil;
import se.lundakarnevalen.extern.util.Logf;

import static android.graphics.Matrix.MSCALE_X;
import static se.lundakarnevalen.extern.util.ViewUtil.dpToPx;

public class LKTrainView extends SVGView {
    private static final String LOG_TAG = LKTrainView.class.getSimpleName();

    //55.71025, 13.18649 (top - left )
    private static final float startLatMap = 55.71025f;
    private static final float startLonMap = 13.18649f;
    //55.69963, 13.20312 (bottom - right)
    private static final float endLatMap = 55.69963f;
    private static final float endLonMap = 13.20312f;

    private static final float diffLat = endLatMap - startLatMap;
    private static final float diffLon = endLonMap - startLonMap;

    private static final int BITMAP_TRAIN = 123;

    private Paint mShadowInk;
    private Picture mGpsMarker;

    private float mGpsMarkerSize;
    private float mGpsShadowXRadius;
    private float mGpsShadowYRadius;

    private PointF mGpsMarkerPos = new PointF(0,0);

    private Paint mPaint;
    private RectF dst = new RectF();
    private float mPreDrawScale;

    private float mTrainMarkerSize;

    private float mTrainPosX = -100;
    private float mTrainPosY = -100;

    private static SparseArray<Bitmap> bitmaps = new SparseArray<>();

    public static void clean() {
        for(int i = 0; i < bitmaps.size(); i++) {
            bitmaps.valueAt(i).recycle();
        }
        bitmaps.clear();
    }

    public LKTrainView(Context context) {
        super(context);
        initMap(context);
    }

    public LKTrainView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initMap(context);
    }

    public LKTrainView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initMap(context);
    }

    private void initMap(Context context) {
        if(isInEditMode()) return;
        mShadowInk = new Paint(Paint.ANTI_ALIAS_FLAG);
        mShadowInk.setColor(Color.argb(128, 128, 128, 128));
        mPaint = new Paint();
        try {
            mGpsMarker = SVG.getFromResource(context, R.raw.gps_marker).renderToPicture();
            bitmaps.put(BITMAP_TRAIN, BitmapUtil.decodeSampledBitmapFromResource(context.getResources(), R.drawable.train_marker, 96, 96));
        } catch (SVGParseException e) {
            e.printStackTrace();
        }

        mGpsMarkerSize = dpToPx(context, 80);
        mGpsShadowXRadius = dpToPx(context, 10);
        mGpsShadowYRadius = dpToPx(context, 6);
        mTrainMarkerSize = dpToPx(context, 40);
    }



    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        updateViewLimitBounds();
    }

    @Override
    protected boolean onClick(float xInSvg, float yInSvg) {
        Logf.d(LOG_TAG, "click(%f, %f, %f)", xInSvg, yInSvg, mPreDrawScale);
        return false;
    }

    public void goToGpsMarker(int svgX, int svgY){
        panTo(svgX,svgY);
    }

    @Override
    protected void onDrawObjects(Canvas canvas) {
        super.onDrawObjects(canvas); // Must be called to draw map
        mPreDrawScale = mMatrixValues[MSCALE_X];

        dst.set(mGpsMarkerPos.x,
                mGpsMarkerPos.y,
                mGpsMarkerPos.x + 2.0f * mGpsShadowXRadius / mPreDrawScale,
                mGpsMarkerPos.y + 2.0f * mGpsShadowYRadius / mPreDrawScale);
        normalizeToMidpointBottom(dst);
        canvas.drawOval(dst, mShadowInk);

        dst.set(mGpsMarkerPos.x,
                mGpsMarkerPos.y,
                mGpsMarkerPos.x + mGpsMarkerSize / mPreDrawScale,
                mGpsMarkerPos.y + mGpsMarkerSize / mPreDrawScale);
        normalizeToMidpointBottom(dst);
        canvas.drawPicture(mGpsMarker, dst);

        dst.set(mTrainPosX,
                mTrainPosY,
                mTrainPosX + mTrainMarkerSize / mPreDrawScale,
                mTrainPosY + mTrainMarkerSize / mPreDrawScale);
        normalizeToMidpointBottom(dst);
        canvas.drawBitmap(bitmaps.get(BITMAP_TRAIN), null, dst, mPaint);
    }

    public void getPointFromCoordinates(float lat, float lon, float[] dst) {
        float lat1 = (lat - startLatMap) / diffLat;
        float lon1 = (lon - startLonMap) / diffLon;
        if(mPictureEndPoint[AXIS_X] == -1) {
            dst[AXIS_X] = lon1 * 1080;
            dst[AXIS_Y] = lat1 * 1368;
        } else {
            dst[AXIS_X] = lon1 * mPictureEndPoint[AXIS_X];
            dst[AXIS_Y] = lat1 * mPictureEndPoint[AXIS_Y];
        }
    }

    private void normalizeToMidpointBottom(RectF rect) {
        rect.offset(-0.5f * rect.width(), -rect.height());
    }

    public void setGpsMarker(float lat, float lng, boolean panToMarker) {
        float[] tmp = new float[2];
        getPointFromCoordinates(lat, lng, tmp);
        setGpsMarker((int) tmp[AXIS_X], (int) tmp[AXIS_Y], panToMarker); // (int) is important to avoid recursion
    }

    public void setGpsMarker(int x, int y, boolean panToMarker) {
        Logf.d(LOG_TAG, "GPSMarker moved to (%d, %d)", x, y);
        PropertyValuesHolder xh = PropertyValuesHolder.ofFloat("x", mGpsMarkerPos.x, x);
        PropertyValuesHolder yh = PropertyValuesHolder.ofFloat("y", mGpsMarkerPos.y, y);
        ValueAnimator anim = ValueAnimator.ofPropertyValuesHolder(xh, yh);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mGpsMarkerPos.x = (Float) animation.getAnimatedValue("x");
                mGpsMarkerPos.y = (Float) animation.getAnimatedValue("y");
                postInvalidate();
            }
        });
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.setDuration(1000);
        anim.start();

        if(panToMarker) { // Will fire of async as the above animation
            panTo(x, y);
        }
    }

    public boolean isWithinLatLngRange(float lat, float lng) {
        return  (startLatMap > lat && lat > endLatMap) &&
                (startLonMap < lng && lng < endLonMap);
    }

    public void setTrainLocation(LocationTracker.LocationJSONResult.LatLng latLng) {
        float[] xy = new float[2];
        getPointFromCoordinates(latLng.lat, latLng.lng, xy);
        mTrainPosX = xy[0];
        mTrainPosY = xy[1];
        Logf.d(LOG_TAG, "New Train pos: %f, %f => %f, %f",latLng.lat, latLng.lng, xy[0], xy[1]);
    }
}
