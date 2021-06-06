package com.example.traveller.adapter

import android.content.Context
import android.graphics.*
import androidx.recyclerview.widget.RecyclerView
import com.example.traveller.settings.PreferencesModel
import com.example.traveller.R
import com.example.traveller.database.Entry
//import com.example.traveller.database.EntryViewModel
import com.example.traveller.databinding.ItemEntryBinding
import com.example.traveller.helper.BitmapHelper
import java.io.FileNotFoundException

class EntryViewHolder(
    private val layoutBinding: ItemEntryBinding,
    private val context: Context,
    private val preferencesModel: PreferencesModel
) : RecyclerView.ViewHolder(layoutBinding.root) {

    fun bind(entry: Entry) = with(layoutBinding) {
        val textData = "${entry.placeName}, ${entry.countryName}, ${entry.date}"
        itemEntryPhotoImageView.setImageBitmap(getImageById(entry.photoId, context, textData))
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
            BitmapHelper.addTextToBitmap(bitmapToBeEdited, textToBeDrawn, preferencesModel)
            return bitmapToBeEdited
        } catch (execution: FileNotFoundException) {
            return BitmapFactory.decodeResource(
                context.resources,
                R.drawable.ic_launcher_foreground
            )
        }
    }
}