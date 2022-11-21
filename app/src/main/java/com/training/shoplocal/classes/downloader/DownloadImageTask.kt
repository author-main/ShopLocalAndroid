package com.training.shoplocal.classes.downloader

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import java.net.HttpURLConnection
import java.net.URL

class DownloadImageTask(private val link: String): DownloadTask<Bitmap?>
{
    private var callback: Callback? = null
   // private val uiHandler = Handler(Looper.getMainLooper())
    fun addDownloadCallback(value: Callback) {
        callback = value
    }
    override fun download(link: String): Bitmap? {
        return try {
            val conn = URL(link).openConnection() as HttpURLConnection
            conn.requestMethod = "HEAD"
            val timestamp = conn.lastModified
            /*var bitmap = cache?.get(link, timestamp)
            if (bitmap == null) {*/
            conn.requestMethod = "GET"
            val bitmap = BitmapFactory.decodeStream(conn.inputStream)
           // }
            conn.disconnect()
            bitmap?.let{
                callback?.onComplete(BitmapTime(it, timestamp))
            } ?: callback?.onFailure()
            bitmap
        } catch (_: Exception) {
            null
        }
    }

    override fun call(): Bitmap? {
        return download(this.link)
     //   uiHandler.post { // обновление View только в главном потоке
        /*if (image == null)
            callback?.onFailure()
        else
            callback?.onComplete(image)*/

                //callback.onComplete(image)
    //    }
        //return image
    }
}