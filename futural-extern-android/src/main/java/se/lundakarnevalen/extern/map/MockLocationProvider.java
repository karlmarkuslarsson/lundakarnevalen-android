package se.lundakarnevalen.extern.map;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.SystemClock;

/**
 * Created by Filip on 2014-05-08.
 */
public class MockLocationProvider {
    private String providerName;
    private Context ctx;

    public MockLocationProvider(String name, Context ctx) {
        this.providerName = name;
        this.ctx = ctx;
        LocationManager lm = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
        lm.addTestProvider(providerName, false, false, false, false, true, true, true, 0, 5);
        lm.setTestProviderEnabled(providerName, true);
    }

    public void pushLocation(double lat, double lon) {
        Location mockLocation = new Location(providerName);
        mockLocation.setLatitude(lat);
        mockLocation.setLongitude(lon);
        mockLocation.setAltitude(0.0);
        mockLocation.setAccuracy(3.0f);
        mockLocation.setTime(System.currentTimeMillis());
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            mockLocation.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
        }
        LocationManager.class.cast(ctx.getSystemService(Context.LOCATION_SERVICE))
                .setTestProviderLocation(providerName, mockLocation);
    }

    public void shutdown() {
        LocationManager.class.cast(ctx.getSystemService(Context.LOCATION_SERVICE))
                .removeTestProvider(providerName);
    }
}