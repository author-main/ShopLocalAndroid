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
    /*init {
        appComponent.injectImageDownloader(this)
    }*/
    /*private val diskCache:ImageDiskCache = DiskCache(CACHE_DIR)
    private val memoryCache: ImageMemoryCache = MemoryCache(8)*/
    //private var cachedir: String? = null
    //private var cacheStorage: ImageCache? = null
    //private var cachedir = DiskCache.getCacheDir()
    private val executorService =
        Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())
    private val listDownloadTask: HashMap<String, Future<ExtBitmap>> = hashMapOf()
    private fun normalizeJournal() {
        diskCache.normalizeJournal()
    }

  /*  private fun setCacheDirectory(value: String) {
        if (createDirectory(value)) {
            cachedir = value
            cacheStorage = DiskCache(cachedir!!)
        }
    }*/

//    @Synchronized

    @Synchronized
    fun downloadImage(link: String?, reduce: Boolean = false, callback: Callback) {

        if (link.isNullOrEmpty()) {
            callback.onFailure()
            return
        }
        val md5link = md5(fileNameFromPath(link))
        val md5MemoryLink = md5link + if (reduce) EMPTY_STRING else "_"
        //val reduceSym = if (reduce) EMPTY_STRING else "_"
        val bitmapMemory = memoryCache.get(md5MemoryLink)
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


        diskCache.put(link)
        val timestamp = diskCache.getTimestamp(link)// ?: 0L
        val task = DownloadImageTask(link, reduce) { extBitmap, fileTimestamp ->
            if (extBitmap.source != Source.NONE)
            {
                extBitmap.bitmap?.let {uploaded ->
                    memoryCache.put(md5MemoryLink, uploaded)
                }
                val filesize = getFileSize("${diskCache.getCacheDirectory()}$md5link")
                //DiskCache.let { storage ->
                    if (timestamp != fileTimestamp) {
                        if (diskCache.placed(filesize))
                            diskCache.update(link, StateEntry.CLEAN, fileTimestamp)
                        else
                            diskCache.remove(link, changeState = true)
                    }
               // }
               // log("complete downloadimagetask $link")
                callback.onComplete(extBitmap)
                //normalizeJournal()
            } else {
                diskCache.remove(link, changeState = true)
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
                timestamp = diskCache.getTimestamp(link)
            )
        }
        listDownloadTask[link] = executorService.submit(task)
    }

  /*  fun cancelTask(link: String) {
        synchronized(this) {
            listDownloadTask[link]?.let { task ->
                if (!task.isDone) {
                    diskCache.remove(link, changeState = true)//, true)
                    task.cancel(true)
                }
            }
            listDownloadTask.remove(link)
        }
    }*/

    fun cancelAll() {

        if (listDownloadTask.isEmpty()) return
        //log("before ${listDownloadTask.size}")
        synchronized(this) {
            //executorService.shutdownNow()
            val iteratorTask = listDownloadTask.iterator()
            while (iteratorTask.hasNext()) {
                val entity = iteratorTask.next()
                //log ("entity = ${entity.key}")
                val deleteCacheFile = !entity.value.isDone
                /*if (!entity.value.isDone)
                    diskCache.remove(entity.key, changeState = true)//, true)*/
                entity.value.cancel(true)
                if (deleteCacheFile)
                    diskCache.remove(entity.key, changeState = true)
                iteratorTask.remove()
            }
            //log("after ${listDownloadTask.size}")
            //log ("count tasks = ${listDownloadTask.size}")

           /* listDownloadTask.forEach {
                if (!it.value.isDone) {
                    diskCache.remove(it.key, changeState = true)//, true)
                    it.value.cancel(true)
                }
            }*/
          //  listDownloadTask.clear()
        }
    }

  /*  companion object {
        private lateinit var instance: ImageLinkDownloader
        private fun getInstance(): ImageLinkDownloader =
            if (this::instance.isInitialized)
                instance
            else {
                log("recreate instance...")
                ImageLinkDownloader(
                    DiskCache(CACHE_DIR),
                    MemoryCache(8)
                )
            }

        fun downloadImage(link: String?, reduce: Boolean = false, callback: Callback) {
            if (link.isNullOrEmpty())
                callback.onFailure()
            else
                getInstance().downloadImage(link, reduce, callback)
        }

        fun cancel(){
            getInstance().cancelAll()
        }
    }*/
}