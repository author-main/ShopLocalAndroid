package com.training.shoplocal.classes.downloader

import android.graphics.Bitmap
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
    class CacheEntry(val hash: String) {
        val length = LongArray(2) // старый и новый размер файла
        var involved: Boolean = false   // файл задействован чтение/запись
    }
    private val entries = LinkedHashMap<String, CacheEntry>(0, 0.75f, true)
    private val JOURNAL_FILENAME        = "journal"
    private val JOURNAL_FILENAME_TMP    = "journal.tmp"
    private val JOURNAL_FILENAME_BACKUP = "journal.bkp"
    private val EXTFILE_SAVED           = ".s"
    private val EXTFILE_PROGRESS        = ".p"

    private val existsCacheStorage  = createDirectory(cacheDir)
    private val fileJournal         = File(cacheDir + JOURNAL_FILENAME)
    private val fileJournalTmp      = File(cacheDir + JOURNAL_FILENAME_TMP)
    private val fileJournalBackup   = File(cacheDir + JOURNAL_FILENAME_BACKUP)

    init {
        if (existsCacheStorage) {
            if (!emptyCacheStorage()) {
                deleteFile(fileJournalTmp!!)
                val existsFileJournal       = fileJournal.exists()         ?: false
                val existsFileJournalBackup = fileJournalBackup.exists()   ?: false
                if (!existsFileJournal) {
                    if (existsFileJournalBackup) {
                        renameFile(fileJournalBackup, fileJournal)
                        rebuildEntries()
                    }
                    else
                        clear()
                }
            }
        }
    }

    private fun md5(link: String): String {
        val HASH_LENGTH = 32
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

    private fun rebuildJournal(){
        val writer = BufferedWriter(FileWriter(fileJournal, false))
        writer.flush()
        writer.close()
    }

    private fun rebuildEntries(){
        entries.clear()
    }


    private fun renameFile(source: String, dest: String){
        renameFile(File(source), File(dest))
    }

    private fun renameFile(source: File, dest: File){
        try {
            if (source.exists()) {
                deleteFile(dest)
                source.renameTo(dest)
            }
        } catch (_:IOException){}
    }

    private fun deleteFile(filename: String) {
        deleteFile(File(filename))
    }

    private fun deleteFile(file: File) {
        try {
            file.delete()
        } catch (_:IOException){}
    }

    private fun emptyCacheStorage(): Boolean {
        return (File(cacheDir).listFiles()?.size ?: 0) == 0
    }

    private fun createDirectory(value: String): Boolean {
        val dir: File = File(value)
        return if (!dir.exists()) {
            try {
                dir.mkdirs()
            } catch (_: IOException) {
                false
            }
        } else
            true
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

        TODO("Not yet implemented")
    }

    override fun clear() {
        entries.clear()
        if (existsCacheStorage) {
            val files = File(cacheDir).listFiles()
            files?.forEach {file ->
                file.delete()
            }
        }
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