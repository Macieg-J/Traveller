package com.example.traveller.camera

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.EXTRA_OUTPUT
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.traveller.R
import com.example.traveller.databinding.ActivityCameraBinding


class CameraActivity : AppCompatActivity() {

    private val binding by lazy { ActivityCameraBinding.inflate(layoutInflater) }
    private val takePhotoButton by lazy { findViewById<Button>(R.id.button) }
    private val photoImageView by lazy { findViewById<ImageView>(R.id.imageView) }
    private val providerIdentifier = "com.example.traveller.FileProvider"
    private val takePictureRequestCode = 1

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
    }

    // authority to identyfikator provider'a, adres pod ktory aplikacja kamery bedzie probowala sie odwolac
    private fun generateUri(): Uri { // zwraca URI do stworzonego pliku (w ktorym bedzie zdjecie)
//        val file = filesDir.resolve("images/image.jpg").also {
        val file = filesDir.resolve("image.jpg").also {
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
                    filesDir.resolve("image.jpg").absolutePath
                )
            )

        }

        super.onActivityResult(requestCode, resultCode, data)
    }
}