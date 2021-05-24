package com.example.traveller.camera

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.traveller.R
import com.example.traveller.databinding.ActivityCameraBinding
import java.util.*


class CameraActivity : AppCompatActivity() {

    private val binding by lazy { ActivityCameraBinding.inflate(layoutInflater) }
    private val takePhotoButton by lazy { findViewById<Button>(R.id.camera_take_picture_button) }
    private val photoImageView by lazy { findViewById<ImageView>(R.id.camera_taken_picture_imageView) }
    private val providerIdentifier = "com.example.traveller.FileProvider"
    private val takePictureRequestCode = 1
    private var photoStringId = UUID.randomUUID().toString()

    // FileProvider zapisuje w pamięci lokalnej aplikacji, MediaStore w pamięci ogólnej

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        takePhotoButton.setOnClickListener {
            val uri = generateUri()
            val takePhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).let {
                it.putExtra(MediaStore.EXTRA_OUTPUT, uri)
            }
            startActivityForResult(takePhotoIntent, takePictureRequestCode)
        }

        binding.cameraSaveButton.setOnClickListener {

        }
    }

    // authority to identyfikator provider'a, adres pod ktory aplikacja kamery bedzie probowala sie odwolac
    private fun generateUri(): Uri { // zwraca URI do stworzonego pliku (w ktorym bedzie zdjecie)
//        val file = filesDir.resolve("images/image.jpg").also {
        val file = filesDir.resolve(photoStringId).also {
            it.writeText("") // tworzy "czysty" plik
        }

        return FileProvider.getUriForFile(this, providerIdentifier, file)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == takePictureRequestCode && resultCode == RESULT_OK) {
//            photoImageView.setImageBitmap(data?.getParcelableExtra("photo") as? Bitmap) // miniaturka zdjecia
            photoImageView.setImageBitmap( // zapis zdjecia
                BitmapFactory.decodeFile(  // pelne zdjecie
//                    filesDir.resolve("images/image.jpg").absolutePath
                    filesDir.resolve(photoStringId).absolutePath
//                ).compress(Bitmap.CompressFormat.JPEG, 50, openFileOutput("x", MODE_APPEND))
                )
            )

        }

        super.onActivityResult(requestCode, resultCode, data)
    }
}