package com.ysered.signatureview.util

import android.graphics.Bitmap


fun scaleBitmapByWidth(original: Bitmap, newWidth: Int): Bitmap {
    return try {
        val newHeight = newWidth * original.height / newWidth
        Bitmap.createScaledBitmap(original, newWidth, newHeight, false)
    } finally {
        original.recycle()
    }
}
