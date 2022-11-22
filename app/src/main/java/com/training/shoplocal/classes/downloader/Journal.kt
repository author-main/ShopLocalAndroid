package com.training.shoplocal.classes.downloader

import com.training.shoplocal.*
import java.io.BufferedReader
import java.io.File
import java.io.FileFilter
import java.io.FileOutputStream
import java.io.FileReader

class Journal private constructor(private val cacheDir: String) {
    private data class CacheEntry(val hash: String) {
        var state:    StateEntry = StateEntry.CLEAN     // состояние файла
        var time:     Long = 0                          // дата создания/изменения файла кеша
        var length:   Long = 0                          // размер файла кеша
        /*override fun equals(other: Any?): Boolean {
            if (other == null || other !is CacheEntry)
                return false
            return hash == other.hash && time == other.time
        }
        override fun hashCode(): Int = hash.hashCode() + time.hashCode()*/
    }
    private val JOURNAL_FILENAME        = "journal"         // файл журнала
    private val JOURNAL_FILENAME_TMP    = "journal.tmp"     // темп файл журнала
    private val JOURNAL_FILENAME_BACKUP = "journal.bkp"     // резервная копия журнала
    private val fileJournal         = File(cacheDir + JOURNAL_FILENAME)
    private val fileJournalTmp      = File(cacheDir + JOURNAL_FILENAME_TMP)
    private val fileJournalBackup   = File(cacheDir + JOURNAL_FILENAME_BACKUP)
    private val entries = LinkedHashMap<String, CacheEntry>(0, 0.75f, true)
    init {
        deleteFile(fileJournalTmp)
        deleteFiles(cacheDir, EXT_CACHETEMPFILE)
        getEntriesFromJournal()
    }

    private fun getFilenameCacheFile(hash: String) =
        "$cacheDir$hash"

    private fun getLineFromEntry(entry: CacheEntry, LF: Boolean = true): String {
        val lf = if (LF) "\n" else ""
        return "${entry.state.value} ${entry.hash} ${entry.time}$lf"
    }

    private fun getEntryFromLine(line: String): CacheEntry? {
        return try {
            val state = StateEntry.valueOf(line.substring(0, 6).trim())
            val hash = line.substring(7, 7 + HASH_LENGTH)
            val entry = CacheEntry(hash)
            if (state != StateEntry.REMOVE)
                entry.time = line.substring(HASH_LENGTH + 8).toLong()
            entry
        } catch (_: Exception){
            null
        }
    }

    private fun getEntriesFromJournal() {
        var existsJournal = true
        if (!fileJournal.exists()) {
            if (fileJournalBackup.exists()) {
                renameFile(fileJournalBackup, fileJournal)
            } else
                existsJournal = false
        }

        if (existsJournal) {
            loadEntriesFromJournal()
        } else {
            val text = StringBuffer()
            try {
                val files = File(cacheDir).listFiles(FileFilter { filename ->
                    filename.name.length == HASH_LENGTH
                })
                files?.forEach {file ->
                    val hash = file.name
                    entries[hash] = CacheEntry(hash).apply {length = file.length()}
                    text.append(getLineFromEntry(entries[hash]!!))
                }
                if (text.isNotEmpty()) {
                    FileOutputStream(fileJournal).use{
                        it.write(text.toString().toByteArray())
                    }
                    text.setLength(0)
                }
            } catch (_: Exception) {
            }
        }
    }

    @Synchronized
    fun clear(){
        entries.clear()
        deleteFile(fileJournal)
        deleteFile(fileJournalTmp)
        deleteFile(fileJournalBackup)
        fileJournal.createNewFile()
    }

    @Synchronized
    fun loadEntriesFromJournal(){
        entries.clear()
        BufferedReader(FileReader(fileJournal)).use{
            it.lineSequence().forEach {line ->
                val entry = getEntryFromLine(line)
                entry?.let{ data ->
                    data.length = getFileSize(getFilenameCacheFile(data.hash))
                    entries[data.hash] = data
                }
            }
        }
    }

    @Synchronized
    fun put(hash: String, state: StateEntry, time: Long) {
        /*val entry = entries[hash]
        entry?.let{
            if (time != 0L)
                it.length = getFileSize(getFilenameCacheFile(hash))
        } ?: run {
            entries[hash] = CacheEntry(hash).apply {
                this.state = state
                this.time  = time
            }
        }*/
        entries[hash] = CacheEntry(hash).apply {
            this.state = state
            this.time  = time
            if (time != 0L)
                length = getFileSize(getFilenameCacheFile(hash))
        }
    }

    @Synchronized
    fun update(hash: String, state: StateEntry, time: Long = 0) {
        entries[hash]?.let{
            it.state = state
            it.time  = time
        }
    }

    /**
     *  Удалить/изменить state запись файла журнала
     *  @param hash хэш (имя) файла кэша
     *  @param changeState true: будет изменен state на REMOVE,
     *  false: запись будет удалена
     */
    @Synchronized
    fun remove(hash: String, changeState: Boolean) {
        if (changeState)
            update(hash, StateEntry.REMOVE)
        else
            entries.remove(hash)
    }

    /**
     *  Сохранение entries state != REMOVE в файл журнала
     *  @return список хэш файлов, которые необходимо удалить
     */
    @Synchronized
    fun saveEntriesToJournal(): List<String>{
        val listRemoved = mutableListOf<String>()
        val text = StringBuffer()
        entries.forEach{entry ->
            if (!removed(entry.value))
                text.append(getLineFromEntry(entry.value))
            else
                listRemoved.add(entry.key)
        }
        if (text.isNotEmpty()) {
            FileOutputStream(fileJournalTmp).use{
                it.write(text.toString().toByteArray())
            }
            text.setLength(0)
            renameFile(fileJournal, fileJournalBackup)
            renameFile(fileJournalTmp, fileJournal)
        }
        return listRemoved
    }

    /*private fun isRemoved(state: StateEntry) =
        state == StateEntry.REMOVE*/

    /**
     *  Проверяем, помечена ли запись на удаление StateEntry.REMOVE
     */
    private fun removed(entry: CacheEntry?) =
        entry?.let {
            it.state == StateEntry.REMOVE
        } ?: true

    /**
    *  Получить размер файлов кэша
    */
   @Synchronized
   fun getCacheSize(): Long {
       //entries.values.sumOf { it.length }
       var sum = 0L
       entries.forEach { entry ->
           if (!removed(entry.value))
           //if (entry.value.state != StateEntry.REMOVE)
               sum += entry.value.length
       }
       return sum
   }
    /**
    *  Получить размер файла кэша
    *  @param hash хэш (имя) файла кэша
    */
   @Synchronized
   fun getCacheFileSize(hash: String): Long =
       entries[hash]?.let{
           //if (it.state != StateEntry.REMOVE)
           if (!removed(it))
               it.length
           else
               0L
       } ?: 0L

    fun leavingCacheFiles(limit: Long): List<String> {
        val size = getCacheSize()
        val list = mutableListOf<String>()
        var sum = 0L
        for (entry in entries) {
            if (entry.value.state == StateEntry.CLEAN) {
                sum += entry.value.length
                if (size - sum <= limit)
                    break
                else
                    list.add(entry.key)
            }
        }
        /*list.forEach { hash ->
            entries.remove(hash)
        }*/
        return list
    }

    /*
    /**
    *  Получить список файлов кэша
    */
   @Synchronized
   fun getListCacheFiles(): List<String> {
       val list = mutableListOf<String>()
       entries.forEach {entry ->
           if (!removed(entry.value))
           //if (entry.value.state != StateEntry.REMOVE)
               list.add(entry.key)
       }
       return list
       //entries.keys.toList()
   }*/

   @Synchronized
   fun equals(hash: String, time: Long): Boolean =
       entries[hash]?.let{entry ->
           entry.time == time
       } ?: false

   companion object {
       private var instance: Journal? = null
       @JvmName("getInstance1")
       fun getInstance(cacheDir: String): Journal =
           instance ?: Journal(cacheDir)
   }
}