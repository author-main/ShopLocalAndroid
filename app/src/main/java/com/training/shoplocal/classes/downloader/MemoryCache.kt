package com.training.shoplocal.classes.downloader

import android.graphics.Bitmap
import androidx.collection.LruCache
import com.training.shoplocal.dagger.CacheSize
import com.training.shoplocal.log

class MemoryCache(@CacheSize override val sizeCache: Int): ImageMemoryCache{

    //private val maxCacheSize = SIZE_MEMORYCACHE * 1024 * 1024 // 8Mb Memory cache
    init {
        log ("create memoryCache...")
    }

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

   /* companion object {
        //private val instance = MemoryCache()
        private lateinit var instance: MemoryCache
        fun initStorage() {
            getInstance()
        }
        private fun getInstance() {
            if (!this::instance.isInitialized)
                instance = MemoryCache()
        }
        fun put(key: String, bitmap: Bitmap) {
            //getInstance()
            instance.put(key, bitmap)
        }
        fun get(key: String): Bitmap? {
            //getInstance()
            return instance.get(key)
        }
        fun remove(key: String) {
            //getInstance()
            instance.remove(key)
        }
        fun exist(key:String): Boolean {
            //getInstance()
            return instance.exist(key)
        }
    }*/
}