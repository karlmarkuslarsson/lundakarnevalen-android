package se.lundakarnevalen.extern.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Picture;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import se.lundakarnevalen.extern.util.Logf;
import se.lundakarnevalen.extern.util.Timer;

/**
 * Created by Filip on 2014-04-23.
 */
public class SVGMapView extends View {

    private static final String LOG_TAG = SVGMapView.class.getSimpleName();
    private static final int INVALID_POINTER_ID = -1;

    private Matrix savedMatrix;
    private Matrix matrix;

    private float mScaleFactor;

    private Rect r = new Rect();
    private Picture pic = new Picture();
    private Timer timer = new Timer();

    private ScaleGestureDetector mScaleDetector;
    private GestureDetector mGestures;

    private float lastFocusY;
    private float lastFocusX;
    private float mInitScale;

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
        savedMatrix = new Matrix();
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

    private static float range(float min, float value, float max){
        return 1f;
    }

    private class GestureListener implements GestureDetector.OnGestureListener {
        @Override
        public boolean onScroll(MotionEvent downEvent, MotionEvent currentEvent, float distanceX, float distanceY) {
            matrix.postTranslate(-distanceX, -distanceY);
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            Log.d(LOG_TAG, matrix.toShortString());
            //matrix.invert(inv);
            matrix.mapPoints(mappedEndPoint, endPoint);
            Logf.d(LOG_TAG, "%f, %f => %f, %f", endPoint[0], endPoint[1], mappedEndPoint[0], mappedEndPoint[1]);

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {return false;}

        @Override
        public boolean onDown(MotionEvent e) {return false;}

        @Override
        public void onShowPress(MotionEvent e) {}

        @Override
        public boolean onSingleTapUp(MotionEvent e) {return false;}
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        filterMatrix(matrix);
        canvas.concat(matrix);
        canvas.getClipBounds(r);
        canvas.drawPicture(pic);
        canvas.restore();
    }

    private float[] values = new float[9];
    private Matrix inv = new Matrix();
    private final float[] endPoint = new float[]{512f, 512f};
    private float[] mappedEndPoint = new float[]{0.0f, 0.0f};

    private void filterMatrix(Matrix matrix) {
        matrix.getValues(values);

        //matrix.invert(inv);
        matrix.mapPoints(mappedEndPoint, endPoint);

        values[2] = Math.max(Math.min(values[2], 0), -1.0f * mappedEndPoint[0]);
        values[5] = Math.max(Math.min(values[5], 0), -1.0f * mappedEndPoint[1]);

        //values[2] = Math.min(values[2], 0);
        //values[5] = Math.min(values[5], 0);

        values[0] = Math.max(Math.min(values[0], 250.0f), mInitScale);
        values[4] = Math.max(Math.min(values[4], 250.0f), mInitScale);

        matrix.setValues(values);
    }

    private void checkClick(float relativeX, float relativeY) {

    }

    public void setSvg(Picture svg, int w, int h) {
        this.pic = svg;
        this.mInitScale = w * 2f / pic.getWidth();
        this.matrix.setScale(mInitScale, mInitScale);
        this.endPoint[0] = pic.getWidth();
        this.endPoint[1] = pic.getHeight();
        //this.matrix.preTranslate(0f, 100f);
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
