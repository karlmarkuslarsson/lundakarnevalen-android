package se.lundakarnevalen.extern.map;

import android.content.Context;
import android.graphics.Picture;
import android.os.AsyncTask;
import android.util.Log;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;

import java.util.concurrent.TimeUnit;

import se.lundakarnevalen.extern.android.R;
import se.lundakarnevalen.extern.util.Logf;
import se.lundakarnevalen.extern.util.Timer;

/**
* Created by Filip on 2014-05-07.
*/
public class TrainMapLoader {
    public static final String LOG_TAG = MapLoader.class.getSimpleName();
    private static boolean isLoading = false;

    private static Picture mapTrain = null;

    public static Picture getMapTrain() { return mapTrain; }

    public static boolean hasLoadedMap(){
        return mapTrain != null;
    }

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
                Timer t = new Timer();
                if(isLoading) return null;

                isLoading = true;
                SVG svg1 = SVG.getFromResource(mContext, R.raw.train_map_longer);
                t.tick(LOG_TAG, "MapTrain: getFromResource()");
                mapTrain = svg1.renderToPicture();
                t.tick(LOG_TAG, "MapTrain: renderToPicture()");
                Logf.d(LOG_TAG, "MapTrain: svg %f x %f, pic %d x %d",
                        svg1.getDocumentWidth(), svg1.getDocumentHeight(),
                        mapTrain.getWidth(), mapTrain.getHeight());

            } catch (SVGParseException e) {
                Log.wtf(LOG_TAG, "This wont happen");
            } finally {
                isLoading = false;
            }
            return null;
        }
    }

    public interface MapLoaderCallback {
        void postTrainMap(Picture picture);
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
                        while (mapTrain == null) sleep(200);
                        Log.d(LOG_TAG, "posting TrainMap: "+mapTrain);
                        mCallback.postTrainMap(mapTrain);
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