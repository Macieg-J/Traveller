package com.example.traveller.helper

import android.content.Context
import android.graphics.*
import com.example.traveller.R
import com.example.traveller.settings.PreferencesModel
import java.io.FileNotFoundException

class BitmapHelper {
    companion object {

        fun getImageById(id: String, context: Context): Bitmap {
            val filePath = context.filesDir.absolutePath + "/" + id
            val createdBitmap: Bitmap

            try {
                createdBitmap = BitmapFactory.decodeFile(filePath)
                return createdBitmap
            } catch (execution: FileNotFoundException) {
                return BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.ic_launcher_foreground
                )
            }
        }

        fun getImageByIdAndAddText(
            id: String,
            context: Context,
            textToBeDrawn: String,
            preferencesModel: PreferencesModel?
        ): Bitmap {
            val filePath = context.filesDir.absolutePath + "/" + id
            val createdBitmap: Bitmap
            try {
                createdBitmap = BitmapFactory.decodeFile(filePath)
                val bitmapToBeEdited: Bitmap =
                    createdBitmap.copy(android.graphics.Bitmap.Config.ARGB_8888, true)
                BitmapHelper.addTextToBitmap(bitmapToBeEdited, textToBeDrawn, preferencesModel)
                return bitmapToBeEdited
            } catch (execution: FileNotFoundException) {
                return BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.ic_launcher_foreground
                )
            }
        }

        private var chosenColor: Int = Color.BLACK
        fun addTextToBitmap(
            bitmap: Bitmap,
            textToBeDrawn: String,
            preferencesModel: PreferencesModel?
        ) {
            if (preferencesModel != null) {
                when (preferencesModel.fontColor) {
                    "Black" -> chosenColor = Color.BLACK
                    "Red" -> chosenColor = Color.RED
                    "Green" -> chosenColor = Color.GREEN
                    "Blue" -> chosenColor = Color.BLUE
                }
            } else {
                chosenColor = Color.BLACK
            }
            val canvas = Canvas(bitmap)
            val paint = Paint(Paint.ANTI_ALIAS_FLAG)
            paint.apply {
                color = chosenColor
                strokeWidth = 12f
//                textSize = 30f
                textSize = preferencesModel?.fontSize ?: 30f
                xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OVER)
            }
            canvas.apply {
                drawText(textToBeDrawn, 8f, 32f, paint)
                drawBitmap(bitmap, 0f, 0f, paint)
            }
        }
    }
}