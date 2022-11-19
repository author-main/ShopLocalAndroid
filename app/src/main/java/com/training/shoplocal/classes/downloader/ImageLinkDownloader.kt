package com.training.shoplocal.classes.downloader

import android.graphics.Bitmap
import com.training.shoplocal.log
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
    private var size = 0L // Размер файлов кэша
    private enum class StateEntry(val value: String){
        CLEAN   ("CLEAN "), // файл кэша свободен
        DIRTY   ("DIRTY "), // файл кэша занят, чтение/запись
        REMOVE  ("REMOVE")  // удалить сведения, файл из кэша
    }
    private val HASH_LENGTH = 32 // длина хэша файла
    private class CacheEntry(val hash: String) {
        var time:     Long = 0                          // дата создания/изменения файла кеша
        var state:    StateEntry = StateEntry.CLEAN     // состояние файла
    }
    private val entries = LinkedHashMap<String, CacheEntry>(0, 0.75f, true)
    private val JOURNAL_FILENAME        = "journal"         // файл журнала
    private val JOURNAL_FILENAME_TMP    = "journal.tmp"     // темп файл журнала
    private val JOURNAL_FILENAME_BACKUP = "journal.bkp"     // резервная копия журнала
    private val EXT_CACHETEMPFILE      = ".t"
    private val existsCacheStorage  = initCache()
    private val fileJournal         = File(cacheDir + JOURNAL_FILENAME)
    private val fileJournalTmp      = File(cacheDir + JOURNAL_FILENAME_TMP)
    private val fileJournalBackup   = File(cacheDir + JOURNAL_FILENAME_BACKUP)

    init {
        if (existsCacheStorage) {
            if (!emptyCacheStorage()) {
                deleteFile(fileJournalTmp)
                if (!fileJournal.exists()) {
                    if (backupJournal())
                        rebuildEntries()
                    else
                        clear()
                }
            }
        }
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

    /**
     *  Создание файла резервной копии если отсутвует
     */
    private fun createJournalBackup(text: StringBuffer) {
        if (text.isEmpty())
            return
        FileOutputStream(fileJournalBackup).use{
            it.write(text.toString().toByteArray())
        }
    }

    /*@Synchronized
    private fun createJournalBackup() {
        if (entries.isEmpty())
            return
        val text = StringBuffer()
        for (entry: CacheEntry in entries.values) {
            text.append("${entry.state.value} ${entry.hash} ${entry.time}\n")
        }
        FileOutputStream(fileJournalBackup).use{
            it.write(text.toString().toByteArray())
        }
        text.setLength(0)
    }*/

    @Synchronized
    private fun removeEntry(hash: String, deletefile: Boolean = false) {
        val text = StringBuffer()
        var deleted = false
        BufferedReader(FileReader(fileJournal)).use{
            it.lineSequence().forEach { line ->
                text.append("$line\n")
                if (!deleted) {
                    if (line.contains(hash)) {
                        if (deletefile)
                            deleteFile(cacheDir + hash)
                        text.setLength(text.length - line.length)
                        deleted = true
                    }
                }
            }
        }
        renameFile(fileJournal, fileJournalBackup)
        if (text.isNotEmpty()) {
            FileOutputStream(fileJournalTmp).use {
                it.write(text.toString().toByteArray())
            }
            text.setLength(0)
            renameFile(fileJournalTmp, fileJournal)
        } else
            fileJournal.createNewFile()
    }


    /**
     *   Полная перезапись файла журнала из entries
     */
    @Synchronized
    private fun rebuildJournal(){
        val text = StringBuffer()
        for (entry: CacheEntry in entries.values) {
            text.append("${entry.state.value} ${entry.hash} ${entry.time}\n")
        }
        FileOutputStream(fileJournalTmp).use{
            it.write(text.toString().toByteArray())
        }
        val existJournal = fileJournal.exists()
        if (existJournal)
            renameFile(fileJournal, fileJournalBackup)
        else
            createJournalBackup(text)
        text.setLength(0)
        renameFile(fileJournalTmp, fileJournal)
    }

    /**
     *  Восстановление файла журнала из backup
     */
    private fun backupJournal(): Boolean{
        return if (fileJournalBackup.exists()) {
                renameFile(fileJournalBackup, fileJournal)
                true
            } else
                false
    }

    /**
     *  Перезапись данных entry[hash] в файле журнала
     */
    @Synchronized
    private fun replaceEntry(hash: String, time: Long, state: StateEntry){
        entries[hash] = CacheEntry(hash).apply {
            this.time     = time
            this.state    = state
        }
        var existJournal = fileJournal.exists()
        if (!existJournal)
            existJournal = backupJournal()
        val text: StringBuffer = StringBuffer()
        if (existJournal) {
            BufferedReader(FileReader(fileJournal)).use {
                it.lineSequence().forEach { line ->
                    if (line.contains(hash)) {
                        text.append("${state.value} $hash $time\n")
                    } else
                        text.append("$line\n")
                }
            }
        } else
            entries.forEach{entity ->
                text.append("${entity.value.state.value} ${entity.value.hash} ${entity.value.time}\n")
            }

        FileOutputStream(fileJournalTmp).use{
            it.write(text.toString().toByteArray())
        }

        if (existJournal)
            renameFile(fileJournal, fileJournalBackup)
        else
            createJournalBackup(text)
        text.setLength(0)
        renameFile(fileJournalTmp, fileJournal)
    }

    /**
     *  Полное пересоздание entries из файла журнала
     */
    private fun rebuildEntries(){
        entries.clear()
        val reader = BufferedReader(FileReader(fileJournal)).use{
            it.lineSequence().forEach { line ->
                val state = StateEntry.valueOf( line.substring(0, 6).trim())
                val hash =                      line.substring(7, 7 + HASH_LENGTH)
                val time =                      line.substring(HASH_LENGTH + 8).toLong()
                entries[hash] = CacheEntry(hash).apply {
                    this.time   = time
                    this.state  = state
                }
            }
        }
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

    private fun getCacheSize(): Long {
        var size: Long = 0
        File(cacheDir).listFiles()?.forEach { file ->
            size += file.length()
        }
        return size
    }

    /**
     *  Инициализация кэша, создание папки для хранения,
     *  подсчет размера кэша size
     */

    private fun initCache(): Boolean{
        val result = createDirectory(cacheDir)
        size = getCacheSize()
        return result
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
            /*val str = "REMOVE"
            log(str.substring(0,6))*/
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