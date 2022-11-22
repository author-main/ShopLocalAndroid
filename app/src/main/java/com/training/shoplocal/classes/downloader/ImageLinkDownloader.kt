package com.training.shoplocal.classes.downloader

import android.graphics.Bitmap
import com.training.shoplocal.*
import java.util.concurrent.Executors
import java.util.concurrent.Future

class DiskCache(private val cacheDir: String): ImageCache {
    private val existsCacheStorage = createDirectory(cacheDir)
    private val journal = Journal.getInstance(cacheDir)

    @Synchronized
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

    override fun get(link: String): Bitmap? =
        loadBitmap(getFileNameFromHash(getHashCacheFile(link)))

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

        val image: Bitmap? = cacheStorage?.get(link)
        image?.let{
            cacheStorage?.update(link, StateEntry.DIRTY)
            callback.onComplete(it)
            cacheStorage?.update(link, StateEntry.CLEAN)
            return
        } ?: run {
            cacheStorage?.remove(link, changeState = true)
        }

        if (listDownloadTask[link] != null) // если идет загрузка файла
            return

        cacheStorage?.put(link)
        val task = DownloadImageTask(link){ bitmap ->
            bitmap?.let {
                val filesize = getFileSize("$cacheStorage${md5(link)}")
                cacheStorage?.let{ storage ->
                    if (storage.placed(filesize))
                        storage.put(link, it)
                    else
                        storage.remove(link, changeState = true)
                }
                callback.onComplete(it.bitmap)
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