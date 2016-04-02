package abrewster.cganong.unb.ca.project;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by coreyganong on 2016-04-02.
 */
public class LocationService extends Service {
    Intent intent;
    public static final String BROADCAST_ACTION = "Hello World";
    private static final int TWO_MINUTES = 1000 * 60 * 2;
    public Location previousBestLocation = null;

    @Override
    public void onCreate() {
        super.onCreate();
        intent = new Intent(BROADCAST_ACTION);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startID) {
        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                if (isBetterLocation(location,previousBestLocation)) {
                    Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                    Address address1 = null;
                    Address address2 = null;
                    try {
                        List<Address> listAddresses1 = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        if (null != listAddresses1 && listAddresses1.size() > 0) {
                            address1 = listAddresses1.get(0);
                        }
                        if (previousBestLocation != null) {
                            List<Address> listAddresses2 = geocoder.getFromLocation(previousBestLocation.getLatitude(), previousBestLocation.getLongitude(), 1);
                            if (null != listAddresses2 && listAddresses2.size() > 0) {
                                address2 = listAddresses2.get(0);
                            }
                        }
                    } catch (IOException e) {

                    }
                    if (address1 != null) {
                        if (address2 != null) {
                            if (addressToString(address1).equals(addressToString(address2))) {
                                return;
                            }
                        }
                        NotificationCompat.Builder mBuilder =
                                new NotificationCompat.Builder(LocationService.this)
                                        .setSmallIcon(R.drawable.ic_menu_manage)
                                        .setContentTitle("Location updated")
                                        .setContentText(addressToString(address1));

                        NotificationManager mNotificationManager =
                                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        // mId allows you to update the notification later on.
                        mNotificationManager.notify(1234, mBuilder.build());
                        previousBestLocation = location;
                    }
                }
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

        // Register the listener with the Location Manager to receive location updates
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 35000, 5, locationListener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 35000, 5, locationListener);
        } catch (SecurityException e ) {

        }
        return START_STICKY;
    }

    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }
    public static String addressToString(Address a) {
        return (a.getAddressLine(0) + ", " + a.getAddressLine(1) + ", " + a.getAddressLine(2));

    }
}