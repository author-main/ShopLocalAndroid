package com.training.shoplocal.classes.downloader

import android.graphics.Bitmap

interface Callback {
    fun onComplete(image: ExtBitmap)
    fun onFailure()
}