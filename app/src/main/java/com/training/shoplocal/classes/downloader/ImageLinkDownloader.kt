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

class ImageLinkDownloader private constructor() {
    private var cachedir: String? = null
    private var cacheStorage: ImageCache? = null
    private val executorService =
        Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())
    private val listDownloadTask: HashMap<String, Future<Bitmap?>> = hashMapOf()
    private fun normalizeJournal() {
        cacheStorage?.normalizeJournal()
    }

    private fun setCacheDirectory(value: String) {
        if (createDirectory(value)) {
            cachedir = value
            cacheStorage = DiskCache(cachedir!!)
        }
    }

    @Synchronized
    private fun downloadImage(link: String, callback: Callback) {
        val iterator = listDownloadTask.iterator()
        while (iterator.hasNext()) {
            if (iterator.next().value.isDone) iterator.remove()
        }
        cacheStorage?.put(link)
        val timestamp = cacheStorage?.getTimestamp(link) ?: 0L
        val task = DownloadImageTask(link) { bitmap, fileTimestamp ->
            bitmap?.let {
                val filesize = getFileSize("$cachedir${md5(link)}")
                cacheStorage?.let { storage ->
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

    fun cancelTask(link: String) {
        synchronized(this) {
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
        synchronized(this) {
            executorService.shutdownNow()
            listDownloadTask.forEach {
                if (!it.value.isDone) {
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

        fun downloadImage(link: String?, callback: Callback) {
            if (link.isNullOrEmpty())
                callback.onFailure()
            else
                getInstance().downloadImage(link, callback)
        }
    }
}