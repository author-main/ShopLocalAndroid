package com.training.shoplocal.classes.downloader

import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import com.training.shoplocal.*
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors
import java.util.concurrent.Future

class ImageLinkDownloader private constructor(){
    private var cachedir: String? = null
    private var cacheStorage: ImageCache? = null
    private val executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())
    private val listDownloadTask:HashMap<String, Future<Bitmap?>> = hashMapOf()
    private fun normalizeJournal(){
        cacheStorage?.normalizeJournal()
    }
    private fun setCacheDirectory(value: String) {
        if (createDirectory(value)) {
            cachedir = value
            cacheStorage = DiskCache(cachedir!!)
        }
    }
    @Synchronized
    private fun downloadImage(link: String, callback: Callback){
        val iterator = listDownloadTask.iterator()
        while (iterator.hasNext()) {
            if (iterator.next().value.isDone) iterator.remove()
        }
        cacheStorage?.put(link)
        val timestamp = cacheStorage?.getTimestamp(link) ?: 0L
        val task = DownloadImageTask(link){ bitmap, fileTimestamp ->
            bitmap?.let {
                val filesize = getFileSize("$cachedir${md5(link)}")
                cacheStorage?.let{ storage ->
                    if (timestamp != fileTimestamp) {
                        if (storage.placed(filesize))
                            storage.update(link, StateEntry.CLEAN, fileTimestamp)
                        else
                            storage.remove(link, changeState = true)
                    }
                }
                callback.onComplete(it)
                normalizeJournal()
            } ?: run {
                cacheStorage?.remove(link, changeState = true)
                callback.onFailure()
                normalizeJournal()
            }
        }.apply {
            //    val time = cacheStorage?.getTimestamp(link)
                setCacheTimestamp(
                    timestamp = cacheStorage?.getTimestamp(link) ?: 0L
                )
        }
        listDownloadTask[link] = executorService.submit(task)
    }

    fun cancelTask(link: String){
        synchronized(this){
            listDownloadTask[link]?.let { task ->
                if (!task.isDone) {
                    cacheStorage?.remove(link, changeState = true)
                    task.cancel(true)
                }
            }
            listDownloadTask.remove(link)
        }
    }

    fun cancelAll() {
        if (listDownloadTask.isEmpty()) return
        synchronized (this) {
            executorService.shutdownNow()
            listDownloadTask.forEach{
                if ( !it.value.isDone) {
                    cacheStorage?.remove(it.key, changeState = true)
                    it.value.cancel(true)
                }
            }
            listDownloadTask.clear()
        }
    }

    companion object {
        private var instance: ImageLinkDownloader? = null
        private fun getInstance(): ImageLinkDownloader =
            instance ?: ImageLinkDownloader().apply {
                setCacheDirectory(getCacheDirectory())
            }
        fun downloadImage(link: String, callback: Callback) {
            getInstance().downloadImage(link, callback)
        }
    }





/*
    @Synchronized
    private fun normalizeJournal(){
        cacheStorage?.normalizeJournal()
    }

    @Synchronized
    fun downloadLinkImage(link: String, callback: Callback){
        val iterator = listDownloadTask.iterator()
        while (iterator.hasNext()) {
            if (iterator.next().value.isDone) iterator.remove()
        }
        val timestamp = cacheStorage?.getTimestamp(link)


        val task = DownloadImageTask(link, timestamp){ bitmap ->
            bitmap?.let {
                log("$link, loaded from Internet")
                val filesize = getFileSize("$cacheStorage${md5(link)}")
                cacheStorage?.let{ storage ->
                    if (storage.placed(filesize))
                        storage.put(link, timestamp)
                    else
                        storage.remove(link, changeState = true)
                }
                callback.onComplete(it)
                normalizeJournal()
            } ?: run {
                cacheStorage?.remove(link, changeState = true)
                callback.onFailure()
                normalizeJournal()
            }
        }
        listDownloadTask[link] = executorService.submit(task)



  /*


        //var timestamp = 0L
      /*  val conn = URL(link).openConnection() as HttpURLConnection
        conn.connect()
        //Handler(Looper.getMainLooper()).post {
        val responseCode = conn.responseCode
        timestamp = if (responseCode == HttpURLConnection.HTTP_OK) {
                      conn.requestMethod = "HEAD"
                      conn.lastModified
                    } else
                      0L
       conn.disconnect()*/


        /*val hash = md5(link)
        log( "***** $hash, timestamp = $timestamp *****")*/
        val image: Bitmap? = cacheStorage?.get(link, timestamp)
        image?.let{
            log( "$link, loaded from cache")
            callback.onComplete(it)
            cacheStorage?.update(link, StateEntry.CLEAN)
            return
        } ?: run {
            cacheStorage?.remove(link, changeState = true)
        }

        // если идет загрузка файла или соединение не установлено
        if (listDownloadTask[link] != null || timestamp == 0L)
            return

        cacheStorage?.put(link)
        val task = DownloadImageTask(link){ bitmap ->
            bitmap?.let {
                log("$link, loaded from Internet")
                val filesize = getFileSize("$cacheStorage${md5(link)}")
                cacheStorage?.let{ storage ->
                    if (storage.placed(filesize))
                        storage.put(link, timestamp)
                    else
                        storage.remove(link, changeState = true)
                }
                callback.onComplete(it)
                normalizeJournal()
            } ?: run {
                cacheStorage?.remove(link, changeState = true)
                callback.onFailure()
                normalizeJournal()
            }
        }
        listDownloadTask[link] = executorService.submit(task)
    }

    fun cancelTask(link: String){
        synchronized(this){
            listDownloadTask[link]?.let { task ->
                if (!task.isDone) {
                    cacheStorage?.remove(link, changeState = true)
                    task.cancel(true)
                }
            }
            listDownloadTask.remove(link)
        }
    }

    fun cancelAll() {
        if (listDownloadTask.isEmpty()) return
        synchronized (this) {
            executorService.shutdownNow()
            listDownloadTask.forEach{
                if ( !it.value.isDone) {
                    it.value.cancel(true)
                    cacheStorage?.remove(it.key, changeState = true)
                }
            }
            listDownloadTask.clear()
        }
    }

    private fun setCacheDirectory(dir: String){
        if (cacheStorage == null)
            cacheStorage = DiskCache(dir)
    }

    companion object {
        private var instance: ImageLinkDownloader? = null
        private fun getInstance(): ImageLinkDownloader =
            instance ?: ImageLinkDownloader().apply {
                setCacheDirectory(getCacheDirectory())
            }

        fun downloadImage(link: String, callback: Callback) {
            getInstance().downloadLinkImage(link, callback)
        }
    }*/*/
}

/*
fun getUrlFileLength(url: String): Long {
    return try {
        val urlConnection = URL(url).openConnection() as HttpURLConnection
        urlConnection.requestMethod = "HEAD"
        //Last-Modified
        urlConnection.getHeaderField("content-length")?.toLongOrNull()?.coerceAtLeast(-1L)
            ?: -1L
    } catch (ignored: Exception) {
        -1L
    }
}*/