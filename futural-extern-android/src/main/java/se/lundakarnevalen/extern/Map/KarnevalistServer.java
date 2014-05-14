package se.lundakarnevalen.extern.map;


import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by Markus on 2014-05-13.
 */
public class KarnevalistServer {
    public static final String remoteAdr = "http://www.karnevalist.se/";

    private Context context;
    private String contentType = "application/json; charset=utf-8";
    private TextResultListener textResultListener;

    /**
     * Creates remote object and sets a listener for the result
     * @param context Application context
     * @param textResultListener Listener called when result is returned by server.
     */
    public KarnevalistServer(Context context, TextResultListener textResultListener){
        this.context = context;
        this.textResultListener = textResultListener;
    }



    /**
     * Checks if device is connected to internet.
     * @return True if internet is available, otherwise false.
     */
    public static boolean hasInternetConnection(Context context){
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivity != null){
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if(info != null)
                for(int n = 0; n < info.length; n++){
                    if(info[n].getState() == NetworkInfo.State.CONNECTED){
                        return true;
                    }
                }
        }
        return false;
    }

    /**
     * Returns a string for the request type based on the Enum.
     * @param type The request type
     * @return The string for the request type.
     */
    private String getRequestTypeString(RequestType type){
        switch(type){
            case POST:
                return "POST";
            case GET:
                return "GET";
            case PUT:
                return "PUT";
            case DELETE:
                return "DELETE";
            default:
                return "GET";
        }
    }

    /**
     * Do a HTTP POST to the server for a plain/text response, use TextResultListener to get response.
     * @param file The file
     * @param data The data
     */
    public void requestServerForText(String file, String data, RequestType type, boolean popup){
        String requestType = getRequestTypeString(type);

        if(hasInternetConnection(context)){
            AsyncTask<String, Void, String> task = new ServerTextTask();
            task.execute(file, data, requestType);
        }else{
            Log.e("LKRemote", "no internet connection");
        }
    }


    class ServerTextTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String file, data, requestType;
            boolean write = true;
            try{
                file = params[0];
                data = params[1];
                requestType = params[2];
                if(requestType.equals(getRequestTypeString(RequestType.GET))){
                    write = false;
                }
            }catch(ArrayIndexOutOfBoundsException e){
                return null;
            }

            URL url;
            try {
                url = new URL(remoteAdr+file);
            } catch (MalformedURLException e) {
                return null;
            }
            HttpURLConnection con;
            try {
                con = (HttpURLConnection) url.openConnection();
            } catch (IOException e) {
                return null;
            }
            if(con == null){
                return null;
            }
            try {
                con.setRequestMethod(requestType);
            } catch (java.net.ProtocolException e) {
            }



            con.setRequestProperty("Content-Type", contentType);
            con.setRequestProperty("Charset", "UTF-8");

            con.setUseCaches(false);
            con.setDoInput(true);
            if(write) {
                con.setDoOutput(true);
            }

            try{
                con.connect();
            }catch(IOException e){
                Log.e("LKRemote", "Could not connect.");
                return null;
            }
            if(write){
                OutputStreamWriter dos;
                try {
                    dos = new OutputStreamWriter(con.getOutputStream());
                } catch (IOException e) {
                    return null;
                }
                try {
                    dos.write(data); // should write data.
                    dos.flush();
                    dos.close();
                } catch (IOException e) {
                    return null;
                }
            }

            InputStreamReader isr;
            try {
                Log.d("LKRemote", "Response: "+con.getResponseCode());
                isr = new InputStreamReader((InputStream) con.getContent());
            } catch (IOException e) {
                Log.e("LKRemote", "Could not open input stream to " + url.getPath() + ((con == null) ? " con was null" : " con was NOT null"+" "+	e));
                return null;
            }
            BufferedReader br = new BufferedReader(isr);
            StringBuilder result = new StringBuilder();
            String line;
            try {
                while((line = br.readLine()) != null){
                    result.append(line);
                }
            } catch (IOException e) {
                return null;
            }

            try {
                br.close();
                isr.close();
                con.disconnect();
            } catch (IOException e) {
            }
            return result.toString();
        }

        @Override
        protected void onPostExecute(String result){

            if(result == null)
                Log.e("LKRemote", "Warning result was null");

            if(textResultListener != null){
                textResultListener.onResult(result);
            }
        }

        @Override
        protected void onCancelled(String result){
            if(result == null)
                Log.e("LKRemote", "WARNING - THE CALL WAS CANCELED");
            if(textResultListener != null){
                textResultListener.onResult(result);
            }
        }

    }


    /**
     * Interface for callback on text result from server call.
     *
     */
    public interface TextResultListener{
        public static final String LOG_TAG = "Result listener";
        public void onResult(String result);
    }

    public enum RequestType{
        POST, GET, PUT, DELETE;
    }

}
