package se.lundakarnevalen.extern.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;

public class ViewUtil {

    public static <T> T get(View parent, int id, Class<T> clz) {
        return clz.cast(parent.findViewById(id));
    }

    public static int dpToPx(Context c, int dp) {
        DisplayMetrics displayMetrics = c.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static int dpToPx(Context c, float dp) {
        DisplayMetrics displayMetrics = c.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }
}

