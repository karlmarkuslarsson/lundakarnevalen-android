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
        if (!isInEditMode()) {
            switch (defStyle){
                case Typeface.BOLD:
                    setTypeface(Typeface.createFromAsset(context.getAssets(), "src/main/assets2/fonts/Futura-Bold.ttf"));
                    break;
                case Typeface.NORMAL:
                    setTypeface(Typeface.createFromAsset(context.getAssets(), "src/main/assets2/fonts/FuturaStd-Medium.ttf"));
                    break;
                case Typeface.ITALIC:
                    setTypeface(Typeface.createFromAsset(context.getAssets(), "src/main/assets2/fonts/FuturaStd-Light.ttf"));
                    break;
            }
        }
    }
}