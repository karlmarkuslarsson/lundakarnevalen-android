package se.lundakarnevalen.extern.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Picture;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import se.lundakarnevalen.extern.util.Logf;

import static android.graphics.Matrix.*;

/**
 * Created by Filip on 2014-04-23.
 */
public class SVGView extends View {
    private static final String LOG_TAG = SVGView.class.getSimpleName();

    public static final boolean DEBUG = false;

    public static final float MAX_ZOOM = 50.0f;

    private Matrix mInverseMatrix;
    private Matrix mSavedMatrix;

    private ScaleGestureDetector mScaleDetector;
    private GestureDetector mGestures;

    private float[] mMatrixValues = new float[9];
    private float mScaleFactor;
    private float mLastFocusY;
    private float mLastFocusX;

    protected float mMinZoom;
    protected Matrix mMatrix;
    protected Picture mPicture;
    protected Rect mCurrentViewBound;

    protected final float[] mPicEndPoint = new float[]{-1f, -1f};
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

    public void init(Context context){
        mMatrix = new Matrix();
        mSavedMatrix = new Matrix();
        mInverseMatrix = new Matrix();
        mCurrentViewBound = new Rect();
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
        // Let the ScaleGestureDetector inspect all events first.
        boolean result = mScaleDetector.onTouchEvent(ev);
        if(!mScaleDetector.isInProgress()){
            result |= mGestures.onTouchEvent(ev);
        }
        invalidate();
        return result;
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

            //mScaleFactor *= Math.max(1.005f, Math.min(mTempScale, 0.995f));
            mScaleFactor *= mTempScale;

            float focusX = detector.getFocusX();
            float focusY = detector.getFocusY();

            mMatrix.getValues(mMatrixValues);

            if(mMinZoom >= mMatrixValues[MSCALE_X] * mScaleFactor){
                mLastFocusX = focusX;
                mLastFocusY = focusY;
                mScaleFactor /= mTempScale;
            }

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

            float focusShiftX = focusX - mLastFocusX;
            float focusShiftY = focusY - mLastFocusY;

            mTransformationMatrix.postTranslate(focusX + focusShiftX, focusY + focusShiftY);

            mMatrix.set(mSavedMatrix);
            mMatrix.postConcat(mTransformationMatrix);

            mLastFocusX = focusX;
            mLastFocusY = focusY;
            return true;
        }
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

    private boolean scroll(float distanceX, float distanceY) {
        return mMatrix.postTranslate(-distanceX, -distanceY);
    }

    @Override
    public void scrollBy(int x, int y) {
        scroll(x, y); //Implicit cast to float here
    }

    protected boolean onClick(float xInSvg, float yInSvg) {
        return false;
    }

    @Override
    protected final void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        filterMatrix(mMatrix);
        canvas.concat(mMatrix);
        canvas.getClipBounds(mCurrentViewBound);
        onDrawObjects(canvas);
        canvas.restore();
    }

    protected void onDrawObjects(Canvas canvas) {
        canvas.drawPicture(mPicture);
        //Nothing here yet, extend in subclass
    }

    private void filterMatrix(Matrix matrix) {
        matrix.getValues(mMatrixValues);

        // screenW - svgW * scale is lower limit
        mMatrixValues[MTRANS_X] = limit(mViewEndPoint[0] - mPicEndPoint[0] * mMatrixValues[MSCALE_X], mMatrixValues[MTRANS_X], 0);
        mMatrixValues[MTRANS_Y] = limit(mViewEndPoint[1] - mPicEndPoint[1] * mMatrixValues[MSCALE_Y], mMatrixValues[MTRANS_Y], 0);

        mMatrixValues[MSCALE_X] = limit(mMinZoom, mMatrixValues[MSCALE_X], MAX_ZOOM);
        mMatrixValues[MSCALE_Y] = limit(mMinZoom, mMatrixValues[MSCALE_Y], MAX_ZOOM);

        matrix.setValues(mMatrixValues);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        updateViewLimitBounds();
    }

    public boolean updateViewLimitBounds() {
        final int w = getMeasuredWidth();
        final int h = getMeasuredHeight();
        if(w > 0 && h > 0) { //Ignore if layout is not calculated yet
            this.mViewEndPoint[0] = w;
            this.mViewEndPoint[1] = h;
            return true;
        } else {
            return false;
        }
    }

    public void setSvg(Picture svg, float minZoom, float[] values) {
        this.mPicture = svg;
        this.mPicEndPoint[0] = svg.getWidth();
        this.mPicEndPoint[1] = svg.getWidth();
        this.mMinZoom = minZoom;


        // Scale image
        float initZoom = mMinZoom * 2f;
        if (values != null) {
            this.mMatrix.setValues(values);
        } else {
            this.mMatrix.setScale(initZoom, initZoom);

            // Center image
            if(updateViewLimitBounds()) {
                float cx = (initZoom * mPicEndPoint[0] - mViewEndPoint[0]) / -2.0f;
                float cy = (initZoom * mPicEndPoint[1] - mViewEndPoint[1]) / -2.0f;
                mMatrix.postTranslate(cx, cy);
                Logf.d(LOG_TAG, "Matrix translated to center picture, translate(%f, %f)", cx, cy);
            }
        }



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
}
