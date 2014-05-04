package se.lundakarnevalen.extern.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ValueAnimator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    private Set<Integer> activeTypes = new HashSet<Integer>();
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

    private static final float startLonMap = 13.1910161648f;
    private static final float startLatMap = 55.707371322f;

    private static final float endLonMap = 13.1979612196f;
    private static final float endLatMap = 55.7034716513f;

    private static final float diffLon = endLonMap - startLonMap;
    private static final float diffLat = endLatMap - startLatMap;

    private HashMap<Integer, Bitmap> bitmaps;

    private float preDrawScale;
    private float[] mTmpPoint = new float[2];

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
            mBubble = SVG.getFromResource(context, R.raw.bubble2).renderToPicture();
        } catch (SVGParseException e) {
            e.printStackTrace();
        }

        mGpsMarkerSize = dpToPx(context, 80);
        mGpsShadowXRadius = dpToPx(context, 10);
        mGpsShadowYRadius = dpToPx(context, 6);

        mBubbleSize = dpToPx(context, 14);
        mBubbleShadowXRadius = dpToPx(context, 3);
        mBubbleShadowYRadius = dpToPx(context, 2);

        // @TODO: Add all markers
        Markers.addMarkers(markers);

        markers.add(new Marker(55.70497849657609f, 13.19363857294538f, R.drawable.kabaren_logo, MarkerType.FUN));
        markers.add(new Marker(55.7047557721988f, 13.19537105245979f, R.drawable.kabaren_logo, MarkerType.FUN));
        markers.add(new Marker(55.706504880685f, 13.19547491457354f, R.drawable.kabaren_logo, MarkerType.FOOD));

        bitmaps = new HashMap<Integer, Bitmap>();
        for (Marker m : markers) {
            if(!bitmaps.containsKey(m.picture)) {
                bitmaps.put(m.picture, BitmapFactory.decodeResource(context.getResources(), m.picture));
            }
        }

        //final Marker marker = new Marker(0f,0f, R.drawable.radio_logo, MarkerType.FUN);
        //marker.x = 256;
        //marker.y = 512;
        //markers.add(marker);

        if(activeTypes.size() == 0) {
            activeTypes.add(MarkerType.FOOD);
            activeTypes.add(MarkerType.FUN);
            activeTypes.add(MarkerType.HELP);
            activeTypes.add(MarkerType.WC);
        }
    }

    public static int dpToPx(Context c, int dp) {
        DisplayMetrics displayMetrics = c.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static int dpToPx(Context c, float dp) {
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
        preDrawScale = mMatrixValues[MSCALE_X];

        // TODO TEST
        /*
        markers = new ArrayList<Marker>();
        markers.add(new Marker(55.7048333f, 13.195352777777778f, R.drawable.cirkusen_logo, MarkerType.FUN));
        markers.add(new Marker(55.7059389f, 13.194805555555556f, R.drawable.filmen_logo, MarkerType.FUN));
        */

        for (Marker m : markers) {
            if(activeTypes.contains(m.type)) {
                if (m.x == -1) {
                    getPointFromCoordinates(m.lat, m.lng, mTmpPoint);
                }

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
                        m.x + mBubbleSize*0.8f,
                        m.y + mBubbleSize*0.8f);
                normalizeToMidpointBottom(dst);
                dst.offset(0f, mBubbleSize * -0.18f);
                canvas.drawBitmap(bitmaps.get(m.picture), null, dst, null);
            }
        }

        getPointFromCoordinates(55.705439f, 13.193153f, mTmpPoint);
        canvas.drawCircle(mTmpPoint[AXIS_X], mTmpPoint[AXIS_Y], mGpsShadowXRadius/ preDrawScale, mLightBlueInk);

        dst.set(mGpsMarkerPos.x,
                mGpsMarkerPos.y,
                mGpsMarkerPos.x + 2.0f * mGpsShadowXRadius / preDrawScale,
                mGpsMarkerPos.y + 2.0f * mGpsShadowYRadius / preDrawScale);
        normalizeToMidpointBottom(dst);
        canvas.drawOval(dst, mShadowInk);

        dst.set(mGpsMarkerPos.x,
                mGpsMarkerPos.y,
                mGpsMarkerPos.x + mGpsMarkerSize / preDrawScale,
                mGpsMarkerPos.y + mGpsMarkerSize / preDrawScale);
        normalizeToMidpointBottom(dst);
        canvas.drawPicture(mGpsMarker, dst);
    }

    private void getPointFromCoordinates(float lat, float lon, float[] dst) {
        float lat1 = (lat - startLatMap) / diffLat;
        float lon1 = (lon - startLonMap) / diffLon;
        dst[0] = lon1 * mPictureEndPoint[AXIS_X];
        dst[1] = lat1 * mPictureEndPoint[AXIS_Y];
    }

    private void normalizeToMidpointBottom(RectF rect) {
        rect.offset(-0.5f * rect.width(), -rect.height());
    }

    public void setGpsMarker(float lat, float lng){
        float[] tmp = new float[2];
        getPointFromCoordinates(lat, lng, tmp);
        setGpsMarker((int) tmp[AXIS_X], (int) tmp[AXIS_Y]);
    }

    public void setGpsMarker(int x, int y) {
        Logf.d(LOG_TAG, "GPSMarker moved to (%d, %d)", x, y);
        ValueAnimator animX =  ValueAnimator.ofFloat(mGpsMarkerPos.x, x);
        animX.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mGpsMarkerPos.x = (Float) animation.getAnimatedValue();
                invalidate();
            }
        });
        ValueAnimator animY =  ValueAnimator.ofFloat(mGpsMarkerPos.y, y);
        animY.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mGpsMarkerPos.y = (Float) animation.getAnimatedValue();
                invalidate();
            }
        });
        final AnimatorSet animation = new AnimatorSet();
        animation.playTogether(animX, animY);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.setDuration(1000);
        animation.start();
    }

    public void setActiveTypes(Collection<Integer> types) {
        activeTypes.clear();
        activeTypes.addAll(types);
        postInvalidate();
    }
}
