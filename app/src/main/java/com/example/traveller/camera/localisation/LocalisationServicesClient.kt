package com.example.traveller.camera.localisation

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task

//public const val REQUEST_CHECK_SETTINGS = 69
//
//class LocalisationServicesClient
//    (
////    val context: Context,
//    val activity: Activity
//) {
//    private val TAG = "LocalisationServicesClient"
//    private lateinit var fusedLocationClient: FusedLocationProviderClient
//    public lateinit var lastKnownLocation: Location;
//
//    public fun setUpLocalisationClient() {
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity.applicationContext)
//
//        getLastKnownLocation()
//
//        val locationRequest = LocationRequest.create().apply {
//            interval = 10000
//            fastestInterval = 2000
//            priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
//        }
//        val builder = LocationSettingsRequest.Builder()
//            .addLocationRequest(locationRequest)
//
//        checkIfLocationSettingsSatisfied()
//
//        // todo https://developer.android.com/training/location/request-updates
//
//    }
//
//    public fun getLastKnownLocation() {
//        if (ActivityCompat.checkSelfPermission(
//                activity.applicationContext,
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return
//        }
//        fusedLocationClient.lastLocation
//            .addOnSuccessListener { location: Location? ->
//                if (location != null) {
//                    lastKnownLocation = location
//                }
//            }
//    }
//
//    fun checkIfLocationSettingsSatisfied() {
//        val builder = LocationSettingsRequest.Builder()
//
//        val client: SettingsClient = LocationServices.getSettingsClient(activity.applicationContext)
//        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())
//
//        task.addOnSuccessListener { locationSettingsResponse ->
//
//        }
//
//        task.addOnFailureListener { exception ->
//            if (exception is ResolvableApiException) {
//                Toast.makeText(
//                    activity.applicationContext,
//                    "Location settings are not satisfied!",
//                    Toast.LENGTH_LONG
//                ).show()
//                Log.d(TAG, "Location settings are not satisfied!")
//                try {
//                    exception.startResolutionForResult(activity, REQUEST_CHECK_SETTINGS)
//                } catch (sendException: IntentSender.SendIntentException) {
//
//                }
//            }
//        }
//    }


//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        // ...
//
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
//    }
//}