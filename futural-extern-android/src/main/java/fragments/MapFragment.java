package fragments;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.*;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import se.lundakarnevalen.extern.android.R;

import static android.location.LocationManager.*;

public class MapFragment extends LKFragment  {
    // Save current dots
    private Bitmap bmOverlay;
    private ImageView img;
    private int imageWidth;
    private int imageHeight;

    // Context
    private Context context;

    // For gps and network
    private LocationManager locMan;
    private static final int TIME_INTERVAL = 1800000; // get gps location every 30 min
    // private static final int TIME_INTERVAL = 10000; // get gps location every 10 sec
    private static final int GPS_DISTANCE = 0; // set the distance value in meter


    // Information about the map
    private float startLonMap = (float) 12.445449839578941;
    private float startLatMap = (float) 55.33715099913018;
    private float endLonMap = (float) 14.580917368875816;
    private float endLatMap = (float) 56.52300194685981;
    private float diffLon = endLonMap - startLonMap;
    private float diffLat = endLatMap - startLatMap;


    // Every time you switch to this fragment.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, null);


        context = getContext();


        if(imageWidth == 0) {
            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            imageWidth = metrics.widthPixels;
            imageHeight = metrics.heightPixels;
        }

        //img = (ImageView) rootView.findViewById(R.id.map_id);
        if (bmOverlay != null) {
          //  ((ImageView) rootView.findViewById(R.id.map_id)).setImageBitmap(bmOverlay);
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void updatePositions() {
        Bitmap mapBitmap = null;
     //       mapBitmap = decodeSampledBitmapFromResource(getResources(), R.drawable.map_skane, imageWidth , imageHeight);

            // Create an overlay bitmap
            bmOverlay = Bitmap.createBitmap(mapBitmap.getWidth(), mapBitmap.getHeight(), mapBitmap.getConfig());
            Canvas canvas = new Canvas();
            canvas.setBitmap(bmOverlay);
            canvas.drawBitmap(mapBitmap, new Matrix(), null);

            Paint paintGray = new Paint();
            Paint paintRed = new Paint();
            //paintRed.setColor(getResources().getColor(R.color.red));
            paintGray.setColor(Color.GRAY);
            float lat1 = 12;
            float lng1 = 123;
                float lat = (lat1 - startLatMap) / diffLat;
                float lon = (lng1 - startLonMap) / diffLon;
                float x = lon * mapBitmap.getWidth();
                float y = mapBitmap.getHeight() - lat * mapBitmap.getHeight();
                canvas.drawCircle(x, y, 1, paintRed);

           // img.setImageBitmap(bmOverlay);

    }

    public void getPosition() {
            if(locMan==null) {
                locMan = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            }
            // Only turn off get position with GPS. Ok with network...
            if(locMan.isProviderEnabled(GPS_PROVIDER)){
                locMan.requestLocationUpdates(GPS_PROVIDER, TIME_INTERVAL, GPS_DISTANCE, PositionListener);
                Log.d("Updateing GPS!", "Update");
            } else {
                Log.d("GPS off", "Avst�ngd GPS");
                if (locMan.isProviderEnabled(NETWORK_PROVIDER)) {
                    locMan.requestLocationUpdates(NETWORK_PROVIDER, TIME_INTERVAL, GPS_DISTANCE, PositionListener);
                    Log.d("Updateing Position with network!", "Update");
                }
            }
            float lng;
            float lat;
            Location location = locMan.getLastKnownLocation(GPS_PROVIDER);
            if (location != null) {
                lng = (float) location.getLongitude();
                lat = (float) location.getLatitude();
                Log.d("Find GPS_position", lng + " " + lat);
            } else {
                location = locMan.getLastKnownLocation(NETWORK_PROVIDER);
                if (location != null) {
                    lng = (float) location.getLongitude();
                    lat = (float) location.getLatitude();
                    Log.d("Find Network_position", lng + " " + lat);
                } else {
                    Log.d("No GPS or Network position", "FAIL1");
                }
            }


    }

    private LocationListener PositionListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            // update location
            locMan.removeUpdates(PositionListener); // remove this listener
        }
        public void onProviderDisabled(String provider) {}
        public void onProviderEnabled(String provider) {}
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    };

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }


    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }
}