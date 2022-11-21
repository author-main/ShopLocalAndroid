package com.training.shoplocal.classes.downloader

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import com.training.shoplocal.createDirectory
import com.training.shoplocal.getTempDirectory
import java.io.FileOutputStream
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
        val BUFFER_SIZE = 4096
        return try {
            val conn = URL(link).openConnection() as HttpURLConnection
            if (conn.responseCode != HttpURLConnection.HTTP_OK) {
                callback?.onFailure()
                return null
            }
            conn.requestMethod = "HEAD"
            val timestamp = conn.lastModified
            conn.requestMethod = "GET"

            val downloadDir = createDirectory(getTempDirectory())
            val inputStream = conn.inputStream
            val filename = getTempDirectory() + link
            val outputStream = FileOutputStream(filename);
            var bytesRead: Int = -1
            val buffer = ByteArray(BUFFER_SIZE)
            var count: Int
            while (inputStream.read(buffer).also { count = it } > 0) {
                outputStream.write(buffer, 0, count)
            }
            inputStream.close()
            outputStream.close()
            conn.disconnect()

            val bitmap = BitmapFactory.decodeStream(conn.inputStream)
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