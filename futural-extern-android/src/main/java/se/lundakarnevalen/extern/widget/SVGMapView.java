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
import android.view.MotionEvent;
import android.view.View;

import com.caverock.androidsvg.SVG;

import se.lundakarnevalen.extern.util.Timer;

/**
 * Created by Filip on 2014-04-23.
 */
public class SVGMapView extends View implements View.OnTouchListener {

    private static final String LOG_TAG = SVGMapView.class.getSimpleName();
    private boolean firstTime;

    private Matrix matrix;
    private Matrix savedMatrix;

    // Variables for zooming
    private PointF start = new PointF();
    private PointF mid = new PointF();
    private float oldDist = 1f;
    private float newDist = 1f;

    // Control scale 1 = full size
    private float scale = 1f;

    // States onTouchEvent
    private final int NONE = 0;
    private int mode = NONE;
    private final int DRAG = 1;
    private final int ZOOM = 2;

    private SVG svg = null;
    private Rect r = new Rect();
    private Picture pic = new Picture();
    private Timer timer = new Timer();

    public SVGMapView(Context context) {
        super(context);
        init();
    }

    public SVGMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SVGMapView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void init(){
        matrix = savedMatrix = new Matrix();
        firstTime = true;
        setOnTouchListener(this);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        firstTime = false;
        float scale;

        // Handle touch events here...
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:   // first finger down only
                savedMatrix.set(matrix);
                start.set(event.getX(), event.getY());
                mode = DRAG;
                break;

            case MotionEvent.ACTION_UP: // first finger lifted

            case MotionEvent.ACTION_POINTER_UP: // second finger lifted
                if (mode == ZOOM) {
                    this.scale = this.scale * newDist / oldDist;
                    // generateDots(this.scale);
                } else {

                    if ((start.x - event.getX()) * (start.x - event.getX()) + (start.y - event.getY()) * (start.y - event.getY()) < 10) {
                        float[] values = new float[9];
                        matrix.getValues(values);
                        // values[2] and values[5] are the x,y coordinates of the top left corner of the drawable image, regardless of the zoom factor.
                        // values[0] and values[4] are the zoom factors for the image's width and height respectively. If you zoom at the same factor, these should both be the same value.
                        float relativeX = (event.getX() - values[2]) / values[0];
                        float relativeY = (event.getY() - values[5]) / values[4];
                        Log.d(LOG_TAG, "Relative: x: " + relativeX + " y: " + relativeY);
                        // values[2] and values[5] are the x,y coordinates of the top left corner of the drawable image, regardless of the zoom factor.
                        checkClick(relativeX, relativeY);
                    } else {

                    }
                }
                mode = NONE;
                // Uppdatera mapen...
                break;

            case MotionEvent.ACTION_POINTER_DOWN: // first and second finger down

                oldDist = spacing(event);
                newDist = oldDist;
                if (oldDist > 5f) {
                    savedMatrix.set(matrix);
                    midPoint(mid, event);
                    mode = ZOOM;
                }
                break;

            case MotionEvent.ACTION_MOVE:

                if (mode == DRAG) {
                    matrix.postTranslate(event.getX() - start.x, event.getY() - start.y); // create the transformation in the matrix  of points
                } else if (mode == ZOOM) {
                    // pinch zooming
                    float newDist2 = spacing(event);
                    // Lock zoom out
                    if (this.scale * newDist2 / oldDist >= 1) {
                        //newDist = newDist2;
                        newDist = newDist2;
                        if (newDist > 5f) {

                            scale = newDist / oldDist;
                            // setting the scaling of the
                            // matrix...if scale > 1 means
                            // zoom in...if scale < 1 means
                            // zoom out
                            matrix.postScale(scale, scale, mid.x, mid.y);
                        }
                    }
                }
                break;
        }
        invalidate();
        return true; // indicate event was handled
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        timer.reset();
        canvas.setMatrix(matrix);
        canvas.getClipBounds(r);
        canvas.drawPicture(pic);
        timer.tick(LOG_TAG, "draw(): " + r);
    }

    /**
     * Calculate the space between the two fingers on touch
     */
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * Calculates the midpoint between the two fingers
     */
    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    private void checkClick(float relativeX, float relativeY) {

    }

    public void setSvg(SVG svg, int w, int h, int dpi) {
        this.svg = svg;
        Timer t = new Timer();
        this.pic = svg.renderToPicture();
        float initScale = getWidth() * 1.0f / pic.getWidth();
        this.matrix.setScale(initScale, initScale);
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
