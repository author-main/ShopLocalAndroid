package com.training.shoplocal.classes.downloader

import android.graphics.Bitmap

interface ImageMemoryCache {
    val sizeCache: Int
    fun put(key: String, bitmap: Bitmap)
    fun get(key: String): Bitmap?
    fun remove(key: String)
    fun exist(key: String): Boolean
}