package com.training.shoplocal.classes.downloader

import android.graphics.Bitmap
import com.training.shoplocal.*
import com.training.shoplocal.classes.EMPTY_STRING
import java.util.concurrent.Executors
import java.util.concurrent.Future
import javax.inject.Inject
import javax.inject.Singleton

enum class Source {
    NONE,         // ошибка загрузки
    MEMORY_CACHE, // загружено из кеша памяти
    DRIVE_CACHE,  // загружено из кеша устройства
    SERVER        // загружено с сервера
}
data class ExtBitmap(var bitmap: Bitmap?, var source: Source)

@Singleton
class ImageLinkDownloader @Inject constructor( // private constructor(
    private val diskCache   : ImageDiskCache,
    private val memoryCache : ImageMemoryCache
) {
    private val executorService =
        Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())
    private val listDownloadTask: HashMap<String, Future<ExtBitmap>> = hashMapOf()
    private fun normalizeJournal() {
        diskCache.normalizeJournal()
    }

    @Synchronized
    fun downloadImage(link: String?, reduce: Boolean = false, callback: Callback) {

        if (link.isNullOrEmpty()) {
            callback.onFailure()
            return
        }
        val md5link = md5(fileNameFromPath(link))
        val md5MemoryLink = md5link + if (reduce) EMPTY_STRING else "_"
        val bitmapMemory = memoryCache.get(md5MemoryLink)
        if (bitmapMemory != null) {
            val extBitmap = ExtBitmap(bitmapMemory, Source.MEMORY_CACHE)
            callback.onComplete(extBitmap)
            return
        }
        val iterator = listDownloadTask.iterator()
        while (iterator.hasNext()) {
            if (iterator.next().value.isDone) iterator.remove()
        }
        diskCache.put(link)
        val timestamp = diskCache.getTimestamp(link)// ?: 0L
        val task = DownloadImageTask(link, reduce) { extBitmap, fileTimestamp ->
            if (extBitmap.source != Source.NONE)
            {
                extBitmap.bitmap?.let {uploaded ->
                    memoryCache.put(md5MemoryLink, uploaded)
                }
                val filesize = getFileSize("${diskCache.getCacheDirectory()}$md5link")
                    if (timestamp != fileTimestamp) {
                        if (diskCache.placed(filesize))
                            diskCache.update(link, StateEntry.CLEAN, fileTimestamp)
                        else
                            diskCache.remove(link, changeState = true)
                    }
                callback.onComplete(extBitmap)
            } else {
                diskCache.remove(link, changeState = true)
                callback.onFailure()
            }
            normalizeJournal()
        }.apply {
            setCacheTimestamp(
                timestamp = diskCache.getTimestamp(link)
            )
        }
        listDownloadTask[link] = executorService.submit(task)
    }

    fun cancelAll() {
        if (listDownloadTask.isEmpty()) return
        synchronized(this) {
            val iteratorTask = listDownloadTask.iterator()
            while (iteratorTask.hasNext()) {
                val entity = iteratorTask.next()
                val deleteCacheFile = !entity.value.isDone
                entity.value.cancel(true)
                if (deleteCacheFile)
                    diskCache.remove(entity.key, changeState = true)
                iteratorTask.remove()
            }
        }
    }
}