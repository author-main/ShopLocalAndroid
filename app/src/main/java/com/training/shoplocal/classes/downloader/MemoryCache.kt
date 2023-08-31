package com.training.shoplocal.classes.downloader

import android.graphics.Bitmap
import androidx.collection.LruCache
import com.training.shoplocal.log
import javax.inject.Inject

class MemoryCache @Inject constructor (override val sizeCache: Int): ImageMemoryCache{
    private val cache = object: LruCache<String, Bitmap>(sizeCache * 1024 * 1024){
        override fun sizeOf(key: String, value: Bitmap): Int {
            return value.byteCount / 1024
        }

    }

    override fun put(key: String, bitmap: Bitmap) {
        cache.put(key, bitmap)
    }

    override fun get(key: String): Bitmap? =
        cache.get(key)

    override fun remove(key: String) {
        cache.remove(key)
    }

    override fun exist(key: String) =
        cache.get(key) != null
}