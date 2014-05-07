package se.lundakarnevalen.extern.map;

import android.content.Context;
import android.graphics.Picture;
import android.util.Log;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;

import java.util.concurrent.Callable;

import se.lundakarnevalen.extern.android.R;
import se.lundakarnevalen.extern.util.Timer;

/**
* Created by Filip on 2014-05-07.
*/
public class LKMapSvgLoader implements Callable<Picture> {
    public static final String LOG_TAG = LKMapSvgLoader.class.getSimpleName();

    private Context c;

    public LKMapSvgLoader(Context c) {
        this.c = c;
    }

    @Override
    public Picture call() throws Exception {
        try {
            Timer t = new Timer();
            SVG svg = SVG.getFromResource(c, R.raw.kartamindre_cleaned);
            t.tick(LOG_TAG, "getFromResource()");
            Picture pic = svg.renderToPicture();
            t.tick(LOG_TAG, "renderToPicture()");
            return pic;
        } catch (SVGParseException e) {
            Log.wtf(LOG_TAG, "This wont happen");
            return null;
        }
    }
}
