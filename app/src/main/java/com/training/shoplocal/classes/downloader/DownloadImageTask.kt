package com.training.shoplocal.classes.downloader

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import java.net.HttpURLConnection
import java.net.URL

class DownloadImageTask(private val link: String,
                        private val cache: DiskCache,
                        private val callback: Callback): DownloadTask<Bitmap?>
{
    private val uiHandler = Handler(Looper.getMainLooper())
    override fun download(link: String): Bitmap? {
        return try {
            val conn = URL(link).openConnection() as HttpURLConnection
            conn.requestMethod = "HEAD"
            val timestamp = conn.lastModified
            var bitmap = cache.get(link, timestamp)
            if (bitmap == null) {
                bitmap = BitmapFactory.decodeStream(conn.inputStream)
                cache.put(link, bitmap, timestamp)
            }
            conn.disconnect()
            bitmap
        } catch (_: Exception) {
            null
        }
    }

    override fun call(): Bitmap? {
        val image = download(this.link)
        uiHandler.post { // обновление View только в главном потоке
            if (image == null)
                callback.onFailure()
            else
                callback.onComplete(image)
        }
        return image
    }
}