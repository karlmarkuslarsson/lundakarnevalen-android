package se.lundakarnevalen.extern.map;

import android.content.Context;
import android.graphics.Picture;
import android.os.AsyncTask;
import android.util.Log;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import se.lundakarnevalen.extern.android.R;
import se.lundakarnevalen.extern.fragments.TrainMapFragment;
import se.lundakarnevalen.extern.util.Timer;

/**
* Created by Filip on 2014-05-07.
*/
public class TrainMapLoader implements Callable<Picture> {
    public static final String LOG_TAG = TrainMapLoader.class.getSimpleName();
    private static FutureTask<Picture> preloaded = null;

    private Context c;

    public TrainMapLoader(Context c) {
        this.c = c;
    }

    public static Future<Picture> preload(Context c) {
        if(preloaded == null){
            preloaded = new FutureTask<Picture>(new TrainMapLoader(c));
            new AsyncTask<Void,Void,Void>(){
                @Override
                protected Void doInBackground(Void... params) {
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
            SVG svg = SVG.getFromResource(c, R.raw.train_map_longer);
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
