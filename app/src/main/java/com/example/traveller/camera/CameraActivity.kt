package com.example.traveller.camera

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Address
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.traveller.settings.PreferencesModel
import com.example.traveller.R
import com.example.traveller.camera.localisation.LocationLogic
import com.example.traveller.camera.localisation.LocationModel
import com.example.traveller.database.Entry
import com.example.traveller.databinding.ActivityCameraBinding
import com.example.traveller.helper.BitmapHelper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.time.LocalDate
import java.util.*
import java.util.function.Consumer

class CameraActivity :
    AppCompatActivity() {

    private val TAG = "CameraActivity"
    private val binding by lazy { ActivityCameraBinding.inflate(layoutInflater) }
    private val takePhotoButton by lazy { findViewById<Button>(R.id.camera_take_picture_button) }
    private val photoImageView by lazy { findViewById<ImageView>(R.id.camera_taken_picture_imageView) }
    private val providerIdentifier = "com.example.traveller.FileProvider"
    private val takePictureRequestCode = 1
    private var photoStringId = UUID.randomUUID().toString()
    private lateinit var locationLogic: LocationLogic
    private lateinit var lastKnownLocation: Address
    private var isLocationFetched = false
    private var preferencesModel: PreferencesModel? = PreferencesModel(1, 30f, "Black")
    private val locationManager by lazy { getSystemService(LocationManager::class.java) } // todo addProximityAlert
    private val callback: Consumer<LocationModel> = Consumer { locationModel ->
        lastKnownLocation = locationModel.lastKnownLocation
        isLocationFetched = true
        enableActions()
    }

    // FileProvider zapisuje w pami??ci lokalnej aplikacji, MediaStore w pami??ci og??lnej

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val fetchedEntry = intent.getParcelableExtra<Entry>("DISPLAY_ENTRY")
        preferencesModel = intent.getParcelableExtra("PreferencesModel") // fixme IS NULL 01.06.2021
        if (fetchedEntry != null) {
            recreateAndDisable(fetchedEntry)
        } else {
//            val fusedLocationProviderClient: FusedLocationProviderClient =
//                LocationServices.getFusedLocationProviderClient(this)
//            locationLogic = LocationLogic(this, fusedLocationProviderClient, callback)

            if (binding.cameraNoteEditTextText.length() == 500) {
                Toast.makeText(
                    this,
                    "Max note length is 500 characters!",
                    Toast.LENGTH_LONG
                ).show()
            }
//        if (fetchedEntry != null) {
//            recreateAndDisable(fetchedEntry)
//        }
            enableActions()
        }
    }

    private fun enableActions() {
        takePhotoButton.setOnClickListener {
            val fusedLocationProviderClient: FusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(this)
            locationLogic = LocationLogic(this, fusedLocationProviderClient, callback)
            val uri = generateUri()
            val takePhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).let {
                it.putExtra(MediaStore.EXTRA_OUTPUT, uri)
            }
            startActivityForResult(takePhotoIntent, takePictureRequestCode)
        }

        binding.cameraSaveButton.setOnClickListener {
            if (binding.cameraNoteEditTextText.length() <= 500) {
                returnItemToBeSaved()
            } else {
                Toast.makeText(
                    this,
                    "Max note length is 500 characters!",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    // authority to identyfikator provider'a, adres pod ktory aplikacja kamery bedzie probowala sie odwolac
    private fun generateUri(): Uri { // zwraca URI do stworzonego pliku (w ktorym bedzie zdjecie) fixme https://developer.android.com/topic/performance/graphics/load-bitmap
//        val file = filesDir.resolve("images/image.jpg").also {
        val file = filesDir.resolve(photoStringId).also {
            it.writeText("") // tworzy "czysty" plik
        }

        return FileProvider.getUriForFile(this, providerIdentifier, file)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == takePictureRequestCode && resultCode == RESULT_OK) {
            val fullBitmap = BitmapFactory.decodeFile(
                filesDir.resolve(photoStringId).absolutePath
            )
            photoImageView.setImageBitmap(fullBitmap)

        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun returnItemToBeSaved() {
        Log.d(
            TAG, "createdEntry - id: \"Entry_$photoStringId\", " +
                    "photoId: $photoStringId, " +
                    "cameraNote: ${binding.cameraNoteEditTextText.text.toString()}, " +
                    "latitude: ${lastKnownLocation.latitude}, " +
                    "longitude: ${lastKnownLocation.longitude}, " +
                    "featureName: ${lastKnownLocation.featureName}, " +
                    "countryName: ${lastKnownLocation.countryName}, " +
                    "date: ${LocalDate.now().year}-${LocalDate.now().month}-${LocalDate.now().dayOfMonth}"
        )
        val createdEntry = Entry(
            "Entry_$photoStringId",
            photoStringId,
            binding.cameraNoteEditTextText.text.toString(),
//            binding.cameraNoteEditTextText.text.toString(),
            lastKnownLocation.latitude,
            lastKnownLocation.longitude,
            lastKnownLocation.locality,
            lastKnownLocation.countryName,
            "" + LocalDate.now().year + LocalDate.now().month + LocalDate.now().dayOfMonth
        )

        val resultIntent = Intent()
        resultIntent.putExtra("ENTRY_INFO", createdEntry)
        setResult(
            Activity.RESULT_OK,
            resultIntent
        )
        isLocationFetched = false
        finish()
    }

    private fun recreateAndDisable(fetchedEntry: Entry) {
        val textToBeAdded =
            "${fetchedEntry.placeName}, ${fetchedEntry.countryName}, ${fetchedEntry.date}"
//        val fetchedBitmap = BitmapHelper.getImageById(fetchedEntry.photoId, this)
        val fetchedBitmap = BitmapHelper.getImageByIdAndAddText(
            fetchedEntry.photoId,
            this,
            textToBeAdded,
            preferencesModel
        )
//        val bitmapToBeEdited: Bitmap =
//            fetchedBitmap.copy(android.graphics.Bitmap.Config.ARGB_8888, true)
//        BitmapHelper.addTextToBitmap(bitmapToBeEdited, textToBeAdded, preferencesModel)
        photoImageView.setImageBitmap(fetchedBitmap)
        binding.cameraNoteEditTextText.setText(fetchedEntry.note)

        binding.cameraTakePictureButton.visibility = View.INVISIBLE
        binding.cameraSaveButton.apply {
            text = "Back"
            setOnClickListener { finish() }
        }
        binding.cameraNoteEditTextText.isClickable = false
        binding.cameraNoteEditTextText.isActivated = false
    }
}