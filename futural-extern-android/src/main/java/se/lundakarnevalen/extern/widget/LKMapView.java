package se.lundakarnevalen.extern.widget;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.LruCache;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;

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
import se.lundakarnevalen.extern.util.BitmapUtil;
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
    private PointF lastPos = new PointF(0,0);

    private RectF dst = new RectF();

    private static final float startLonMap = 13.1910161648f;
    private static final float startLatMap = 55.707371322f;

    private static final float endLonMap = 13.1979612196f;
    private static final float endLatMap = 55.7034716513f;

    private static final float diffLon = endLonMap - startLonMap;
    private static final float diffLat = endLatMap - startLatMap;

    private HashMap<Integer, Bitmap> bitmaps;

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

        mBubbleSize = dpToPx(context, 10);
        mBubbleShadowXRadius = dpToPx(context, 2);
        mBubbleShadowYRadius = dpToPx(context, 1);

        // @TODO: Add all markers

        Markers.addMarkers(markers);
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
        float scale = mMatrixValues[MSCALE_X];
        float pictureHeight = mPictureEndPoint[AXIS_X];
        float pictureWidth = mPictureEndPoint[AXIS_Y];

        // TODO TEST
        /*
        markers = new ArrayList<Marker>();

        markers.add(new Marker(55.7048333f, 13.195352777777778f, R.drawable.cirkusen_logo, MarkerType.FUN));

        markers.add(new Marker(55.7059389f, 13.194805555555556f, R.drawable.filmen_logo, MarkerType.FUN));
        */


        for (Marker m : markers) {
            if(activeTypes.contains(m.type)) {
                if (m.x == -1) {
                    final float lat = (m.lat - startLatMap) / diffLat;
                    final float lon = (m.lng - startLonMap) / diffLon;
                    m.x = lon * pictureWidth;
                    m.y = lat * pictureHeight;
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

        float lat1 = (55.705655f - startLatMap) / diffLat;
        float lon1 = (13.194277f - startLonMap) / diffLon;
        float m1x = lon1 * pictureWidth;
        float m1y = lat1 * pictureHeight;
        canvas.drawCircle(m1x, m1y, mGpsShadowXRadius/scale, mBlueInk);

        float lat2 = (55.705439f - startLatMap) / diffLat; //y
        float lon2 = (13.193153f - startLonMap) / diffLon;
        float m2x = lon2 * pictureWidth;
        float m2y = lat2 * pictureHeight;
        canvas.drawCircle(m2x, m2y, mGpsShadowXRadius/scale, mLightBlueInk);

        dst.set(mGpsMarkerPos.x,
                mGpsMarkerPos.y,
                mGpsMarkerPos.x + 2.0f * mGpsShadowXRadius / scale,
                mGpsMarkerPos.y + 2.0f * mGpsShadowYRadius / scale);
        normalizeToMidpointBottom(dst);
        canvas.drawOval(dst, mShadowInk);

        dst.set(mGpsMarkerPos.x,
                mGpsMarkerPos.y,
                mGpsMarkerPos.x + mGpsMarkerSize / scale,
                mGpsMarkerPos.y + mGpsMarkerSize / scale);
        normalizeToMidpointBottom(dst);
        canvas.drawPicture(mGpsMarker, dst);
    }

    private void normalizeToMidpointBottom(RectF rect) {
        rect.offset(-0.5f * rect.width(), -rect.height());
    }

    public void setGpsMarker(float lat, float lng){
        float pictureHeight = mPictureEndPoint[AXIS_X];
        float pictureWidth = mPictureEndPoint[AXIS_Y];
        float lLat = (55.705439f - startLatMap) / diffLat; //y
        float lLon = (13.193153f - startLonMap) / diffLon;
        float mx = lLon * pictureWidth;
        float my = lLat * pictureHeight;
        mGpsMarkerPos.set(mx, my);
        postInvalidate();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void setGpsMarker(Fragment a, int x, int y) {
        Logf.d(LOG_TAG, "GPSMarker moved to (%d, %d)", x, y);
        if(Build.VERSION.SDK_INT > 10){
            ValueAnimator animX =  ValueAnimator.ofFloat(mGpsMarkerPos.x, x);
            animX.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mGpsMarkerPos.x = (Float) animation.getAnimatedValue();
                    invalidate();
                }
            });
            ValueAnimator animY =  ValueAnimator.ofFloat(mGpsMarkerPos.x, x);
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

            a.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    animation.start();
                }
            });
        } else {
            mGpsMarkerPos.set(x, y);
            postInvalidate();
        }
    }

    public void setActiveTypes(Collection<Integer> types) {
        activeTypes.clear();
        activeTypes.addAll(types);
        postInvalidate();
    }
}
