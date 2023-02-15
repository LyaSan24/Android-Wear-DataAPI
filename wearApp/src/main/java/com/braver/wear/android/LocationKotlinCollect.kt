package com.braver.wear.android

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.core.app.ActivityCompat

class LocationKotlinCollect {

    var longitude = 0.0
    var latitude = 0.0

    fun getLocation(context: Context): String {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (ActivityCompat.checkSelfPermission(
                context,
                ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request permission
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(ACCESS_FINE_LOCATION),
                1
            )
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, object :
                LocationListener {
                override fun onLocationChanged(location: Location) {
                    // Handle location change
                    latitude = location.latitude
                    longitude = location.longitude
                    // Do something with the latitude and longitude
                }

                override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}

                override fun onProviderEnabled(provider: String) {}

                override fun onProviderDisabled(provider: String) {}
            })
        }

        return "$latitude;$longitude"

    }
}