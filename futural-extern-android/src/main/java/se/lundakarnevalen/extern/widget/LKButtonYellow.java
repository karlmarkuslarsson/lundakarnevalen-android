package se.lundakarnevalen.extern.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.Button;

import se.lundakarnevalen.extern.android.R;

public class LKButtonYellow extends Button {
    private Context context;

    public LKButtonYellow(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public LKButtonYellow(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public LKButtonYellow(Context context, AttributeSet attrs, int defstyle) {
        super(context, attrs, defstyle);
        this.context = context;
        init();
    }

    private void init() {
        final int pxPadding = 20;
        setPadding(pxPadding, pxPadding, pxPadding, pxPadding);
        setBackgroundResource(R.drawable.song_button_selector_yellow);
        setTextColor(Color.WHITE);
    }
}
