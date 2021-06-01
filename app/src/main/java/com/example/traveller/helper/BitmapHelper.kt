package com.example.traveller.helper

import android.graphics.*
import com.example.traveller.settings.PreferencesModel

class BitmapHelper {
    companion object {
        private var chosenColor: Int = Color.BLACK
        fun addTextToBitmap(
            bitmap: Bitmap,
            textToBeDrawn: String,
            preferencesModel: PreferencesModel?
        ) {
            if (preferencesModel != null) {
                when(preferencesModel.fontColor){
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