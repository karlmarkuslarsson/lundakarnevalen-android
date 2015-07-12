package se.lundakarnevalen.extern.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Picture;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Toast;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.PropertyValuesHolder;
import com.nineoldandroids.animation.ValueAnimator;

import se.lundakarnevalen.extern.util.Logf;

import static android.graphics.Matrix.MSCALE_X;
import static android.graphics.Matrix.MSCALE_Y;
import static android.graphics.Matrix.MTRANS_X;
import static android.graphics.Matrix.MTRANS_Y;

public class SVGView extends View {
    private static final String LOG_TAG = SVGView.class.getSimpleName();

    public static final boolean DEBUG = false;

    public float addExtra = 0;

    public float mMaxZoom = 7.0f;
    public float mMidZoom = mMaxZoom /3;

    public static final int AXIS_X = 0;
    public static final int AXIS_Y = 1;

    private Matrix mInverseMatrix;
    private Matrix mSavedMatrix;

    private ScaleGestureDetector mScaleDetector;
    private GestureDetector mGestures;

    private float mScaleFactor;
    private float mLastFocusY;
    private float mLastFocusX;

    protected boolean mDisableTouch = false;
    protected float[] mMatrixValues = new float[9];
    protected float mMinZoom;
    protected Matrix mMatrix;
    protected Picture mPicture;

    protected final float[] mPictureEndPoint = new float[]{-1f, -1f};
    protected final float[] mViewEndPoint = new float[]{-1f, -1f};


    public SVGView(Context context) {

        super(context);

        init(context);
    }

    public SVGView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SVGView(Context context, AttributeSet attrs, int defStyle) {

        super(context, attrs, defStyle);

        init(context);
    }

    private void init(Context context){
        if(Build.VERSION.SDK_INT < 11)    {

            addExtra = 38*3;
        }
        mMatrix = new Matrix();
        mSavedMatrix = new Matrix();
        mInverseMatrix = new Matrix();
        if(!isInEditMode()){
            mPicture = new Picture();
            mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
            mGestures = new GestureDetector(getContext(), new GestureListener());
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(mDisableTouch) return false;

        // Let the ScaleGestureDetector inspect all events first.
        boolean result = mScaleDetector.onTouchEvent(ev);
        if(!mScaleDetector.isInProgress()){
            result |= mGestures.onTouchEvent(ev);
        }
        invalidate();
        return result;
    }

    public void switchToSvg(Picture p) {
        mPicture = p;
        postInvalidate();
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        private Matrix mTransformationMatrix = new Matrix();
        private float mTempScale = 1f;

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            mLastFocusX = detector.getFocusX();
            mLastFocusY = detector.getFocusY();
            //lastScale = detector.getScaleFactor();
            mSavedMatrix.set(mMatrix);
            mScaleFactor = 1;
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            mSavedMatrix.set(mMatrix); // on end we set the new mMatrix to default
        }


        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mTempScale = detector.getScaleFactor();

            final float focusX = detector.getFocusX();
            final float focusY = detector.getFocusY();

            mMatrix.getValues(mMatrixValues);

            if(mMinZoom >= mMatrixValues[MSCALE_X] * mTempScale){
                if(DEBUG) Logf.d(LOG_TAG, "Reached min zoom!");
                mLastFocusX = focusX;
                mLastFocusY = focusY;
                mTempScale = 1.0f;
            }

            if(mMaxZoom <= mMatrixValues[MSCALE_X] * mTempScale){
                if(DEBUG) Logf.d(LOG_TAG, "Reached max zoom!");
                mLastFocusX = focusX;
                mLastFocusY = focusY;
                mTempScale = 1.0f;
            }

            mScaleFactor *= mTempScale;

            if(DEBUG) Logf.d(LOG_TAG, "minZoom: %f, mTemp: %f, mScaleFactor: %f, mScaleFactor * matrix[scale]: %f",
                    mMinZoom, mTempScale, mScaleFactor, mMatrixValues[MSCALE_X] * mScaleFactor);

            //Zoom focus is where the fingers are centered,
            mTransformationMatrix.reset();
            mTransformationMatrix.postTranslate(-focusX, -focusY);
            mTransformationMatrix.postScale(mScaleFactor, mScaleFactor);

            /*
                Adding focus shift to allow for scrolling with two pointers down.
                Remove it to skip this functionality.
                This could be done in fewer lines, but for clarity I do it this way here
            */

            final float focusShiftX = focusX - mLastFocusX;
            final float focusShiftY = focusY - mLastFocusY;

            mTransformationMatrix.postTranslate(focusX + focusShiftX, focusY + focusShiftY);

            mMatrix.set(mSavedMatrix);
            mMatrix.postConcat(mTransformationMatrix);

            mLastFocusX = focusX;
            mLastFocusY = focusY;
            return true;
        }
    }

    @Override
    public void requestLayout() {
        super.requestLayout();
        Log.d(LOG_TAG, "requestLayout()");
    }

    private static float limit(float min, float value, float max){
        return Math.max(Math.min(value, max), min);
    }

    public float[] getPictureXYbyScreenXY(float x, float y){
        mMatrix.invert(mInverseMatrix);
        final float[] pts = {x, y};
        mInverseMatrix.mapPoints(pts);
        return pts;
    }

    public RectF getCurrentViewPort(RectF dst){
        mMatrix.invert(mInverseMatrix);
        dst.set(0,0, mViewEndPoint[0], mViewEndPoint[1]);
        mInverseMatrix.mapRect(dst);
        return dst;
    }

    private class GestureListener implements GestureDetector.OnGestureListener {
        @Override
        public boolean onScroll(MotionEvent downEvent, MotionEvent currentEvent, float distanceX, float distanceY) {
            scroll(distanceX, distanceY);
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {return false;}

        @Override
        public boolean onDown(MotionEvent e) {return false;}

        @Override
        public void onShowPress(MotionEvent e) {}

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            float[] ps = getPictureXYbyScreenXY(e.getX(), e.getY());
            if(DEBUG) Logf.d(LOG_TAG, "screen(%f,%f) => svg(%f,%f)\nMatrix: %s", e.getX(), e.getY(), ps[0], ps[1], mMatrix.toShortString());
            return onClick(ps[0], ps[1]);
        }
    }

    public boolean scroll(float distanceX, float distanceY) {
        boolean result = mMatrix.postTranslate(-distanceX, -distanceY);
        postInvalidate();
        return result;
    }

    public boolean translate(float distanceX, float distanceY) {

        mMatrix.getValues(mMatrixValues);
        mMatrixValues[MTRANS_X] = distanceX;
        mMatrixValues[MTRANS_Y] = distanceY;
        mMatrix.setValues(mMatrixValues);
        postInvalidate();
        return true;
    }

    public PointF getTransXY(){
        mMatrix.getValues(mMatrixValues);
        return new PointF(mMatrixValues[MTRANS_X], mMatrixValues[MTRANS_Y]);
    }

    public PointF getTransXY(Matrix m) {
        m.getValues(mMatrixValues);
        return new PointF(mMatrixValues[MTRANS_X], mMatrixValues[MTRANS_Y]);
    }

    @Override
    public void scrollBy(int x, int y) {
        scroll(x, y); //Implicit cast to float here
    }

    protected boolean onClick(float xInSvg, float yInSvg) {
        return false;
    }

    private long t0, acc = 0;
    private short counter;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(isInEditMode()) return;

        t0 = System.currentTimeMillis();
        //canvas.save();
        filterMatrix(mMatrix);

        canvas.setMatrix(mMatrix);
        onDrawObjects(canvas);
        //canvas.restore();
        acc += System.currentTimeMillis() - t0;

        if(++counter % 200 == 0){
            final long t = acc / counter;
            Logf.d(LOG_TAG, "Rendering, AVG: %d ms, FPS: %f", t, 1000f/t);
            acc = counter = 0;
        }
    }

    protected void onDrawObjects(Canvas canvas) {
        canvas.drawPicture(mPicture);
        //Nothing here yet, extend in subclass
    }

    protected void filterMatrix(Matrix matrix) {
        matrix.getValues(mMatrixValues);

        // screenW - svgW * scale is lower limit
        mMatrixValues[MTRANS_X] = limit(mViewEndPoint[AXIS_X] - mPictureEndPoint[AXIS_X] * mMatrixValues[MSCALE_X], mMatrixValues[MTRANS_X], 0);
        mMatrixValues[MTRANS_Y] = limit(addExtra+mViewEndPoint[AXIS_Y] - mPictureEndPoint[AXIS_Y] * (mMatrixValues[MSCALE_Y]), mMatrixValues[MTRANS_Y], addExtra);

        mMatrixValues[MSCALE_X] = limit(mMinZoom, mMatrixValues[MSCALE_X], mMaxZoom);
        mMatrixValues[MSCALE_Y] = limit(mMinZoom, mMatrixValues[MSCALE_Y], mMaxZoom);

        matrix.setValues(mMatrixValues);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        updateViewLimitBounds();
    }

    public boolean updateViewLimitBounds() {
        final int mw = getMeasuredWidth();
        final int mh = getMeasuredHeight();
        if(mw > 0 && mh > 0) { //Ignore if layout is not calculated yet
            this.mViewEndPoint[AXIS_X] = mw;
            this.mViewEndPoint[AXIS_Y] = mh;
            postInvalidate();
            return true;
        } else {
            final int w = getWidth();
            final int h = getHeight();
            if(w > 0 && h > 0) {
                this.mViewEndPoint[AXIS_X] = mw;
                this.mViewEndPoint[AXIS_Y] = mh;
                postInvalidate();
                return true;
            }
        }
        return false;
    }

    public void setSvgLazy(Picture svg) {
        this.mPicture = svg;
    }



    public void setSvg(Picture svg, float minZoom, float[] values) {
        this.mPicture = svg;
        this.mPictureEndPoint[AXIS_X] = svg.getWidth();
        this.mPictureEndPoint[AXIS_Y] = svg.getHeight();
        this.mMinZoom = minZoom;

        // Scale image
        float initZoom = mMinZoom * 1.0f;
        if (values != null) {
            this.mMatrix.setValues(values);
            updateViewLimitBounds();
        } else {
            this.mMatrix.reset();
            this.mMatrix.setScale(initZoom, initZoom);

            // Center image
            if(updateViewLimitBounds()) {
                float cx = (initZoom * mPictureEndPoint[AXIS_X] - mViewEndPoint[AXIS_X]) / -2.0f;
                float cy = (initZoom * mPictureEndPoint[AXIS_Y] - mViewEndPoint[AXIS_Y]) / -2.0f;
                mMatrix.postTranslate(cx, cy);
                Logf.d(LOG_TAG, "Matrix translated to center picture, translate(%f, %f)", cx, cy);
            }
        }

        postInvalidate();
    }

    public void panToCenterFast(){
        panTo(mPictureEndPoint[AXIS_X] / 2, mPictureEndPoint[AXIS_Y]/2, false);
    }

    public void panTo(float x, float y){
        panTo(x, y, true);
    }

    public void panTo(float x, float y, boolean animate){

        Matrix m = new Matrix(mMatrix);
        final PointF startXY = getTransXY(m);

        final float[] targetXY = {x, y};
        m.mapPoints(targetXY);

        final float[] centerOnViewXY = getPictureXYbyScreenXY(mViewEndPoint[0] * 0.5f, mViewEndPoint[1] * 0.7f);
        m.mapPoints(centerOnViewXY);

        final float[] end = {
                targetXY[0] - centerOnViewXY[0],
                targetXY[1] - centerOnViewXY[1]};

        float resX = startXY.x - end[0];
        float resY = startXY.y - end[1];

        if (DEBUG) Logf.d(LOG_TAG, "Animate to xy(%f, %f) => currentT(%f, %f) + deltaT(%f, %f) = (%f, %f)",
                x, y, startXY.x, startXY.y, end[0], end[1], resX, resY);

        if(animate) {
            final PropertyValuesHolder xHolder = PropertyValuesHolder.ofFloat("x", startXY.x, resX);
            final PropertyValuesHolder yHolder = PropertyValuesHolder.ofFloat("y", startXY.y, resY);

            final ValueAnimator anim = ValueAnimator.ofPropertyValuesHolder(xHolder, yHolder);
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    final float newX = (Float) animation.getAnimatedValue("x");
                    final float newY = (Float) animation.getAnimatedValue("y");
                    translate(newX, newY);
                }
            });
            anim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    mDisableTouch = true;
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    mDisableTouch = false;
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    mDisableTouch = false;
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });
            anim.setDuration(600);
            anim.start();
        } else {
            translate(resX, resY);
        }
    }

    public void zoom(float newScale) {
        mScaleFactor = newScale;
        mMatrix.getValues(mMatrixValues);
        mMatrixValues[MSCALE_X] = limit(mMinZoom, newScale, mMaxZoom);
        mMatrixValues[MSCALE_Y] = limit(mMinZoom, newScale, mMaxZoom);
        mMatrix.setValues(mMatrixValues);
        postInvalidate();
    }

    public void importMatrixValues(float[] floatArray) {
        mMatrix.setValues(floatArray);
        postInvalidate();
    }

    public float[] exportMatrixValues(){
        float[] floats = new float[9];
        mMatrix.getValues(floats);
        return floats;
    }

    public void setMaxZoom(float mMaxZoom) {
        Logf.d(LOG_TAG, "setMaxZoom(%f)", mMaxZoom);
        this.mMaxZoom = mMaxZoom;
        this.mMidZoom = mMaxZoom * 2.0f / 3.0f;
    }
}
