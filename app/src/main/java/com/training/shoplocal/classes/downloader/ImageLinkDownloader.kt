package com.training.shoplocal.classes.downloader

import android.graphics.Bitmap
import com.training.shoplocal.Error
import java.io.File
import java.io.IOException
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

interface Callback {
    fun onComplete(image: Bitmap)
    fun onFailure(error: Error)
}

class DiskCache(private val cacheDir: String): ImageCache {
    private val entries = LinkedHashMap<String, Map.Entry<*, *>>(0, 0.75f, true)
    private val JOURNAL_FILENAME        = "journal"
    private val JOURNAL_FILENAME_TMP    = "journal.tmp"
    private val JOURNAL_FILENAME_BACKUP = "journal.bkp"
    private val EXTFILE_SAVED           = ".s"
    private val EXTFILE_PROGRESS        = ".p"

    private var fileJournal: File? = null
    private var fileJournalTmp: File? = null
    private var fileJournalBackup: File? = null
    private var existsCacheStorage = createDirectory(cacheDir)

    init {
        if (existsCacheStorage) {
            fileJournal         = File(cacheDir + JOURNAL_FILENAME)
            fileJournalTmp      = File(cacheDir + JOURNAL_FILENAME_TMP)
            fileJournalBackup   = File(cacheDir + JOURNAL_FILENAME_BACKUP)
            if (!emptyCacheStorage()) {
                deleteFile(fileJournalTmp!!)
                val existsFileJournal       = fileJournal?.exists()         ?: false
                val existsFileJournalBackup = fileJournalBackup?.exists()   ?: false
                if (!existsFileJournal) {
                    if (existsFileJournalBackup)
                        renameFile(fileJournalBackup!!, fileJournal!!)
                    else
                        clear()
                }
            }
        }
    }


    private fun rebuildCacheJournal(){

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

    override fun get(hash: String): Bitmap? {
        TODO("Not yet implemented")
    }

    override fun put(hash: String, image: Bitmap) {
        TODO("Not yet implemented")
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

  /*  companion object {
        private var instance: ImageCache? = null
        fun getInstance(cacheDir: String): ImageCache =
            instance ?: DiskCache(cacheDir)

        /*fun get(md5: String): Bitmap? {
            return instance?.get(md5)
        }*/
    }*/
}

class ImageLinkDownloader private constructor(){
    private var cacheStorage: ImageCache? = null
    fun md5(link: String): String {
        val HASH_LENGTH = 32
        try {
        val md = MessageDigest.getInstance("MD5")
        val messageDigest = md.digest(link.toByteArray())
        val hashbi = BigInteger(1, messageDigest)
        var hashtext = hashbi.toString(16)
            try {
                hashtext = "0".repeat(HASH_LENGTH - hashtext.length) + hashtext
            } catch (e: IllegalArgumentException) {
                throw RuntimeException(e)
            }
        /*while (hashtext.length < 32) {
            hashtext = "0$hashtext"
        }*/
        return hashtext
    } catch (e: NoSuchAlgorithmException) {
        throw RuntimeException(e)
    }
    }
    fun downloadLinkImage(link: String, callback: Callback){
        val image = Bitmap.createBitmap(100,100,Bitmap.Config.ALPHA_8)
        if (image != null)
            callback.onComplete(image)
        else
            callback.onFailure(Error.NO_CONNECTION)
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

        fun download(url: String, callback: Callback) {
            getInstance().downloadLinkImage(url, callback)
        }
    }
}