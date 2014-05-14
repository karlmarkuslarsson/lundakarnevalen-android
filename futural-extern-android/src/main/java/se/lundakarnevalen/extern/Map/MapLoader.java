package se.lundakarnevalen.extern.map;

import android.content.Context;
import android.graphics.Picture;
import android.os.AsyncTask;
import android.util.Log;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import se.lundakarnevalen.extern.android.R;
import se.lundakarnevalen.extern.util.Logf;
import se.lundakarnevalen.extern.util.Timer;

/**
* Created by Filip on 2014-05-07.
*/
public class MapLoader {
    public static final String LOG_TAG = MapLoader.class.getSimpleName();

    private static boolean isLoading = false;

    private static Picture mapMini = null;
    private static Picture mapLarge = null;

    public static boolean hasLoadedMapMini(){
        return mapMini != null;
    }

    public static Picture getMapMini() { return mapMini; }

    public static boolean hasLoadedMapLarge(){
        return mapLarge != null;
    }

    public static void startPreLoading(Context c) {
        new MapLoaderCallable(c).execute();
    }

    public static Picture getMapLarge() {
        return mapLarge;
    }

    private static class MapLoaderCallable extends AsyncTask<Void, Void, Void> {
        private final Context mContext;

        private MapLoaderCallable(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Timer t = new Timer();
                isLoading = true;
                if(mapMini==null) {
                    SVG svg1 = SVG.getFromResource(mContext, R.raw.karta_mini);
                    t.tick(LOG_TAG, "MapMini: getFromResource()");
                    mapMini = svg1.renderToPicture();
                    t.tick(LOG_TAG, "MapMini: renderToPicture()");
                    Logf.d(LOG_TAG, "MapMini: svg %f x %f, pic %d x %d",
                            svg1.getDocumentWidth(), svg1.getDocumentHeight(),
                            mapMini.getWidth(), mapMini.getHeight());

                }
                t.reset();

                TrainMapLoader.startPreLoading(mContext);
                if(mapLarge==null) {

                    SVG svg2 = SVG.getFromResource(mContext, R.raw.kartamindre_cleaned);
                    t.tick(LOG_TAG, "MapLarge: getFromResource()");
                    mapLarge = svg2.renderToPicture();
                    t.tick(LOG_TAG, "MapLarge: renderToPicture()");
                    Logf.d(LOG_TAG, "MapLarge: svg %f x %f, pic %d x %d",
                            svg2.getDocumentWidth(), svg2.getDocumentHeight(),
                            mapLarge.getWidth(), mapLarge.getHeight());
                }
            } catch (SVGParseException e) {
                Log.wtf(LOG_TAG, "This wont happen");
            } finally {
                isLoading = false;
            }
            return null;
        }
    }

    public interface MapLoaderCallback{
        public void postMiniMap(Picture picture);
        public void postLargerMap(Picture picture);
    }

    public static class MapSvgLoader {
        private final MapLoaderCallback mCallback;

        public MapSvgLoader(MapLoaderCallback callback) {
            this.mCallback = callback;
        }

        public void startWait() {
            new AsyncTask<Void, Void, Void>(){
                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        while (mapMini == null) sleep(200);
                        Log.d(LOG_TAG, "posting MiniMap: "+mapMini);
                        mCallback.postMiniMap(mapMini);
                        while (mapLarge == null) sleep(500);
                        Log.d(LOG_TAG, "posting LargeMap: "+mapLarge);
                        mCallback.postLargerMap(mapLarge);
                    } catch (InterruptedException e) {
                        Log.wtf(LOG_TAG, "Future was interrupted", e);
                    }
                    return null;
                }

                private void sleep(int ms) throws InterruptedException {
                    TimeUnit.MILLISECONDS.sleep(ms);
                }

                @Override
                protected void onPostExecute(Void aVoid) {}
            }.execute();
        }

    }
}
