package com.example.traveller.camera.localisation

import android.location.Address
import android.location.Geocoder

data class LocationModel(
    val lastKnownLocation: Address,
    val lastKnownLatitude: Double,
    val lastKnownLongitude: Double
)