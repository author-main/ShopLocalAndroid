package com.training.shoplocal.classes.downloader

import android.graphics.Bitmap
import com.training.shoplocal.*
import java.util.concurrent.Executors
import java.util.concurrent.Future

class DiskCache(private val cacheDir: String): ImageCache {
    private val existsCacheStorage = createDirectory(cacheDir)
    private val journal = Journal.getInstance(cacheDir)
    private var size = journal.getCacheSize()

    /**
     *  Перезаписать журнал значениями из entries<String, CachEntry>
     */
    override fun journal() {
        journal.saveEntriesToJournal()
    }

    private fun getHashCacheFile(link: String): String =
        md5(link)

    override fun get(link: String): Bitmap? {
        TODO("Not yet implemented")
    }

    override fun put(link: String, image: BitmapTime?) {
        val state = if (image != null)
                        StateEntry.CLEAN
                    else
                        StateEntry.DIRTY
        val time = image?.time ?: 0L
        journal.put(getHashCacheFile(link), state, time)
    }

    override fun remove(link: String, changeState: Boolean) {
        val hash = getHashCacheFile(link)
        journal.remove(hash, changeState)
        if (!changeState)
            deleteCacheFile(hash)
    }

    override fun update(link: String, state: StateEntry) {
        TODO("Not yet implemented")
    }

    override fun clear() {
        TODO("Not yet implemented")
    }

    private fun getFileNameFromHash(hash: String) =
        "$cacheDir$hash"

    private fun deleteCacheFile(hash: String){
        val filename = getFileNameFromHash(hash)
        deleteFile(filename)
        deleteFile("${filename}.$EXT_CACHETEMPFILE")
    }
}

class ImageLinkDownloader private constructor(){
    private var cacheStorage: ImageCache? = null
    private val executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())
    private val listDownloadTask:HashMap<String, Future<Bitmap?>> = hashMapOf()

    fun downloadLinkImage(link: String, callback: Callback){
        val iterator = listDownloadTask.iterator()
        while (iterator.hasNext()) {
            if (iterator.next().value.isDone) iterator.remove()
        }

        val image: Bitmap? = cacheStorage?.get(link)
        image?.let{
            cacheStorage?.update(link, StateEntry.DIRTY)
            callback.onComplete(it)
            return
        }

        if (listDownloadTask[link] != null) // если идет загрузка файла
            return

        cacheStorage?.put(link)
        val task = DownloadImageTask(link){ bitmap ->
            bitmap?.let {
                cacheStorage?.put(link, it)
                callback.onComplete(it.bitmap)
            } ?: run {
                cacheStorage?.remove(link, changeState = true)
                callback.onFailure()
            }
        }
        listDownloadTask[link] = executorService.submit(task)
    }

    fun cancelTask(link: String){
        synchronized(this){
            listDownloadTask[link]?.let { task ->
                if (!task.isDone)
                    task.cancel(true)
            }
            listDownloadTask.remove(link)
        }
    }

    fun cancelAll() {
        if (listDownloadTask.isEmpty()) return
        synchronized (this) {
            executorService.shutdownNow()
            listDownloadTask.forEach{
                if ( !it.value.isDone)
                    it.value.cancel(true)
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
    }
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