package com.training.shoplocal.classes.downloader

import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import com.training.shoplocal.*
import com.training.shoplocal.classes.EMPTY_IMAGE
import com.training.shoplocal.classes.EMPTY_STRING
import kotlinx.coroutines.delay
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors
import java.util.concurrent.Future

enum class Source {
    NONE,         // ошибка загрузки
    MEMORY_CACHE, // загружено из кеша памяти
    DRIVE_CACHE,  // загружено из кеша устройства
    SERVER        // загружено с сервера
}
data class ExtBitmap(var bitmap: Bitmap?, var source: Source)

class ImageLinkDownloader private constructor() {
    //private var cachedir: String? = null
    //private var cacheStorage: ImageCache? = null
    //private var cachedir = DiskCache.getCacheDir()
    private val executorService =
        Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())
    private val listDownloadTask: HashMap<String, Future<ExtBitmap>> = hashMapOf()
    private fun normalizeJournal() {
        DiskCache.normalizeJournal()
    }

  /*  private fun setCacheDirectory(value: String) {
        if (createDirectory(value)) {
            cachedir = value
            cacheStorage = DiskCache(cachedir!!)
        }
    }*/

    @Synchronized
    private fun downloadImage(link: String, reduce: Boolean, callback: Callback) {
        val md5link = md5(fileNameFromPath(link))
        val md5MemoryLink = md5link + if (reduce) EMPTY_STRING else "_"
        //val reduceSym = if (reduce) EMPTY_STRING else "_"
        val bitmapMemory = MemoryCache.get(md5MemoryLink)
        if (bitmapMemory != null) {
           // log ("$link from memory cache")
            val extBitmap = ExtBitmap(bitmapMemory, Source.MEMORY_CACHE)
            callback.onComplete(extBitmap)
            return
        }
        /*Handler(Looper.getMainLooper()).post {
            Thread.sleep(4000)
        }*/
        val iterator = listDownloadTask.iterator()
        while (iterator.hasNext()) {
            if (iterator.next().value.isDone) iterator.remove()
        }


        DiskCache.put(link)
        val timestamp = DiskCache.getTimestamp(link) ?: 0L
        val task = DownloadImageTask(link, reduce) { extBitmap, fileTimestamp ->
            if (extBitmap.source != Source.NONE)
            {
                extBitmap.bitmap?.let {uploaded ->
                    MemoryCache.put(md5MemoryLink, uploaded)
                }
                val filesize = getFileSize("${DiskCache.getCacheDir()}$md5link")
                //DiskCache.let { storage ->
                    if (timestamp != fileTimestamp) {
                        if (DiskCache.placed(filesize))
                            DiskCache.update(link, StateEntry.CLEAN, fileTimestamp)
                        else
                            DiskCache.remove(link, changeState = true)
                    }
               // }
               // log("complete downloadimagetask $link")
                callback.onComplete(extBitmap)
                //normalizeJournal()
            } else {
                DiskCache.remove(link, changeState = true)
                callback.onFailure()
            }


                /*?: run {
                DiskCache.remove(link, changeState = true)
                callback.onFailure()
                //normalizeJournal()
            }*/
            //log("md5 = $md5link")
            normalizeJournal()
        }.apply {
            //    val time = DiskCache.getTimestamp(link)
            setCacheTimestamp(
                timestamp = DiskCache.getTimestamp(link) ?: 0L
            )
        }
        listDownloadTask[link] = executorService.submit(task)
    }

    fun cancelTask(link: String) {
        synchronized(this) {
            listDownloadTask[link]?.let { task ->
                if (!task.isDone) {
                    DiskCache.remove(link, changeState = true)//, true)
                    task.cancel(true)
                }
            }
            listDownloadTask.remove(link)
        }
    }

    fun cancelAll() {
        if (listDownloadTask.isEmpty()) return
        synchronized(this) {
            executorService.shutdownNow()
            listDownloadTask.forEach {
                if (!it.value.isDone) {
                    DiskCache.remove(it.key, changeState = true)//, true)
                    it.value.cancel(true)
                }
            }
            listDownloadTask.clear()
        }
    }

    companion object {
        private var instance: ImageLinkDownloader? = null
        private fun getInstance(): ImageLinkDownloader =
            instance ?: ImageLinkDownloader()/*.apply {
                setCacheDirectory(getCacheDirectory())
            }*/


        fun downloadImage(link: String?, reduce: Boolean = false, callback: Callback) {
            if (link.isNullOrEmpty())
                callback.onFailure()
            else
                getInstance().downloadImage(link, reduce, callback)
        }

      /*  fun downloadCardImage(link: String?, callback: Callback) {
            downloadImage(link, reduce = true, callback)
        /*if (link.isNullOrEmpty())
                callback.onFailure()
            else
                getInstance().downloadImage(link, reduce = true, callback)*/
        }*/

        fun cancel(){
            getInstance().cancelAll()
        }
    }
}