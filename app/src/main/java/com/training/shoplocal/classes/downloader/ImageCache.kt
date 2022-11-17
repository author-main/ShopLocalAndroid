package com.training.shoplocal.classes.downloader

import android.graphics.Bitmap

interface ImageCache {
    //val cacheDir: String
    fun get(link: String): Bitmap?
    fun put(link: String, image: Bitmap)
    fun remove(hash: String)
    fun clear()
}