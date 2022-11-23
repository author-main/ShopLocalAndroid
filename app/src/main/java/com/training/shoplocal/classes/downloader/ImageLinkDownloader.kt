package com.training.shoplocal.classes.downloader

import android.graphics.Bitmap
import com.training.shoplocal.*
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors
import java.util.concurrent.Future

class DiskCache(private val cacheDir: String): ImageCache {
    private val existsCacheStorage = createDirectory(cacheDir)
    private val journal = Journal.getInstance(cacheDir)

    override fun placed(filesize: Long): Boolean{
        if (journal.getCacheSize() + filesize < CACHE_SIZE)
            return true
        if (filesize > CACHE_SIZE)
            return false
        val listHash = journal.leavingCacheFiles(CACHE_SIZE - filesize)
        if (listHash.isEmpty())
            return false
        listHash.forEach {hash ->
            deleteCacheFile(hash)
            journal.remove(hash, changeState = false)
        }
        return true
    }

    /**
     *  Перезаписать журнал значениями из entries<String, CachEntry>
     */
    override fun normalizeJournal() {
        val listHashForDelete = journal.saveEntriesToJournal()
        listHashForDelete.forEach {hash ->
            deleteCacheFile(hash)
        }
    }


    private fun getHashCacheFile(link: String): String =
        md5(link)


    override fun get(link: String, timestamp: Long): Bitmap? {
        val hash = getHashCacheFile(link)
        journal.update(hash, StateEntry.DIRTY)
        var bitmap: Bitmap? = null
        if (timestamp > 0L) {
            if  (journal.equals(hash, timestamp)) {
                bitmap = loadBitmap(getFileNameFromHash(hash))
            } /*else
                journal.remove(hash, changeState = true)*/
        } else {
            bitmap = loadBitmap(getFileNameFromHash(hash))
        }
        return bitmap
    }

    override fun put(link: String, time: Long) {
        val state = if (time == 0L)
                        StateEntry.DIRTY
                    else
                        StateEntry.CLEAN
        journal.put(getHashCacheFile(link), state, time)
    }

    override fun remove(link: String, changeState: Boolean) {
        val hash = getHashCacheFile(link)
        journal.remove(hash, changeState)
        if (!changeState)
            deleteCacheFile(hash)
    }

    override fun update(link: String, state: StateEntry) {
        val hash = getHashCacheFile(link)
        journal.update(link, StateEntry.DIRTY)
    }

    override fun clear() {
        deleteFiles(cacheDir)
        journal.clear()
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

        val conn = URL(link).openConnection() as HttpURLConnection
        val timestamp = if (conn.responseCode != HttpURLConnection.HTTP_OK) {
                            conn.requestMethod = "HEAD"
                            conn.lastModified
                        } else
                            0L
        conn.disconnect()

        val hash = md5(link)
        log( "***** $hash, timestamp = $timestamp *****")

        val image: Bitmap? = cacheStorage?.get(link, timestamp)
        image?.let{
            log( "$hash, loaded from cache")
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
                log("$hash, loaded from Internet")
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