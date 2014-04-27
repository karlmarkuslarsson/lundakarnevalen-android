package se.lundakarnevalen.extern.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Picture;
import android.graphics.Rect;
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

    public static final boolean DEBUG = true;

    public static final float MAX_ZOOM = 250.0f;

    private float[] values = new float[9];

    private Matrix inverseMatrix;
    private Matrix savedMatrix;
    protected Matrix matrix;

    private float mScaleFactor;

    protected Picture pic;

    private ScaleGestureDetector mScaleDetector;
    private GestureDetector mGestures;

    private float lastFocusY;
    private float lastFocusX;
    protected float mInitScale;

    protected Rect currentViewBound;

    protected final float[] picEndPoint = new float[]{-1f, -1f};
    protected final float[] viewEndPoint = new float[]{-1f, -1f};


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
        matrix = new Matrix();
        savedMatrix = new Matrix();
        inverseMatrix = new Matrix();
        pic = new Picture();
        currentViewBound = new Rect();
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        mGestures = new GestureDetector(getContext(), new GestureListener());
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

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            lastFocusX = detector.getFocusX();
            lastFocusY = detector.getFocusY();
            //lastScale = detector.getScaleFactor();
            savedMatrix.set(matrix);
            mScaleFactor = 1;
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            savedMatrix.set(matrix); // on end we set the new matrix to default
        }

        Matrix transformationMatrix = new Matrix();
        float scale = 1f;

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scale = detector.getScaleFactor();

            //mScaleFactor *= Math.max(1.005f, Math.min(scale, 0.995f));
            mScaleFactor *= scale;

            matrix.getValues(values);
            if(mInitScale >= values[MSCALE_X] && scale < 1f){
                return false;
            }

            float focusX = detector.getFocusX();
            float focusY = detector.getFocusY();

            //Zoom focus is where the fingers are centered,
            transformationMatrix.reset();
            transformationMatrix.postTranslate(-focusX, -focusY);
            transformationMatrix.postScale(mScaleFactor, mScaleFactor);

            /*
                Adding focus shift to allow for scrolling with two pointers down.
                Remove it to skip this functionality.
                This could be done in fewer lines, but for clarity I do it this way here
            */

            float focusShiftX = focusX - lastFocusX;
            float focusShiftY = focusY - lastFocusY;

            transformationMatrix.postTranslate(focusX + focusShiftX, focusY + focusShiftY);

            matrix.set(savedMatrix);
            matrix.postConcat(transformationMatrix);

            lastFocusX = focusX;
            lastFocusY = focusY;
            return true;
        }
    }

    private static float limit(float min, float value, float max){
        return Math.max(Math.min(value, max), min);
    }

    public float[] getPictureXYbyScreenXY(float x, float y){
        matrix.invert(inverseMatrix);
        final float[] pts = {x, y};
        inverseMatrix.mapPoints(pts);
        return pts;
    }

    private class GestureListener implements GestureDetector.OnGestureListener {
        @Override
        public boolean onScroll(MotionEvent downEvent, MotionEvent currentEvent, float distanceX, float distanceY) {
            matrix.postTranslate(-distanceX, -distanceY);
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
            if(DEBUG) Logf.d(LOG_TAG, "screen(%f,%f) => svg(%f,%f)\nMatrix: %s", e.getX(), e.getY(), ps[0], ps[1], matrix.toShortString());
            return onClick(ps[0], ps[1]);
        }
    }

    protected boolean onClick(float xInSvg, float yInSvg) {
        return false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        filterMatrix(matrix);
        canvas.concat(matrix);
        canvas.getClipBounds(currentViewBound);
        canvas.drawPicture(pic);
        onDrawObjects(canvas);
        canvas.restore();
    }

    protected void onDrawObjects(Canvas canvas) {
        //Nothing here yet, extend in subclass
    }

    private void filterMatrix(Matrix matrix) {
        matrix.getValues(values);

        // screenW - svgW * scale is lower limit
        values[MTRANS_X] = limit(viewEndPoint[0] - picEndPoint[0] * values[MSCALE_X], values[MTRANS_X], 0);
        values[MTRANS_Y] = limit(viewEndPoint[1] - picEndPoint[1] * values[MSCALE_Y], values[MTRANS_Y], 0);

        values[MSCALE_X] = limit(mInitScale, values[MSCALE_X], MAX_ZOOM);
        values[MSCALE_Y] = limit(mInitScale, values[MSCALE_Y], MAX_ZOOM);

        matrix.setValues(values);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        updateViewBounds();
    }

    public void updateViewBounds() {
        this.viewEndPoint[0] = getMeasuredWidth();
        this.viewEndPoint[1] = getMeasuredHeight();
    }

    public void setSvg(Picture svg, float preferredZoom) {
        this.pic = svg;
        this.picEndPoint[0] = svg.getWidth();
        this.picEndPoint[1] = svg.getWidth();
        this.mInitScale = preferredZoom;
        this.matrix.setScale(mInitScale, mInitScale);
        //this.matrix.preTranslate(0f, 100f);
        postInvalidate();
    }

    public void importMatrixValues(float[] floatArray) {
        matrix.setValues(floatArray);
    }

    public float[] exportMatrixValues(){
        float[] floats = new float[9];
        matrix.getValues(floats);
        return floats;
    }
}
