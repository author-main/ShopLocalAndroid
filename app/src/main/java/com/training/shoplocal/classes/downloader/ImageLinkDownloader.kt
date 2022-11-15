package com.training.shoplocal.classes.downloader

import android.graphics.Bitmap
import com.training.shoplocal.Error
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

interface Callback {
    fun onComplete(image: Bitmap)
    fun onFailure(error: Error)
}

class ImageLinkDownloader private constructor(){
    fun md5(link: String): String? {
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
    companion object {
        private var instance: ImageLinkDownloader? = null
        private fun getInstance(): ImageLinkDownloader =
            instance ?: ImageLinkDownloader()

        fun download(url: String, callback: Callback) {
            getInstance().downloadLinkImage(url, callback)
        }
    }
}