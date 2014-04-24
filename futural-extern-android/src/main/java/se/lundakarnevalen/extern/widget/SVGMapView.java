package se.lundakarnevalen.extern.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;

import com.caverock.androidsvg.SVG;

import se.lundakarnevalen.extern.util.Logf;
import se.lundakarnevalen.extern.util.Timer;

/**
 * Created by Filip on 2014-04-23.
 */
public class SVGMapView extends View {

    private static final String LOG_TAG = SVGMapView.class.getSimpleName();
    private static final int INVALID_POINTER_ID = -1;

    private Matrix matrix;

    private float mScaleFactor;

    private SVG svg = null;

    private Rect r = new Rect();
    private Picture pic = new Picture();
    private Timer timer = new Timer();
    private ScaleGestureDetector mScaleDetector;
    private GestureDetector mGestures;

    private float mPosX;
    private float mPosY;

    private float mLastTouchX;
    private float mLastTouchY;
    // The focus point for the scaling
    private float scalePointX;
    private float scalePointY;

    private int mActivePointerId;
    private float lastFocusY;
    private float lastFocusX;


    public SVGMapView(Context context) {
        super(context);
        init(context);
    }

    public SVGMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SVGMapView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public void init(Context context){
        matrix = new Matrix();
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        mGestures = new GestureDetector(getContext(), new GestureListener());
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // Let the ScaleGestureDetector inspect all events first.
        mScaleDetector.onTouchEvent(ev);
        if(!mScaleDetector.isInProgress()){
            mGestures.onTouchEvent(ev);
        }
        invalidate();
        return true;
    }

    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            lastFocusX = detector.getFocusX();
            lastFocusY = detector.getFocusY();
            return true;
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= Math.max(detector.getScaleFactor(), 1.05f);
            // Don't let the object get too small or too large.
            mScaleFactor = Math.max(0.9f, Math.min(mScaleFactor, 100.0f));

            Matrix transformationMatrix = new Matrix();
            float focusX = detector.getFocusX();
            float focusY = detector.getFocusY();

            //Zoom focus is where the fingers are centered,
            transformationMatrix.postTranslate(-focusX, -focusY);
            transformationMatrix.postScale(mScaleFactor*0.5f, mScaleFactor*0.5f);

            /* Adding focus shift to allow for scrolling with two pointers down. Remove it to skip this functionality. This could be done in fewer lines, but for clarity I do it this way here */
            //Edited after comment by chochim
            float focusShiftX = focusX - lastFocusX;
            float focusShiftY = focusY - lastFocusY;
            transformationMatrix.postTranslate(focusX + focusShiftX, focusY + focusShiftY);
            matrix.postConcat(transformationMatrix);
            lastFocusX = focusX;
            lastFocusY = focusY;
            return true;
        }
    }

    private class GestureListener implements GestureDetector.OnGestureListener {
        @Override
        public boolean onScroll(MotionEvent downEvent, MotionEvent currentEvent, float distanceX, float distanceY) {
            Log.d(LOG_TAG, "onScroll()");
            matrix.postTranslate(-distanceX, -distanceY);
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            Log.d(LOG_TAG, "onLongPress()");
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Log.d(LOG_TAG, "onFling()");
            return false;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {
            Log.d(LOG_TAG, "onShowPress()");
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        timer.reset();

        canvas.setMatrix(matrix);
        canvas.getClipBounds(r);
        canvas.drawPicture(pic);

        timer.tick(LOG_TAG, "draw(): " + r+ "scale: "+mScaleFactor);
    }

    private void checkClick(float relativeX, float relativeY) {

    }

    public void setSvg(SVG svg, int w, int h, int dpi) {
        this.svg = svg;
        Timer t = new Timer();
        this.pic = svg.renderToPicture();
        float initScale = getWidth() * 1.0f / pic.getWidth();
        this.mScaleFactor = initScale;

        t.tick(LOG_TAG, "renderToPicture("+ w +" ,"+ h +")");
        postInvalidate();
    }

    public void setMatrixValues(float[] floatArray) {
        matrix.setValues(floatArray);
    }

    public float[] getMatrixValues(){
        float[] floats = new float[9];
        matrix.getValues(floats);
        return floats;
    }
}
