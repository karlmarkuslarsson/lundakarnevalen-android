package se.lundakarnevalen.extern.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;
import com.nineoldandroids.animation.PropertyValuesHolder;
import com.nineoldandroids.animation.ValueAnimator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import se.lundakarnevalen.extern.android.R;
import se.lundakarnevalen.extern.data.DataContainer;
import se.lundakarnevalen.extern.data.DataElement;
import se.lundakarnevalen.extern.data.DataType;
import se.lundakarnevalen.extern.map.Marker;
import se.lundakarnevalen.extern.util.BitmapUtil;
import se.lundakarnevalen.extern.util.Logf;

import static android.graphics.Matrix.MSCALE_X;
import static se.lundakarnevalen.extern.util.ViewUtil.dpToPx;

/**
 * Created by Filip on 2014-04-27.
 */
public class LKMapView extends SVGView {
    private static final String LOG_TAG = LKMapView.class.getSimpleName();

    private static final float startLonMap = 13.1910161648f;
    private static final float startLatMap = 55.707371322f;

    private static final float endLonMap = 13.1979612196f;
    private static final float endLatMap = 55.7034716513f;

    private static final float diffLon = endLonMap - startLonMap;
    private static final float diffLat = endLatMap - startLatMap;

    private static final float CLOSE_THRESHOLD = 46.0f; //last 40
    private static final float BUBBLE_SIZE_MULTIPLIER = 3.0f;

    private static SparseArray<Bitmap> bitmaps = new SparseArray<Bitmap>();
    private RectF mCurrentViewPort = new RectF();

    public static void clean() {
        for(int i = 0; i < bitmaps.size(); i++) {
            bitmaps.valueAt(i).recycle();
        }
        bitmaps.clear();
    }

    public boolean isWithinLatLngRange(float lat, float lng) {
        return  (startLatMap > lat && lat > endLatMap) &&
                (startLonMap < lng && lng < endLonMap);
    }

    public interface OnMarkerSelectedListener {
        /** null of unselect */
        public void onMarkerSelected(Marker m);
    }


    private Set<DataType> activeTypes = new HashSet<DataType>();
    private List<Marker> markers = new ArrayList<Marker>();

    private Paint mShadowInk;
    private Paint mLightBlueInk;
    private Paint mBlueInk;

    private Picture mGpsMarker;
    private Picture mBubble;

    private float mGpsMarkerSize;
    private float mGpsShadowXRadius;
    private float mGpsShadowYRadius;

    private float mBubbleSize;
    private float mBubbleShadowXRadius;
    private float mBubbleShadowYRadius;

    private PointF mGpsMarkerPos = new PointF(0,0);

    private RectF dst = new RectF();

    private float mPreDrawScale;

    private Marker mFocusedMarker;
    private OnMarkerSelectedListener mListener;

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
            mGpsMarker = SVG.getFromResource(context, R.raw.gps_marker).renderToPicture();
            mBubble = SVG.getFromResource(context, R.raw.bubble).renderToPicture();
        } catch (SVGParseException e) {
            e.printStackTrace();
        }

        mGpsMarkerSize = dpToPx(context, 80);
        mGpsShadowXRadius = dpToPx(context, 10);
        mGpsShadowYRadius = dpToPx(context, 6);

        mBubbleSize = 14.0f;//dpToPx(context, 8);
        mBubbleShadowXRadius = mBubbleSize/4.0f; //dpToPx(context, 2);
        mBubbleShadowYRadius = mBubbleSize/8.0f; //dpToPx(context, 1);

        markers.clear();
        for (DataElement elm : DataContainer.getAllData()) {
            markers.add(new Marker(elm));
        }

        Collections.sort(markers);

        initBitmapCache(context);
        initZoomLimit(context);
    }

    /**
     * We want to have difirent
     */
    private void initZoomLimit(Context context) {
        final Resources r = getResources();
        if(r != null && r.getDisplayMetrics() != null) {
            final int dpi = r.getDisplayMetrics().densityDpi;
            switch (dpi){
                case DisplayMetrics.DENSITY_XXXHIGH: setMaxZoom(12.0f); break;
                case DisplayMetrics.DENSITY_XXHIGH: setMaxZoom(11.0f); break;
                case DisplayMetrics.DENSITY_XHIGH: setMaxZoom(10.0f); break;
                case DisplayMetrics.DENSITY_HIGH: setMaxZoom(9.0f); break;
                case DisplayMetrics.DENSITY_MEDIUM: setMaxZoom(8.0f); break;
                case DisplayMetrics.DENSITY_LOW: setMaxZoom(7.0f); break;
                default: setMaxZoom(7.0f); break;
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        updateViewLimitBounds();
    }

    private void initBitmapCache(Context context) {
        int hw = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ? 224 : 56;
        for (Marker m : markers) {
            if(bitmaps.get(m.picture) == null) {
                bitmaps.put(m.picture, BitmapUtil.decodeSampledBitmapFromResource(context.getResources(), m.picture, hw, hw));
            }
        }
    }

    public void setListener(OnMarkerSelectedListener listener) {
        this.mListener = listener;
    }

    public boolean triggerClick(float xInSvg, float yInSvg) {
        return onClick(xInSvg, yInSvg);
    }

    @Override
    protected boolean onClick(float xInSvg, float yInSvg) {
        final float offsetY = -10.0f / mPreDrawScale;
        if(addExtra>0) {
            yInSvg += addExtra/mPreDrawScale;
        }
        float min = Float.MAX_VALUE;
        Marker closest = null;
        for (Marker m : markers) {
            if(activeTypes.contains(m.element.type)) {
                if (m.x != -1) {
                    final float distance = m.distance(xInSvg, yInSvg - offsetY) / mPreDrawScale;
                    if (distance < min) {
                        min = distance;
                        closest = m;
                    }
                } else {
                    m.isFocusedInMap = false;
                }
            }
        }

        boolean found = (closest != null && min < CLOSE_THRESHOLD * mPreDrawScale);
        Logf.d(LOG_TAG, "click(%f, %f, %f) => Dist: %f, Closest: %s", xInSvg, yInSvg, mPreDrawScale, min, (closest != null) ? closest.element.title : closest);

        if(mFocusedMarker != null) {
            mFocusedMarker.isFocusedInMap = false;
        }

        if (found) {
            closest.isFocusedInMap = true;
            mFocusedMarker = closest;
            panTo(xInSvg, yInSvg);
            if (mListener != null) {
                mListener.onMarkerSelected(closest);
            }
        } else {
            mFocusedMarker = null;
            mListener.onMarkerSelected(null);
        }
        return false;
    }

    public void goToGpsMarker(int svgX, int svgY){


        if(mFocusedMarker != null) {
            mFocusedMarker.isFocusedInMap = false;
        }
        mFocusedMarker = null;
        mListener.onMarkerSelected(null);

        panTo(svgX,svgY);
    }

    @Override
    protected void onDrawObjects(Canvas canvas) {
        super.onDrawObjects(canvas); // Must be called to draw map
        mPreDrawScale = mMatrixValues[MSCALE_X];

        getCurrentViewPort(mCurrentViewPort);
        mCurrentViewPort.inset(-10.0f, -10.0f);

        for (Marker m:markers) {
            if(activeTypes.contains(m.element.type)) {
                if (m.x == -1) {
                    getPointFromCoordinates(m);
                }

                if(m.isFocusedInMap || !mCurrentViewPort.contains(m.x, m.y)) continue;

                paintMarker(canvas, m);
            }
        }

        if(mFocusedMarker != null) {
            paintMarkerFocused(canvas, mFocusedMarker);
        }

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
    }

    private void paintMarker(Canvas canvas, Marker m) {
        if (m.element.type == DataType.ENTRANCE){
            dst.set(m.x,
                    m.y,
                    m.x + mBubbleSize * 2.0f,
                    m.y + mBubbleSize * 1.18f);
            dst.offset(-0.5f * dst.width(), -0.5f * dst.height()); // center in XY
            canvas.drawBitmap(bitmaps.get(m.picture), null, dst, null);
        } else {
            dst.set(m.x, m.y, m.x, m.y);
            dst.inset(-mBubbleShadowXRadius, -mBubbleShadowYRadius);
            canvas.drawOval(dst, mShadowInk);

            dst.set(m.x,
                    m.y,
                    m.x + mBubbleSize,
                    m.y + mBubbleSize);
            normalizeToMidpointBottom(dst);
            canvas.drawPicture(mBubble, dst);

            dst.set(m.x,
                    m.y,
                    m.x + mBubbleSize * 0.8f,
                    m.y + mBubbleSize * 0.8f);
            normalizeToMidpointBottom(dst);
            dst.offset(0f, mBubbleSize * -0.18f);
            canvas.drawBitmap(bitmaps.get(m.picture), null, dst, null);
        }
    }

    private void paintMarkerFocused(Canvas canvas, Marker m) {
        if (m.element.type == DataType.ENTRANCE){
            dst.set(m.x,
                    m.y,
                    m.x + mBubbleSize * 3.0f,
                    m.y + mBubbleSize * 1.76f);
            dst.offset(-0.5f * dst.width(), -0.5f * dst.height()); // center in XY
            canvas.drawBitmap(bitmaps.get(m.picture), null, dst, null);
        } else {
            dst.set(m.x, m.y, m.x, m.y);
            dst.inset(-mBubbleShadowXRadius, -mBubbleShadowYRadius);
            canvas.drawOval(dst, mShadowInk);
            mBubbleSize *= BUBBLE_SIZE_MULTIPLIER;
            dst.set(m.x,
                    m.y,
                    m.x + mBubbleSize,
                    m.y + mBubbleSize);
            normalizeToMidpointBottom(dst);
            canvas.drawPicture(mBubble, dst);

            dst.set(m.x,
                    m.y,
                    m.x + mBubbleSize * 0.8f,
                    m.y + mBubbleSize * 0.8f);
            normalizeToMidpointBottom(dst);
            dst.offset(0f, mBubbleSize * -0.18f);
            canvas.drawBitmap(bitmaps.get(m.picture), null, dst, null);
            mBubbleSize /= BUBBLE_SIZE_MULTIPLIER;
        }
    }

    public void getPointFromCoordinates(float lat, float lon, float[] dst) {
        float lat1 = (lat - startLatMap) / diffLat;
        float lon1 = (lon - startLonMap) / diffLon;
        if(mPictureEndPoint[AXIS_X]==-1) {
            dst[0] = lon1 * 512;
            dst[1] = lat1 * 512;
        } else {
            dst[0] = lon1 * mPictureEndPoint[AXIS_X];
            dst[1] = lat1 * mPictureEndPoint[AXIS_Y];
        }
    }

    private void getPointFromCoordinates(Marker m) {
        float lat = (m.element.lat - startLatMap) / diffLat;
        float lon = (m.element.lng - startLonMap) / diffLon;
        m.x = lon * mPictureEndPoint[AXIS_X];
        m.y = lat * mPictureEndPoint[AXIS_Y];
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

    public void setActiveTypes(Collection<DataType> types) {
        activeTypes.clear();
        activeTypes.addAll(types);
        postInvalidate();
    }

    @Override
    public boolean updateViewLimitBounds() {
        final boolean hasLayoutAndBounds = super.updateViewLimitBounds();
        if(hasLayoutAndBounds) {
            mBubbleSize = (mViewEndPoint[AXIS_X] * 0.04f)/mMinZoom;//dpToPx(context, 8);
            mBubbleShadowXRadius = mBubbleSize/4.0f; //dpToPx(context, 2);
            mBubbleShadowYRadius = mBubbleSize/8.0f; //dpToPx(context, 1);
        }
        return hasLayoutAndBounds;
    }
}
