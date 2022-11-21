package com.training.shoplocal.classes.downloader

import android.graphics.Bitmap

interface Callback {
    fun onComplete(image: Bitmap)
    fun onFailure()
}