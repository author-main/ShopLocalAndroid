package com.training.shoplocal.classes.downloader

import android.graphics.Bitmap

interface ImageCache {
    //val cacheDir: String
    fun get(link: String, time: Long): Bitmap?
    fun put(link: String, image: Bitmap, time: Long)
    fun remove(hash: String)
    fun clear()
}