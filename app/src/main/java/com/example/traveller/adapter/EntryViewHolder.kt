package com.example.traveller.adapter

import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import androidx.recyclerview.widget.RecyclerView
import com.example.traveller.R
import com.example.traveller.database.Entry
import com.example.traveller.databinding.ItemEntryBinding

class EntryViewHolder(
    private val layoutBinding: ItemEntryBinding,
    private val context: Context
) : RecyclerView.ViewHolder(layoutBinding.root) {

    private val uriBeginning = ""
    private val uriEnd = ".jpg"

    fun bind(entry: Entry) = with(layoutBinding) {
        itemEntryPhotoImageView.setImageBitmap(getImageById("image", context))
//        setImage()
        itemEntryCountryTextView.text = entry.countryName
        itemFinancePlaceTextView.text = entry.placeName
        itemFinanceDateTextView.text = entry.date
    }

    fun getImageById(id: String, context: Context): Bitmap {
        val filePath = context.filesDir.absolutePath + "id" + uriEnd
        return BitmapFactory.decodeFile(filePath)
    }

//    private fun setImage() {
//        //MediaStore.getExternalVolumeNames(this)
//        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
//        context.contentResolver.query(
//            uri,
//            arrayOf(MediaStore.Images.Media._ID),
//            null,
//            null,
//            null
//        )?.use {
//            if (it.moveToNext()) {
//                val id = it.getInt(it.getColumnIndex(MediaStore.Images.Media._ID))
//                val imgUri = ContentUris.withAppendedId(uri, id.toLong())
//                context.contentResolver.openInputStream(imgUri)?.use {
//                    BitmapFactory.decodeStream(it).let {
//                        layoutBinding.itemEntryPhotoImageView.setImageBitmap(it)
//                    }
//                }
//            }
//        }
//    }
}