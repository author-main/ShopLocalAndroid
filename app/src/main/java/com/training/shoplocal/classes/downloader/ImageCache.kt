package com.training.shoplocal.classes.downloader

import android.graphics.Bitmap
import com.training.shoplocal.StateEntry

interface ImageCache {
    fun placed(filesize: Long): Boolean
    fun get(link: String, timestamp: Long): Bitmap?
    fun put(link: String, time: Long)
    fun remove(link: String, changeState: Boolean)//, cancel: Boolean = false)
    fun update(link: String, state: StateEntry, time: Long)
    fun getTimestamp(link: String): Long
    fun normalizeJournal()
    fun clear()
    fun getCacheDir(): String
}