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
import com.example.traveller.adapter.getImageById
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
    AppCompatActivity() { // fixme add text on bitmap when displaying details (like in ViewHolder, see top left corner on items in RecyclerView)

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
    private var preferencesModel: PreferencesModel? = PreferencesModel(30f, "Black")
    private val locationManager by lazy { getSystemService(LocationManager::class.java) } // todo addProximityAlert
    private val callback: Consumer<LocationModel> = Consumer { locationModel ->
        lastKnownLocation = locationModel.lastKnownLocation
        isLocationFetched = true
        enableActions()
    }

    // FileProvider zapisuje w pamięci lokalnej aplikacji, MediaStore w pamięci ogólnej

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val fusedLocationProviderClient: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(this)
        locationLogic = LocationLogic(this, fusedLocationProviderClient, callback)
        val fetchedEntry = intent.getParcelableExtra<Entry>("DISPLAY_ENTRY")
        preferencesModel = intent.getParcelableExtra("PreferencesModel")
        if (binding.cameraNoteEditTextText.length() == 500) {
            Toast.makeText(
                this,
                "Max note length is 500 characters!",
                Toast.LENGTH_LONG
            ).show()
        }
        if (fetchedEntry != null) {
            recreateAndDisable(fetchedEntry)
        }
        enableActions()
    }

    private fun enableActions() {
        takePhotoButton.setOnClickListener {
            val uri = generateUri()
            val takePhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).let {
                it.putExtra(MediaStore.EXTRA_OUTPUT, uri)
            }
            startActivityForResult(takePhotoIntent, takePictureRequestCode) // fixme
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
            val fullBitmap = BitmapFactory.decodeFile(  // pelne zdjecie
//                    filesDir.resolve("images/image.jpg").absolutePath
                filesDir.resolve(photoStringId).absolutePath
            )
//            val options = BitmapFactory.Options().apply {
//                inJustDecodeBounds = true
//            }
//            BitmapFactory.decodeResourceStream(resources, , options) // fixme
//
//            val scaleWidth = 450
//            val scaleHeight = 550
//            val width = fullBitmap.width
//            val height = fullBitmap.height
//            val scaledWidth = (width / scaleWidth).toFloat()
//            val scaledHeight = (height / scaleHeight).toFloat()
//            val matrix = Matrix()
//            matrix.postScale(scaledWidth, scaledHeight)
//            val scaledBitmap = Bitmap.createBitmap(fullBitmap, 0, 0, width, height, matrix, false)

//            photoImageView.setImageBitmap(data?.getParcelableExtra("photo") as? Bitmap) // miniaturka zdjecia
//            photoImageView.setImageBitmap( // zapis zdjecia
//                BitmapFactory.decodeFile(  // pelne zdjecie
////                    filesDir.resolve("images/image.jpg").absolutePath
//                    filesDir.resolve(photoStringId).absolutePath
////                ).compress(Bitmap.CompressFormat.JPEG, 50, openFileOutput("x", MODE_APPEND))
//                )
//            )
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
        val fetchedBitmap = getImageById(fetchedEntry.photoId, this)
        val bitmapToBeEdited: Bitmap =
            fetchedBitmap.copy(android.graphics.Bitmap.Config.ARGB_8888, true)
        val textToBeAdded =
            "${fetchedEntry.placeName}, ${fetchedEntry.countryName}, ${fetchedEntry.date}"
        BitmapHelper.addTextToBitmap(bitmapToBeEdited, textToBeAdded, preferencesModel)
        photoImageView.setImageBitmap(bitmapToBeEdited)
        binding.cameraNoteEditTextText.setText(fetchedEntry.note)
//        binding.cameraNoteEditTextText.setText(fetchedEntry.note)

        binding.cameraTakePictureButton.visibility = View.INVISIBLE
        binding.cameraSaveButton.apply {
            text = "Back"
            setOnClickListener { finish() }
        }
        binding.cameraNoteEditTextText.isClickable = false
        binding.cameraNoteEditTextText.isActivated = false
//        binding.cameraNoteEditTextText.isClickable = false
//        binding.cameraNoteEditTextText.isActivated = false
    }
}