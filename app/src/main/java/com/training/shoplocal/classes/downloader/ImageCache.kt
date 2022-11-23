package com.training.shoplocal.classes.downloader

import android.graphics.Bitmap
import com.training.shoplocal.StateEntry

interface ImageCache {
    fun placed(filesize: Long): Boolean
    fun get(link: String, timestamp: Long): Bitmap?
    fun put(link: String, time: Long = 0L)
    fun remove(link: String, changeState: Boolean)
    fun update(link: String, state: StateEntry)
    fun normalizeJournal()
    fun clear()
}