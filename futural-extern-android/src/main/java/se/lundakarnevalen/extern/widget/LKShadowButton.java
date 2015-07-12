package se.lundakarnevalen.extern.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.Button;

public class LKShadowButton extends Button {

    public LKShadowButton(Context context) {
        super(context);
    }

    public LKShadowButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LKShadowButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LKShadowButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private Rect mRect = new Rect();
    private Paint mPaint = new Paint();

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.getClipBounds(mRect);

        mRect.top = mRect.bottom - 12;
        mPaint.setARGB(50, 0, 0, 0);

        canvas.drawRect(mRect, mPaint);


    }
}
