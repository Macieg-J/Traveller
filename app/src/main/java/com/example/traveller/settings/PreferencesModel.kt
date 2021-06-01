package com.example.traveller.settings

import android.graphics.Color
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
@Parcelize
data class PreferencesModel(
    val fontSize: Float,
    val fontColor: String
): Parcelable
