package com.training.shoplocal.classes.downloader

import android.graphics.Bitmap
import com.training.shoplocal.StateEntry

interface ImageCache {
    fun get(link: String): Bitmap?
    fun put(link: String, image: BitmapTime? = null)
    fun remove(link: String, changeState: Boolean)
    fun update(link: String, state: StateEntry)
    fun journal()
    fun clear()
}