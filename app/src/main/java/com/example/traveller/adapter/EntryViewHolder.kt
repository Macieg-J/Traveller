package com.example.traveller.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.recyclerview.widget.RecyclerView
import com.example.traveller.R
import com.example.traveller.database.Entry
import com.example.traveller.databinding.ItemEntryBinding
import java.io.FileNotFoundException

fun getImageById(id: String, context: Context): Bitmap {
    val filePath = context.filesDir.absolutePath + "/" + id
    val createdBitmap: Bitmap
//        createdBitmap.use {
//        }

    try {
        createdBitmap = BitmapFactory.decodeFile(filePath)
        return createdBitmap
    } catch (execution: FileNotFoundException) {
        return BitmapFactory.decodeResource(
            context.resources,
            R.drawable.ic_launcher_foreground
        )
    }
//        return BitmapFactory.decodeFile(filePath)
}

class EntryViewHolder(
    private val layoutBinding: ItemEntryBinding,
    private val context: Context
) : RecyclerView.ViewHolder(layoutBinding.root) {

    private val uriBeginning = ""
    private val uriEnd = ".jpg"

    fun bind(entry: Entry) = with(layoutBinding) {
        itemEntryPhotoImageView.setImageBitmap(getImageById(entry.photoId, context))
//        setImage()
        itemEntryCountryTextView.text = entry.countryName
        itemFinancePlaceTextView.text = entry.placeName
        itemFinanceDateTextView.text = entry.date
    }

    private fun getImageById(id: String, context: Context): Bitmap {
        val filePath = context.filesDir.absolutePath + "/" + id
        val createdBitmap: Bitmap
//        createdBitmap.use {
//        }

        try {
            createdBitmap = BitmapFactory.decodeFile(filePath)
            return createdBitmap
        } catch (execution: FileNotFoundException) {
            return BitmapFactory.decodeResource(
                context.resources,
                R.drawable.ic_launcher_foreground
            )
        }
//        return BitmapFactory.decodeFile(filePath)
    }
}