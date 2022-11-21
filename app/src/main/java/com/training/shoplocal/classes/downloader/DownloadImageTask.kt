package com.training.shoplocal.classes.downloader

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.training.shoplocal.*
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

            val filename = getCacheDirectory() + md5(link) + ".$EXT_CACHETEMPFILE"
            val inputStream = conn.inputStream
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

            //val bitmap = BitmapFactory.decodeStream(conn.inputStream)
            val option = BitmapFactory.Options()
            option.inPreferredConfig = Bitmap.Config.ARGB_8888
            val bitmap = BitmapFactory.decodeFile(filename, option)//decodeStream(conn.inputStream)
            bitmap?.let{
                callback?.onComplete(BitmapTime(it, timestamp))
            } ?: callback?.onFailure()
            bitmap
        } catch (_: Exception) {
            callback?.onFailure()
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