package com.training.shoplocal.classes.downloader

import android.graphics.Bitmap
import androidx.collection.LruCache

class MemoryCache {
    private val maxMemory = Runtime.getRuntime().maxMemory() / 1024
    private val maxCacheSize = (maxMemory / 10).toInt()
    private val cache = object: LruCache<String, Bitmap>(maxCacheSize){
        override fun sizeOf(key: String, value: Bitmap): Int {
            return value.byteCount / 1024
        }
    }
}