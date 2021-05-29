package com.example.traveller.adapter

import android.content.Context
import android.graphics.*
import androidx.recyclerview.widget.RecyclerView
import com.example.traveller.R
import com.example.traveller.database.Entry
//import com.example.traveller.database.EntryViewModel
import com.example.traveller.databinding.ItemEntryBinding
import com.example.traveller.helper.BitmapHelper
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
        val textData = "${entry.placeName}, ${entry.countryName}, ${entry.date}"
        itemEntryPhotoImageView.setImageBitmap(getImageById(entry.photoId, context, textData))
//        setImage()
        itemEntryCountryTextView.text = entry.countryName
        itemFinancePlaceTextView.text = entry.placeName
        itemFinanceDateTextView.text = entry.date
    }

    private fun getImageById(id: String, context: Context, textToBeDrawn: String): Bitmap {
        val filePath = context.filesDir.absolutePath + "/" + id
        val createdBitmap: Bitmap
        try {
            createdBitmap = BitmapFactory.decodeFile(filePath)
            val bitmapToBeEdited: Bitmap = createdBitmap.copy(android.graphics.Bitmap.Config.ARGB_8888, true)
            BitmapHelper.addTextToBitmap(bitmapToBeEdited, textToBeDrawn)
            return bitmapToBeEdited
        } catch (execution: FileNotFoundException) {
            return BitmapFactory.decodeResource(
                context.resources,
                R.drawable.ic_launcher_foreground
            )
        }
    }

//    private fun addTextToBitmap(bitmap: Bitmap, textToBeDrawn: String) {
//        val canvas = Canvas(bitmap)
//        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
//        paint.apply {
//            color = Color.BLACK
//            strokeWidth = 12f
//            style = Paint.Style.STROKE
//            textSize = 12f
//            xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OVER)
//        }
//        canvas.apply {
//            drawText(textToBeDrawn, 8f, 8f, paint)
//            drawBitmap(bitmap, 0f, 0f, paint)
//        }
//    }
// fun bind(entry: EntryViewModel) = with(layoutBinding) {
//        if (entry.bitmap == null) {
//            itemEntryPhotoImageView.setImageResource(R.drawable.common_google_signin_btn_icon_dark) //fixme
//        }
//        else itemEntryPhotoImageView.setImageBitmap(entry.bitmap)
////        setImage()
//        itemEntryCountryTextView.text = entry.countryName
//        itemFinancePlaceTextView.text = entry.placeName
//        itemFinanceDateTextView.text = entry.date
//    }
//
//    private fun getImageById(id: String, context: Context): Bitmap { // fixme placeholder
//        val filePath = context.filesDir.absolutePath + "/" + id
//        val createdBitmap: Bitmap
////        createdBitmap.use {
////        }
//
//        try {
//            createdBitmap = BitmapFactory.decodeFile(filePath)
//            return createdBitmap
//        } catch (execution: FileNotFoundException) {
//            return BitmapFactory.decodeResource(
//                context.resources,
//                R.drawable.ic_launcher_foreground
//            )
//        }
////        return BitmapFactory.decodeFile(filePath)
//    }
}