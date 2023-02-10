package com.braver.wear.android;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;

public class LocationCollect {
    double longitude;
    double latitude;
    boolean changed = false;

    @SuppressLint("MissingPermission")
    public String getCoordinatesTranslated(Context context) {
        Location gps_loc, network_loc, final_loc;

        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);


        try {

            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {
                        }
                        @Override
                        public void onProviderEnabled(String provider) {
                        }
                        @Override
                        public void onProviderDisabled(String provider) {
                        }
                        @Override
                        public void onLocationChanged(final Location location) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            changed = true;
                        }
                    });

            gps_loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            network_loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if (network_loc != null) {
                final_loc = network_loc;
                latitude = final_loc.getLatitude();
                longitude = final_loc.getLongitude();
            } else if (gps_loc != null) {
                final_loc = gps_loc;
                latitude = final_loc.getLatitude();
                longitude = final_loc.getLongitude();
            } else {
                latitude = 0.0;
                longitude = 0.0;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return getLatitude() + ";" + getLongitude();

    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
