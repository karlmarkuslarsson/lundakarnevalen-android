package se.lundakarnevalen.extern.map;

import android.content.Context;
import android.graphics.Picture;
import android.os.AsyncTask;
import android.util.Log;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import se.lundakarnevalen.extern.android.R;
import se.lundakarnevalen.extern.util.Timer;

/**
* Created by Filip on 2014-05-07.
*/
public class MapLoader implements Callable<Picture> {
    public static final String LOG_TAG = MapLoader.class.getSimpleName();
    private static FutureTask<Picture> preloaded = null;

    private Context c;

    public MapLoader(Context c) {
        this.c = c;
    }

    public static FutureTask<Picture> preload(Context c) {
        if(preloaded == null){
            preloaded = new FutureTask<Picture>(new MapLoader(c));
            new AsyncTask<Void,Void,Void>(){
                @Override
                protected Void doInBackground(Void... params) {
                    if (preloaded == null) { // if async starts after cleanup
                        return null;
                    }
                    preloaded.run();
                    return null;
                }
            }.execute();
        }
        return preloaded;
    }

    public static void clean(){
        preloaded = null;
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
