package se.lundakarnevalen.extern.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class LKActionBarTextView extends TextView {

    public LKActionBarTextView(Context context) {
        super(context);
        init();
    }

    public LKActionBarTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LKActionBarTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void init() {
        if(isInEditMode()) return;
        setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/karneval_font.ttf"));


    }
}