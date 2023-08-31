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
        val hash = md5(fileNameLink)
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
                }
            }
        } catch (_: Exception) {
        }

        if (bitmap == null) {
            bitmap = loadBitmap(filename, reduce)
            if (bitmap == null) {
                bitmap = EMPTY_IMAGE.asAndroidBitmap()
                source = Source.NONE
            } else
                source = Source.DRIVE_CACHE
        }
        callback(ExtBitmap(bitmap, source), fileTimestamp)
        return ExtBitmap(null, Source.NONE)
    }

    override fun call(): ExtBitmap {
        return download(this.link)
    }
}