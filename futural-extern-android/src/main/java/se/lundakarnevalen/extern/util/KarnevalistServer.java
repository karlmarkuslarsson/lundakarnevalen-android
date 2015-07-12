package se.lundakarnevalen.extern.util;


import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;


public class KarnevalistServer {
    private static final String LOG_TAG = KarnevalistServer.class.getSimpleName();
    private static final String DEFAULT_CONTENT_TYPE = "application/json; charset=utf-8";
    private static final String REMOTE_ADR = "http://www.karnevalist.se/";

    private final Context context;

    /**
     * Creates remote object and sets a listener for the result
     * @param context Application context
     */
    public KarnevalistServer(Context context){
        this.context = context;
    }

    /**
     * Checks if device is connected to internet.
     * @return True if internet is available, otherwise false.
     */
    public boolean hasInternetConnection() {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if(connectivity != null){
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if(info != null) {
                    for (NetworkInfo anInfo : info) {
                        if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                            return true;
                        }
                    }
                }
            }
        } catch (Throwable e){
            Log.wtf(LOG_TAG, "Failed to check hasInternetConnection()", e);
        }
        return false;
    }

    /**
     * Do a HTTP POST to the server for a plain/text response, use TextResultListener to get response.
     * @param file The file
     * @param data The data
     */
    public void requestServerForText(String file, String data, RequestType type, TextResultListener listener){
        if(hasInternetConnection()) {
            Log.d(LOG_TAG, "Sending request to server: "+file);
            new ServerTextTask(file, data, type, listener).execute();
        } else {
            Log.e(LOG_TAG, "No internet connection");
        }
    }


    private static class ServerTextTask extends AsyncTask<Void, Void, String> {

        private final String file;
        private final String data;
        private final RequestType type;
        private final TextResultListener textResultListener;

        private ServerTextTask(String file, String data, RequestType type, TextResultListener textResultListener) {
            this.file = file;
            this.data = data;
            this.type = type;
            this.textResultListener = textResultListener;
        }

        @Override
        protected String doInBackground(Void... params) {
            final StringBuilder result = new StringBuilder();
            final boolean write = (type != RequestType.GET);

            BufferedReader br = null;
            HttpURLConnection con = null;
            OutputStreamWriter dos = null;

            try {
                URL url = new URL(REMOTE_ADR + file);
                con = (HttpURLConnection) url.openConnection();

                con.setRequestMethod(type.name());
                con.setRequestProperty("Content-Type", DEFAULT_CONTENT_TYPE);
                con.setRequestProperty("Charset", "UTF-8");
                con.setUseCaches(false);
                con.setDoInput(true);
                con.setDoOutput(write);
                con.connect();

                if(write) {
                    dos = new OutputStreamWriter(con.getOutputStream());
                    dos.write(data);
                    dos.flush();
                }

                Log.d(LOG_TAG, "Response: "+con.getResponseCode());
                br = new BufferedReader(new InputStreamReader((InputStream) con.getContent()));

                String line;
                while((line = br.readLine()) != null){
                    result.append(line);
                }

            } catch (IOException e) {
                Log.w(LOG_TAG, "Failed to execute server query: " + toString(), e);
            } catch (Throwable e){
                Log.wtf(LOG_TAG, "Failed to execute server query: "+toString(), e);
            } finally {
                try {
                    if (br != null) {
                        br.close();
                    }
                    if (dos != null) {
                        dos.close();
                    }
                    if (con != null) {
                        con.disconnect();
                    }
                } catch (Throwable e) {
                    Log.wtf(LOG_TAG, "Failed to close streams", e);
                }
            }

            return result.toString();
        }

        @Override
        protected void onPostExecute(String result){
            if(result == null) {
                Log.e(LOG_TAG, "Warning result was null");
            }

            if(textResultListener != null){
                textResultListener.onResult(result);
            }
        }

        @Override
        protected void onCancelled(String result){
            if(result == null)
                Log.e(LOG_TAG, "WARNING - THE CALL WAS CANCELED");
            if(textResultListener != null){
                textResultListener.onResult(result);
            }
        }

    }

    /**
     * Interface for callback on text result from server call.
     */
    public interface TextResultListener {
        String LOG_TAG = TextResultListener.class.getSimpleName();
        void onResult(String result);
    }

    public enum RequestType {
        POST, GET, PUT, DELETE
    }

}
