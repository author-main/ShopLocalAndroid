package com.training.shoplocal.classes.downloader

import android.graphics.Bitmap
import com.training.shoplocal.*
import java.io.*
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.concurrent.Executors
import java.util.concurrent.Future

class DiskCache(private val cacheDir: String): ImageCache {
    private val existsCacheStorage = createDirectory(cacheDir)
    private val journal = Journal.getInstance(cacheDir, object: OnDeleteCacheFile{
        override fun deleteCacheFile(hash: String) {
            deleteCacheFiles(hash)
        }
    })
    private var size = journal.getCacheSize()
    /**
     *  Изменение размера кэша в соответствии с максимальным размером кэша CACHE_SIZE
     *  @param insize размер нового файла, помещаемого в кэш
     *  @param inhash хэш нового файла, помещаемого в кэш
     *  @return Boolean - true файл может быть размещен в кэше
     */
    @Synchronized
    private fun trimCache(inhash: String, insize: Long): Boolean{
        if (size + insize < CACHE_SIZE) {
            size += insize
            return true
        }
        val limit = CACHE_SIZE - insize
        val hashList = journal.getHashList(excludeHash = inhash)
        val outHashList = mutableListOf<String>()
        var placed = false
        var calcSize = size
        for (hash in hashList) {
            calcSize -= journal.getEntrySize(hash)
            outHashList.add(hash)
            if (calcSize <= limit) {
                placed = true
                size = calcSize + insize
                journal.remove(outHashList)
                break
            }
        }
        return placed
    }

    private fun deleteCacheFiles(hash: String){
        val filename = cacheDir + hash
        deleteFile(filename)
        deleteFile("filename.${EXT_CACHETEMPFILE}")
    }


    /*private fun md5(link: String): String {
        try {
            val md = MessageDigest.getInstance("MD5")
            val messageDigest = md.digest(link.toByteArray())
            var hashtext = BigInteger(1, messageDigest).toString(16)
            *//*try {
                hashtext = "0".repeat(HASH_LENGTH - hashtext.length) + hashtext
            } catch (_: IllegalArgumentException) {}*//*
            while (hashtext.length < HASH_LENGTH)
                hashtext = "0$hashtext"
            return hashtext
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException(e)
        }
    }*/

    /*private fun getCacheSize(): Long {
        var size: Long = 0
        File(cacheDir).listFiles(FileFilter { it.extension != EXT_CACHETEMPFILE} )?.forEach { file ->
            size += file.length()
        }
        return size
    }*/

    private fun getLinkHash(link: String): String =
        md5(link)


    override fun get(link: String): Bitmap? {
        val hash = getLinkHash(link)
        return null
    }

    override fun put(link: String, image: BitmapData?) {
        val hash = md5(link)
        val filecache = cacheDir + hash
        if (image != null) {
            if (trimCache(hash, image.length)) {
                renameFile("$filecache.$EXT_CACHETEMPFILE", filecache)
                journal.add(hash, image)
            }
            else {
                journal.remove(hash, false)
            }
        } else
            journal.add(hash)
    }

    /**
     *  Удаление записи из файла журнала
     *  @param link имя файла
     *  @param changeState true - запись в файле журнала будет помечена state = REMOVE,
     *  false - запись журнала и файлы кэша будут удалены. По умолчанию: true
     */
    override fun remove(link: String, changeState: Boolean) {
        val hash = getLinkHash(link)
        size -= journal.remove(hash, changeState)
    }

    override fun update(link: String, state: StateEntry) {
        journal.update(getLinkHash(link), state)
    }

    override fun clear() {
        size = 0L
        journal.clear()
    }
}

class ImageLinkDownloader private constructor(){
    private var cacheStorage: ImageCache? = null
    private val executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())
    private val listDownloadTask:HashMap<String, Future<Bitmap?>> = hashMapOf()

    fun downloadLinkImage(link: String, callback: Callback){
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
                cacheStorage?.remove(link)
                callback.onFailure()
            }
        }
        listDownloadTask[link] = executorService.submit(task)
    }

    fun cancelTask(link: String){
        synchronized(this){
            listDownloadTask.forEach {
                if (it.key == link &&  !it.value.isDone)
                    it.value.cancel(true)
            }
        }
    }

    fun cancelAll() {
        if (listDownloadTask.isEmpty()) return
        synchronized (this) {
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