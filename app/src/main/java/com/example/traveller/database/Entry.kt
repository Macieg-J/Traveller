package com.example.traveller.database

import android.graphics.Bitmap
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import java.util.*

@Parcelize
@Entity
data class Entry(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "photo") val photoId: String,
    @ColumnInfo(name = "note") val note: String?,
    @ColumnInfo(name = "latitude") val latitude: Double,
    @ColumnInfo(name = "longitude") val longitude: Double,
    @ColumnInfo(name = "place_name") val placeName: String?,
    @ColumnInfo(name = "country_name") val countryName: String,
    @ColumnInfo(name = "date") val date: String,

    ) : Parcelable

//
//data class EntryViewModel(
//    val id: String,
//    val note: String?,
//    val placeName: String?,
//    val countryName: String,
//    val date: String,
//    val bitmap: Bitmap? = null
//)
