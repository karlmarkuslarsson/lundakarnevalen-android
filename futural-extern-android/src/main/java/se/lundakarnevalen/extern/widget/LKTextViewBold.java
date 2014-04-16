package se.lundakarnevalen.extern.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Custom textview with custom font. 
 *
 */
public class LKTextViewBold extends TextView {
    Context context;

    public LKTextViewBold(Context context) {
        super(context);
        this.context = context;
        font();

    }

    public LKTextViewBold(Context context, AttributeSet attrs){
        super(context, attrs);
        this.context = context;
        font();
    }

    public LKTextViewBold(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        this.context = context;
        font();
    }

    private void font(){
        try {
            Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/Futura-Bold.ttf");
            setTypeface(tf);

        } catch (Exception e) {
        }
    }
}