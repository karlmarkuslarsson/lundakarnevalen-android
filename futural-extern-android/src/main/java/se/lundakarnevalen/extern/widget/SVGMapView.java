package se.lundakarnevalen.extern.widget;

import android.content.Context;
import android.graphics.BitmapFactory;
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

import se.lundakarnevalen.extern.android.R;
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

            // Don't let the object get too small or too large.
            // mScaleFactor = Math.max(0.8f, Math.min(mScaleFactor, 2.0f));

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
        public void onLongPress(MotionEvent e) {}

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
        canvas.concat(matrix);
        canvas.getClipBounds(r);
        canvas.drawPicture(pic);
        canvas.restore();
    }

    private void checkClick(float relativeX, float relativeY) {

    }

    public void setSvg(Picture svg, int w, int h) {
        this.pic = svg;
        float initScale = w * 1.0f / pic.getWidth();
        this.matrix.setScale(initScale, initScale);
        this.matrix.preTranslate(0f, 100f);
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
