package com.example.traveller.helper

import android.graphics.*

class BitmapHelper {
    companion object {
        fun addTextToBitmap(bitmap: Bitmap, textToBeDrawn: String) {
            val canvas = Canvas(bitmap)
            val paint = Paint(Paint.ANTI_ALIAS_FLAG)
            paint.apply {
                color = Color.BLACK
                strokeWidth = 12f
//                style = Paint.Style.STROKE
                textSize = 30f
                xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OVER)
            }
            canvas.apply {
                drawText(textToBeDrawn, 8f, 32f, paint)
                drawBitmap(bitmap, 0f, 0f, paint)
            }
        }
    }
}