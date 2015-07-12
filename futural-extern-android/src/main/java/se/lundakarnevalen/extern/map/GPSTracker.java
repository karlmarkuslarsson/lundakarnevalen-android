package se.lundakarnevalen.extern.map;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import se.lundakarnevalen.extern.util.Logf;

public class GPSTracker extends Service implements LocationListener, GpsStatus.Listener {
    private static final String LOG_TAG = GPSTracker.class.getSimpleName();
    private static final boolean DEBUG = true;
    
    public static final int UPDATE_DELAY_MILLIS = 20000;
    public static final int INITAL_DELAY_MILLIS = 100;

    //Filter accuracy on this value in meters
    public static final int REQUERED_ACCURACY_METERS = 200;

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 100; // 1 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 20000; // 2 sec

    private final LocationManager mLocationManager;
    private final Handler mHandler;
    private final Runnable mUpdateRunnable;

    private List<GPSListener> mListeners;
    private Location mLocation;
    private double mLatitude = 55.707644;
    private double mLongitude = 13.192707;

    public void invalidateMe(GPSListener listener) {
        if (mLatitude > 0.0f && mLongitude > 0.0f ) {
            listener.onNewLocation(mLatitude, mLongitude);
        }
    }

    @Override
    public void onGpsStatusChanged(int event) {
        Logf.d(LOG_TAG, "onGpsStatusChanged(%d)", event);
    }
    public interface GPSListener {
        void onNewLocation(double lat, double lng);

    }


    public GPSTracker () {
        this.mLocationManager = null;
        this.mListeners = null;
        this.mHandler = null;
        this.mUpdateRunnable = null;
    }

    public GPSTracker(Context context) {
        this.mLocationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        this.mListeners = new ArrayList<>(2);
        this.mHandler = new Handler(context.getMainLooper());
        this.mUpdateRunnable = new Runnable() {
            @Override
            public void run() {
                updateLocation();
                mHandler.postDelayed(this, UPDATE_DELAY_MILLIS);
            }
        };
        init();
    }

    private void init() {
        if (DEBUG) return;

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        String bestProvider = mLocationManager.getBestProvider(criteria, true);
        if(bestProvider == null) {
            return;
        }
        this.mHandler.postDelayed(mUpdateRunnable, INITAL_DELAY_MILLIS);

        mLocationManager.requestLocationUpdates(bestProvider, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

        if (!LocationManager.GPS_PROVIDER.equalsIgnoreCase(bestProvider)){
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
            mLocationManager.addGpsStatusListener(this);
        }
    }

    public Location updateLocation() {
        try {
            // if GPS Enabled get lat/long using GPS Services
            if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Location l = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                if (mLocation != l) {
                    mLocation = l;
                    mLatitude = mLocation.getLatitude();
                    mLongitude = mLocation.getLongitude();
                    Logf.d(LOG_TAG, "Updated position: lat %f, lng %f, accuracy:%f", mLatitude, mLongitude, l.getAccuracy());
                    onLocationChanged(mLocation);
                }
            } else {
                Log.d(LOG_TAG, "LocationProvider: GPS disabled");
            }

        } catch (Exception e) {
            Log.wtf(LOG_TAG, "Failed to acquire location", e);
            e.printStackTrace();
        }
        return mLocation;
    }

    /**
     * Stop using GPS listener
     * Calling this function will stop using GPS in your app
     */
    public void stopUsingGPS() {
        mHandler.removeCallbacks(mUpdateRunnable);
        mLocationManager.removeUpdates(this);
    }

    public void addListener(GPSListener listener) {
        this.mListeners.add(listener);
    }

    public void removeListener(GPSListener listener) {
        this.mListeners.remove(listener);
    }

    @Override
    public void onLocationChanged(Location location) {
        final double lat = location.getLatitude();
        final double lng = location.getLongitude();
        if (location.getAccuracy() < REQUERED_ACCURACY_METERS) {

            // We only care if accuracy is good enough
            Logf.d(LOG_TAG, "(%s) Posting location: lat %f, lng %f, acc:%f", location.getProvider(), lat, lng, location.getAccuracy());
            this.mLocation = location;

            for (GPSListener l : mListeners) {
                if (DEBUG) {
                    l.onNewLocation(55.707644, 13.192707); // Push fake coordinates
                } else {
                    l.onNewLocation(lat, lng);
                }
            }
        } else {
            Logf.d(LOG_TAG, "(%s) Ignoring location: lat %f, lng %f, acc:%f", location.getProvider(), lat, lng, location.getAccuracy());
        }

    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.wtf(LOG_TAG, "Current Provider is disabled: "+provider);
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.wtf(LOG_TAG, "Current Provider is enabled: "+provider);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Logf.wtf(LOG_TAG, "Current Provider Status: %s, %d, %s",provider, status, extras);
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

}