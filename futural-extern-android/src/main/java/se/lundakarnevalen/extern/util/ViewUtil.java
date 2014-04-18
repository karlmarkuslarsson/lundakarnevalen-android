package se.lundakarnevalen.extern.util;

import android.view.View;

/**
 * Created by Filip on 2014-04-18.
 */
public class ViewUtil {

    public static <T> T get(View parent, int id, Class<T> clz) {
        return clz.cast(parent.findViewById(id));
    }

}

