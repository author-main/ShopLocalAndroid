package com.training.shoplocal.classes.downloader

import com.training.shoplocal.*
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.FileReader

class Journal private constructor(private val cacheDir: String) {
    private var size = 0L // Размер файлов кэша
    private data class CacheEntry(val hash: String) {
        var time:     Long = 0                          // дата создания/изменения файла кеша
        var state:    StateEntry = StateEntry.CLEAN     // состояние файла
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
        restoreJournal()
    }

    private fun restoreJournal(){
        var skip = false
        if (!fileJournal.exists()) {
            if (fileJournalBackup.exists())
                renameFile(fileJournalBackup, fileJournal)
            else {
                skip = true
                deleteFiles(cacheDir)
                fileJournal.createNewFile()
            }
        }
        if (!skip) {
            BufferedReader(FileReader(fileJournal)).use{
                it.lineSequence().forEach { line ->
                    val state = StateEntry.valueOf(line.substring(0, 6).trim())
                    val hash  = line.substring(7, 7 + HASH_LENGTH)
                    val time  = line.substring(HASH_LENGTH + 8).toLong()
                    val lastindex = line.lastIndexOf(' ')
                    entries[hash] = CacheEntry(hash).apply {
                        this.time   = time
                        this.state  = state
                    }
                }
            }
        }
    }

    private fun updateJournalFiles(){//restoreFromTemp: Boolean = false){
        renameFile(fileJournal, fileJournalBackup)
        //if (restoreFromTemp)
        renameFile(fileJournalTmp, fileJournal)
    }

    @Synchronized
    fun add(hash: String, time: Long) {
        entries[hash] = CacheEntry(hash).apply {
            this.state =    StateEntry.DIRTY
            this.time  =    time
        }
        addJournal(entries[hash]!!)
    }

    /**
     *  Обновление информации о файле кэша, обновление информации в файле журнала
     *  @param hash хэш файла
     *  @param state состояние файла DIRTY: чтение или запись, CLEAN: файл свободен
     */
    @Synchronized
    fun update(hash: String, state: StateEntry) {
        val time = entries[hash]?.time ?: 0
        entries[hash] = CacheEntry(hash).apply {
            this.state  = state
            this.time   = time
            this.length = getFileSize(cacheDir + hash)
        }
        updateJournal(entries[hash]!!)
    }


    @Synchronized
    fun remove(hashlist: MutableList<String>){
        hashlist.forEach {hash ->
            entries.remove(hash)
        }
        removeJournal(hashlist)
    }

    @Synchronized
    fun remove(hash: String, fromJournal: Boolean = false) {
        if (fromJournal) {
            entries.remove(hash)
            removeJournal(hash)
            return
        }
        entries[hash] = CacheEntry(hash).apply {
            this.state = StateEntry.REMOVE
            this.time  = 0
        }
        updateJournal(entries[hash]!!)
    }


    private fun removeJournal(hashlist: MutableList<String>){
        val text = StringBuffer()
        BufferedReader(FileReader(fileJournal)).use {
            it.lineSequence().forEach { line ->
                var skip = false
                for ( i in hashlist.indices ) {
                    if (line.contains(hashlist[i])) {
                        skip = true
                        hashlist.removeAt(i)
                        break
                    }
                }
                if (!skip)
                    text.append(line + "\n")
            }
        }

        if (text.isNotEmpty()) {
            FileOutputStream(fileJournalTmp).use {
                it.write(text.toString().toByteArray())
            }
            updateJournalFiles()//restoreFromTemp = true)
        } else {
            deleteFile(fileJournal)
            fileJournal.createNewFile()
        }
    }


    private fun addJournal(entity: CacheEntry){
        val text = StringBuffer()
        BufferedReader(FileReader(fileJournal)).use {
            it.lineSequence().forEach { line ->
                text.append(line + "\n")
            }
            text.append("${entity.state.value} ${entity.hash} ${entity.time} ${entity.length}\n")
        }
        if (text.isNotEmpty()) {
            FileOutputStream(fileJournalTmp).use {
                it.write(text.toString().toByteArray())
            }
            updateJournalFiles()//restoreFromTemp = true)
        }
    }


    private fun updateJournal(entity: CacheEntry){
        val text = StringBuffer()
        BufferedReader(FileReader(fileJournal)).use {
            it.lineSequence().forEach { line ->
                if (line.contains(entity.hash)) {
                    text.append("${entity.state.value} ${entity.hash} ${entity.time} ${entity.length}\n")
                } else
                    text.append(line + "\n")
            }
        }
        if (text.isNotEmpty()) {
            FileOutputStream(fileJournalTmp).use {
                it.write(text.toString().toByteArray())
            }
            updateJournalFiles()//restoreFromTemp = true)
        }
    }


    private fun removeJournal(hash: String){
        val text = StringBuffer()
        BufferedReader(FileReader(fileJournal)).use {
            it.lineSequence().forEach { line ->
                val skip = line.contains(hash) && line.startsWith(StateEntry.REMOVE.value)
                if (!skip)
                    text.append(line + "\n")
            }
        }

        if (text.isNotEmpty()) {
            FileOutputStream(fileJournalTmp).use {
                it.write(text.toString().toByteArray())
            }
            updateJournalFiles()//restoreFromTemp = true)
        } else {
            deleteFile(fileJournal)
            fileJournal.createNewFile()
        }
    }

    fun getHashList(): List<String>{
        val list = mutableListOf<String>()
        for (entry in entries) {
            if (entry.value.state == StateEntry.CLEAN || entry.value.state == StateEntry.REMOVE)
                list.add(entry.value.hash)
        }
        return list
    }

    fun getEntrySize(hash: String): Long {
        val entry = entries[hash]
        return entry?.length ?: 0L
    }



    companion object {
        const val EXT_CACHETEMPFILE = "t"
        enum class StateEntry(val value: String){
            CLEAN   ("CLEAN "), // файл кэша свободен
            DIRTY   ("DIRTY "), // файл кэша занят, чтение/запись
            REMOVE  ("REMOVE")  // удалить сведения, файл из кэша
        }
        private var instance: Journal? = null
        @JvmName("getInstance1")
        fun getInstance(cacheDir: String): Journal =
            instance ?: Journal(cacheDir)

    }
}