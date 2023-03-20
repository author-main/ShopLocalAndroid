package com.training.shoplocal.classes.downloader

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import androidx.compose.ui.graphics.asAndroidBitmap
import com.training.shoplocal.*
import com.training.shoplocal.classes.EMPTY_IMAGE
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

class DownloadImageTask(private val link: String, private val reduce: Boolean, val callback: (extBitmap: ExtBitmap, timestamp: Long) -> Unit): DownloadTask<ExtBitmap>
{
    private var cacheTimestamp: Long = 0L
    fun setCacheTimestamp(timestamp: Long){
        cacheTimestamp   = timestamp
    }
    override fun download(link: String): ExtBitmap {
        val fileNameLink = fileNameFromPath(link)
        var source = Source.SERVER
        val BUFFER_SIZE = 32768
        //val hash = md5(link)
        val hash = md5(fileNameLink)
        var bitmap: Bitmap? = null
        val filename    = getCacheDirectory() + hash//md5(link)
        val filenameTmp = "$filename.$EXT_CACHETEMPFILE"
        /*log("filenamelink = $fileNameLink")
        log("filename = $filename")
        log("filenametmp = $filenameTmp")*/


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
                    val outputStream = FileOutputStream(filenameTmp)
                    val buffer = ByteArray(BUFFER_SIZE)
                    var count: Int
                    while (inputStream.read(buffer).also { count = it } > 0) {
                        outputStream.write(buffer, 0, count)
                    }

                    inputStream.close()
                    outputStream.flush()
                    outputStream.close()
                    conn.disconnect()
                    bitmap = loadBitmap(filenameTmp, reduce)//decodeStream(conn.inputStream)
                    renameFile(filenameTmp, filename)
                    fileTimestamp = timestamp
                //    log("$hash - загружено из Инет")
                }
            }
        } catch (_: Exception) {
            //log("error image download")
        }

        if (bitmap == null) {
            bitmap = loadBitmap(filename, reduce)
            if (bitmap == null) {
                bitmap = EMPTY_IMAGE.asAndroidBitmap()
                source = Source.NONE
            } else
                source = Source.DRIVE_CACHE
        }

/*        if (bitmap == null) {
            bitmap = EMPTY_IMAGE.asAndroidBitmap()
            source = Source.NONE
        }*/

        /*Handler(Looper.getMainLooper()).post {
            Thread.sleep(3000)
            callback(bitmap, fileTimestamp)
        }*/
       // for (i in 1..1000000000){}
        //log("$link downloaded from task")
        callback(ExtBitmap(bitmap, source), fileTimestamp)
        return ExtBitmap(null, Source.NONE)
    }

    override fun call(): ExtBitmap {
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