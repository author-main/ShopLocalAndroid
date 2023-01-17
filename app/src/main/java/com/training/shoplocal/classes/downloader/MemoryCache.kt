package com.training.shoplocal.classes.downloader

import android.graphics.Bitmap
import androidx.collection.LruCache
import com.training.shoplocal.classes.SIZE_MEMORYCACHE
import com.training.shoplocal.log

class MemoryCache {
    //private val maxMemory = Runtime.getRuntime().maxMemory() / 1024
    //private val maxCacheSize = (maxMemory / 10).toInt()
    private val maxMemory = SIZE_MEMORYCACHE * 1024 * 1024 // 8Mb Memory cache
    private val maxCacheSize = maxMemory
    private val cache = object: LruCache<String, Bitmap>(maxCacheSize){
        override fun sizeOf(key: String, value: Bitmap): Int {
            //log("max cache size = $maxCacheSize")
            return value.byteCount / 1024
        }

    }

    private fun put(key: String, bitmap: Bitmap) {
        cache.put(key, bitmap)
    }
    private fun get(key: String): Bitmap? =
        cache.get(key)
    private fun remove(key: String) {
        cache.remove(key)
    }

    companion object {
        private lateinit var instance: MemoryCache
        private fun getInstance() {
            if (!this::instance.isInitialized)
                instance = MemoryCache()
        }
        fun put(key: String, bitmap: Bitmap) {
            getInstance()
            instance.put(key, bitmap)
        }
        fun get(key: String): Bitmap? {
            getInstance()
            return instance.get(key)
        }
        fun remove(key: String) {
            getInstance()
            instance.remove(key)
        }
    }
}