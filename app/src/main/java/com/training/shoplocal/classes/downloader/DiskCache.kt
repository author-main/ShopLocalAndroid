package com.training.shoplocal.classes.downloader

import android.graphics.Bitmap
import com.training.shoplocal.*

class DiskCache private constructor(private val cacheDir: String): ImageCache {
    private val existsCacheStorage = createDirectory(cacheDir)
    private val journal = Journal.getInstance(cacheDir)

    override fun getCacheDir(): String = cacheDir

    override fun getTimestamp(link: String): Long {
        val hash = getHashCacheFile(link)
        return journal.getTimestamp(hash)
    }

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
        val state = journal.getEntryState(hash)
        if (state == StateEntry.DIRTY)
            return null
        journal.update(hash, StateEntry.DIRTY)
        var bitmap: Bitmap? = null

        if (timestamp > 0L) {
            if  (journal.equals(hash, timestamp)) {
                bitmap = loadBitmap(getFileNameFromHash(hash))
            }
        } else {
            bitmap = loadBitmap(getFileNameFromHash(hash))
        }


        return bitmap
    }

    override fun put(link: String, time: Long) {
       /* val state = if (time == 0L)
            StateEntry.DIRTY
        else
            StateEntry.CLEAN*/
        journal.put(getHashCacheFile(link), time)
    }

    override fun remove(link: String, changeState: Boolean){//, cancel: Boolean) {
        val hash = getHashCacheFile(link)
        journal.remove(hash, changeState)
        if (!changeState)
            deleteCacheFile(hash)
    }

    override fun update(link: String, state: StateEntry, time: Long) {
        val hash = getHashCacheFile(link)
        journal.update(hash, state, time)
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

    companion object {
        private lateinit var instance: ImageCache
        fun initStorage(cachedir: String){
            getInstance(cachedir)
        }
        private fun getInstance(cachedir: String){
            if (!this::instance.isInitialized)
                instance = DiskCache(cachedir)
        }
        fun getTimestamp(link: String) = instance.getTimestamp(link)

        fun placed(filesize: Long) = instance.placed(filesize)

        fun normalizeJournal() {
            instance.normalizeJournal()
        }

        fun get(link: String, timestamp: Long): Bitmap? = instance.get(link, timestamp)

        fun put(link: String, time: Long = 0L) {
            instance.put(link, time)
        }

        fun remove(link: String, changeState: Boolean){//, cancel: Boolean) {
            instance.remove(link, changeState)//, cancel)
        }

        fun update(link: String, state: StateEntry, time: Long) {
            instance.update(link, state, time)
        }

        fun clear() {
            instance.clear()
        }

        fun getCacheDir() = instance.getCacheDir()

    }
}