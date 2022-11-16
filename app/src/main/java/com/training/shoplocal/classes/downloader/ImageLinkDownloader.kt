package com.training.shoplocal.classes.downloader

import android.content.Context
import android.graphics.Bitmap
import com.training.shoplocal.AppShopLocal.Companion.appContext
import com.training.shoplocal.Error
import java.io.File
import java.io.IOException
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

interface Callback {
    fun onComplete(image: Bitmap)
    fun onFailure(error: Error)
}

class DiskCache(private val cacheDir: String): ImageCache {
    private var existsCacheStorage = false
    init{
        val dir: File = File(cacheDir)
        existsCacheStorage = if (!dir.exists()) {
            try {
                dir.mkdirs()
            } catch (_: IOException) {
                false
            }
        } else
            true
    }
    override fun get(hash: String): Bitmap? {
        TODO("Not yet implemented")
    }

    override fun put(hash: String, image: Bitmap) {
        TODO("Not yet implemented")
    }

    override fun remove(hash: String) {
        TODO("Not yet implemented")
    }

    override fun clear() {
        TODO("Not yet implemented")
    }

  /*  companion object {
        private var instance: ImageCache? = null
        fun getInstance(cacheDir: String): ImageCache =
            instance ?: DiskCache(cacheDir)

        /*fun get(md5: String): Bitmap? {
            return instance?.get(md5)
        }*/
    }*/
}

class ImageLinkDownloader private constructor(){
    private var cacheStorage: ImageCache? = null
    fun md5(link: String): String {
        val HASH_LENGTH = 32
        try {
        val md = MessageDigest.getInstance("MD5")
        val messageDigest = md.digest(link.toByteArray())
        val hashbi = BigInteger(1, messageDigest)
        var hashtext = hashbi.toString(16)
            try {
                hashtext = "0".repeat(HASH_LENGTH - hashtext.length) + hashtext
            } catch (e: IllegalArgumentException) {
                throw RuntimeException(e)
            }
        /*while (hashtext.length < 32) {
            hashtext = "0$hashtext"
        }*/
        return hashtext
    } catch (e: NoSuchAlgorithmException) {
        throw RuntimeException(e)
    }
    }
    fun downloadLinkImage(link: String, callback: Callback){
        val image = Bitmap.createBitmap(100,100,Bitmap.Config.ALPHA_8)
        if (image != null)
            callback.onComplete(image)
        else
            callback.onFailure(Error.NO_CONNECTION)
    }

    private fun setCacheDirectory(dir: String){
        if (cacheStorage == null)
            cacheStorage = DiskCache(dir)
    }

    companion object {
        private var instance: ImageLinkDownloader? = null
        private fun getInstance(): ImageLinkDownloader =
            instance ?: ImageLinkDownloader()

        fun setCacheDirectory(dir: String){
            getInstance().setCacheDirectory(dir)
        }

        fun download(url: String, callback: Callback) {
            getInstance().downloadLinkImage(url, callback)
        }
    }
}