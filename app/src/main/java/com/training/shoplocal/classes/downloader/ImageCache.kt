package com.training.shoplocal.classes.downloader

import android.graphics.Bitmap

interface ImageCache {
    //val cacheDir: String
    fun get(hash: String): Bitmap?
    fun put(hash: String, image: Bitmap)
    fun remove(hash: String)
    fun clear()
}