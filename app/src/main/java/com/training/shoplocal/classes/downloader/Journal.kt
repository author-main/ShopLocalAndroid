package com.training.shoplocal.classes.downloader

import com.training.shoplocal.*
import java.io.BufferedReader
import java.io.File
import java.io.FileFilter
import java.io.FileOutputStream
import java.io.FileReader
import java.io.IOException
import javax.inject.Inject

class Journal @Inject constructor(private val cacheDir: String) {
    private data class CacheEntry(val hash: String) {
        var state:    StateEntry = StateEntry.CLEAN     // состояние файла
        var time:     Long = 0                          // дата создания/изменения файла кеша
        var length:   Long = 0                          // размер файла кеша
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

    fun clear(){
        entries.clear()
        deleteFile(fileJournal)
        deleteFile(fileJournalTmp)
        deleteFile(fileJournalBackup)
        fileJournal.createNewFile()
    }

    private fun loadEntriesFromJournal(){
        entries.clear()
        try {
            BufferedReader(FileReader(fileJournal)).use {
                it.lineSequence().forEach { line ->
                    val entry = getEntryFromLine(line)
                    entry?.let { data ->
                        data.length = getFileSize(getFilenameCacheFile(data.hash))
                        entries[data.hash] = data
                    }
                }
            }
        } catch(_: IOException) {

        }
    }

    fun put(hash: String, time: Long) {
        if (entries[hash] != null)
            return
        entries[hash] = CacheEntry(hash).apply {
            this.state = StateEntry.DIRTY
            this.time  = time
            length = getFileSize(getFilenameCacheFile(hash))
        }
    }

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
    fun remove(hash: String, changeState: Boolean, cancel: Boolean = false) {
        if (cancel) {
            val entry = entries[hash]
            entry?.let{
                if (it.state == StateEntry.DIRTY && it.length > 0L)
                    it.state = StateEntry.CLEAN
                else
                    entries.remove(hash)
            }
            return
        }

        if (changeState)
            update(hash, StateEntry.REMOVE)
        else
            entries.remove(hash)
    }

    /**
     *  Сохранение entries state != REMOVE в файл журнала
     *  @return список хэш файлов, которые необходимо удалить
     */
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
   fun getCacheSize(): Long {
       var sum = 0L
       entries.forEach { entry ->
           if (!removed(entry.value))
               sum += entry.value.length
       }
       return sum
   }

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
        return list
    }

    fun equals(hash: String, time: Long): Boolean =
       entries[hash]?.let{entry ->
           entry.time == time
       } ?: false

    fun getTimestamp(hash: String): Long =
        entries[hash]?.time ?: 0L

    fun getEntryState(hash: String): StateEntry =
        entries[hash]?.state ?: StateEntry.REMOVE
}