package se.lundakarnevalen.extern.map;

import android.content.Context;
import android.graphics.Picture;
import android.os.AsyncTask;
import android.util.Log;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;

import java.util.concurrent.Semaphore;

import se.lundakarnevalen.extern.android.R;
import se.lundakarnevalen.extern.util.Timer;

/**
* Created by Filip on 2014-05-07.
*/
public class MapLoader {
    public static final String LOG_TAG = MapLoader.class.getSimpleName();

    private static Semaphore mapSignal = new Semaphore(2);
    private static Semaphore mapWaitSignal = new Semaphore(1);

    private static boolean isLoading = false;
    private static Picture mapMini = null;
    private static Picture mapLarge = null;

    public static void startPreLoading(Context c) {
        new MapLoaderCallable(c).execute();
    }

    private static class MapLoaderCallable extends AsyncTask<Void, Void, Void> {
        private final Context mContext;

        private MapLoaderCallable(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                mapSignal.acquire(2);
                isLoading = true;
                Timer t = new Timer();

                SVG svg1 = SVG.getFromResource(mContext, R.raw.karta_mini);
                t.tick(LOG_TAG, "MapMini: getFromResource()");
                mapMini = svg1.renderToPicture();
                t.tick(LOG_TAG, "MapMini: renderToPicture()");

                t.reset();
                TrainMapLoader.preload(mContext);

                SVG svg2 = SVG.getFromResource(mContext, R.raw.kartamindre_cleaned);
                t.tick(LOG_TAG, "MapLarge: getFromResource()");
                mapLarge = svg2.renderToPicture();
                t.tick(LOG_TAG, "MapLarge: renderToPicture()");
                mapSignal.release();

                isLoading = false;
            } catch (SVGParseException e) {
                Log.wtf(LOG_TAG, "This wont happen");
            } catch (InterruptedException e) {
                e.printStackTrace();
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
                        Log.d(LOG_TAG, "mapWaitSignal.acquire()");
                        mapWaitSignal.acquire();
                        if(mapMini == null) {
                            mapSignal.acquire();
                        }
                        Log.d(LOG_TAG, "posting MiniMap: "+mapMini);
                        mCallback.postMiniMap(mapMini);
                        if(mapLarge == null) {
                            mapSignal.acquire();
                        }
                        Log.d(LOG_TAG, "posting LargeMap: "+mapMini);
                        mCallback.postLargerMap(mapLarge);
                        Log.d(LOG_TAG, "mapWaitSignal.release()");
                        mapWaitSignal.release();
                    } catch (InterruptedException e) {
                        Log.wtf(LOG_TAG, "Future was interrupted", e);
                    } finally {
                        if(mapMini == null) mapSignal.release();
                        if(mapLarge == null) mapSignal.release();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {}
            }.execute();
        }
    }
}
