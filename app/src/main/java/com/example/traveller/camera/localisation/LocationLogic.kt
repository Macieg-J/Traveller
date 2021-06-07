package com.example.traveller.camera.localisation

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import java.util.*
import java.util.function.Consumer
import javax.security.auth.callback.Callback

const val REQUEST_CHECK_SETTINGS = 69
const val UPDATE_INTERVAL: Long = 10000

class LocationLogic
    (
    val activity: Activity,
    val fusedLocationProviderClient: FusedLocationProviderClient,
    val callback: Consumer<LocationModel>
) {
    private val TAG = "LocalisationServicesClient"

    //    private lateinit var locationCallback: LocationCallback
    private var locationRequest: LocationRequest = LocationRequest.create()
    lateinit var lastKnownLocation: Location;

    init {
        startPoolingLocation()
    }

    private fun startPoolingLocation() {
        if (ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationProviderClient.lastLocation
            .addOnCompleteListener { task ->
                val location: Location = task.result
                requestNewLocationData()
                Log.d(TAG, "Location: $location, latitude: ${location.latitude}")
                decodeLocation(task.result)
            }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = UPDATE_INTERVAL
        locationRequest.fastestInterval = UPDATE_INTERVAL
        locationRequest.smallestDisplacement = 200f

        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.myLooper()!!
        )
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            if (locationResult != null) {
                lastKnownLocation = locationResult.lastLocation
                decodeLocation(lastKnownLocation)
                Log.d(
                    TAG,
                    "last known location: $lastKnownLocation, " +
                            "last known latitude: ${lastKnownLocation.latitude}, " +
                            "last known longitude: ${lastKnownLocation.longitude}, "

                )
            }
        }
    }

    private fun decodeLocation(lastKnownLocation: Location) {
        val geocoder = Geocoder(activity, Locale.getDefault())
        val listOfAddresses: List<Address> =
            geocoder.getFromLocation(
                lastKnownLocation.latitude,
                lastKnownLocation.longitude,
                3
            )
        callback.accept(
            LocationModel(
                listOfAddresses[0],
                lastKnownLocation.latitude,
                lastKnownLocation.longitude
            )
        )
        Log.d(TAG, "Last known country: ${listOfAddresses[0].countryName}, Last known city: ${listOfAddresses[0].locality}")
    }
}