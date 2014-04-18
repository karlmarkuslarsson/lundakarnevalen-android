package se.lundakarnevalen.extern.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Custom textview with custom font. 
 *
 */
public class LKTextView extends TextView {

    public LKTextView(Context context) {
        super(context);
    }

    public LKTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LKTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setTypeface(Typeface tf, int style) {
        if(isInEditMode()) return;
        switch (style){
            case Typeface.BOLD:
                super.setTypeface(Typeface.createFromAsset(super.getContext().getAssets(), "fonts/FuturaStd-Light.ttf"));
                break;
            case Typeface.NORMAL:
                super.setTypeface(Typeface.createFromAsset(super.getContext().getAssets(), "fonts/Futura-Bold.ttf"));
                //super.setTypeface(Typeface.createFromAsset(super.getContext().getAssets(), "fonts/FuturaStd-Medium.ttf"));
                break;
            case Typeface.ITALIC:
                super.setTypeface(Typeface.createFromAsset(super.getContext().getAssets(), "fonts/FuturaStd-Light.ttf"));
                //super.setTypeface(Typeface.createFromAsset(super.getContext().getAssets(), "fonts/FuturaStd-Light.ttf"));
                break;
        }
    }
}