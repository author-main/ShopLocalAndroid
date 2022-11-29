package com.training.shoplocal.classes.downloader

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import com.training.shoplocal.*
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

class DownloadImageTask(private val link: String, val callback: (bitmap: Bitmap?, timestamp: Long) -> Unit): DownloadTask<Bitmap?>
{
    private var cacheTimestamp: Long = 0L
    fun setCacheTimestamp(timestamp: Long){
        cacheTimestamp   = timestamp
    }
    override fun download(link: String): Bitmap? {
        val BUFFER_SIZE = 4096
        val hash = md5(link)
        var bitmap: Bitmap? = null
        val filename    = getCacheDirectory() + hash//md5(link)
        val filenameTmp = "$filename.$EXT_CACHETEMPFILE"
        val conn = URL(link).openConnection() as HttpURLConnection
        var fileTimestamp = cacheTimestamp
        try {
            conn.connect()
            if (conn.responseCode == HttpURLConnection.HTTP_OK) {
                conn.requestMethod = "HEAD"
                val timestamp = conn.lastModified
                if (timestamp != cacheTimestamp) {
                    //df67318f8d0816b3b2ea29505075262a 1666444506000
                    conn.requestMethod = "GET"
                    val inputStream = conn.inputStream
                    val outputStream = FileOutputStream(filenameTmp);
                    val buffer = ByteArray(BUFFER_SIZE)
                    var count: Int
                    while (inputStream.read(buffer).also { count = it } > 0) {
                        outputStream.write(buffer, 0, count)
                    }
                    inputStream.close()
                    outputStream.close()
                    conn.disconnect()
                    bitmap = loadBitmap(filenameTmp)//decodeStream(conn.inputStream)
                    renameFile(filenameTmp, filename)
                    fileTimestamp = timestamp
                    //log("$hash - загружено из Инет")
                }
            }
        } catch (_: Exception) {}
        if (bitmap == null) {
          //  log("$hash - загружено из кэша")
            bitmap = loadBitmap(filename)
        }

        /*Handler(Looper.getMainLooper()).post {
            Thread.sleep(3000)
            callback(bitmap, fileTimestamp)
        }*/
        //for (i in 1..1000000000){}
        callback(bitmap, fileTimestamp)
        return null//bitmap
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