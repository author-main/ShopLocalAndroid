package com.training.shoplocal.classes.downloader

import android.graphics.Bitmap
import com.training.shoplocal.*
import java.io.*
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.concurrent.Executors
import java.util.concurrent.Future

interface Callback {
    fun onComplete(image: Bitmap)
    fun onFailure()
}


class DiskCache(private val cacheDir: String): ImageCache {
    private val existsCacheStorage = createDirectory(cacheDir)
    private var size = getCacheSize()
    private val journal = Journal.getInstance(cacheDir)

    /**
     *  Изменение размера кэша в соответствии с максимальным размером кэша CACHE_SIZE
     *  @param insize размер нового файла, помещаемого в кэш
     *  @return Boolean - true файл может быть размещен в кэше
     */
    @Synchronized
    private fun trimCache(insize: Long): Boolean{
        if (size + insize < CACHE_SIZE)
            return true
        val limit = CACHE_SIZE - insize
        val hashList = journal.getHashList()
        val outHashList = mutableListOf<String>()
        var placed = false
        var calcSize = size
        for (hash in hashList) {
            calcSize -= journal.getEntrySize(hash)
            outHashList.add(hash)
            if (calcSize <= limit) {
                placed = true
                size = calcSize
                journal.remove(outHashList)
                break
            }
        }
        return placed
    }

    private fun md5(link: String): String {
        try {
            val md = MessageDigest.getInstance("MD5")
            val messageDigest = md.digest(link.toByteArray())
            var hashtext = BigInteger(1, messageDigest).toString(16)
            /*try {
                hashtext = "0".repeat(HASH_LENGTH - hashtext.length) + hashtext
            } catch (_: IllegalArgumentException) {}*/
            while (hashtext.length < HASH_LENGTH)
                hashtext = "0$hashtext"
            return hashtext
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException(e)
        }
    }

    private fun getCacheSize(): Long {
        var size: Long = 0
        File(cacheDir).listFiles(FileFilter { it.extension != Journal.EXT_CACHETEMPFILE} )?.forEach { file ->
            size += file.length()
        }
        return size
    }

    private fun getLinkHash(link: String): String =
        md5(link)


    override fun get(link: String, time: Long): Bitmap? {
        val hash = getLinkHash(link)
        return null
    }

    override fun put(link: String, image: Bitmap, time: Long) {
        val hash = getLinkHash(link)
    }

    override fun remove(hash: String) {
    }

    override fun clear() {
    }
}

class ImageLinkDownloader private constructor(){
    private var cacheStorage: ImageCache? = null
    private val executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())
    private val listDownloadTask:HashMap<String, Future<Bitmap?>> = hashMapOf()

    fun downloadLinkImage(link: String, callback: Callback){
        val task = DownloadImageTask(link, cacheStorage, callback)
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
            instance ?: ImageLinkDownloader()

        fun setCacheDirectory(dir: String){
            getInstance().setCacheDirectory(dir)
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