package se.lundakarnevalen.extern.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Custom textview with custom font. 
 *
 */
public class LKActionBarTextView extends TextView {

    public LKActionBarTextView(Context context) {
        super(context);
    }

    public LKActionBarTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LKActionBarTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setTypeface(Typeface tf, int style) {
        if(isInEditMode()) return;
        super.setTypeface(Typeface.createFromAsset(super.getContext().getAssets(), "fonts/Robot!Head.ttf"));


    }
}