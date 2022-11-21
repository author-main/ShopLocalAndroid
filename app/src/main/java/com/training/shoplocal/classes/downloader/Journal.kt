package com.training.shoplocal.classes.downloader

import com.training.shoplocal.*
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.FileReader

class Journal private constructor(private val cacheDir: String) {
    private var onDeleteCacheFile: OnDeleteCacheFile? = null
//    private var size = 0L // Размер файлов кэша
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
        initEntriesJournal()
    }

    private fun initEntriesJournal(){
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
            val text = StringBuffer()
            var journalChanged = false
            BufferedReader(FileReader(fileJournal)).use{
                it.lineSequence().forEach { line ->
                    val hash    = line.substring(7, 7 + HASH_LENGTH)
                    var state = StateEntry.valueOf(line.substring(0, 6).trim())
                    if (state != StateEntry.REMOVE) {
                        val data = (line.substring(HASH_LENGTH + 8)).split(' ')
                        val time = data[0].toLong()
                        val length = data[1].toLong()

                        var stringEntry = line
                        if (state == StateEntry.DIRTY){
                            if (length > 0) {
                                journalChanged = true
                                //size += length
                                state = StateEntry.CLEAN
                                stringEntry = line.replace(StateEntry.DIRTY.value, StateEntry.CLEAN.value)
                            } else {
                                //deleteChacheFile(hash)
                                onDeleteCacheFile?.deleteCacheFile(hash)
                            }
                        }
                        if (state == StateEntry.CLEAN) {
                            entries[hash] = CacheEntry(hash).apply {
                                this.state  = state
                                this.time   = time
                                this.length = length
                            }
                            text.append(stringEntry + "\n")
                        }
                    } else {
                        journalChanged = true
                        onDeleteCacheFile?.deleteCacheFile(hash)
                        //deleteChacheFile(hash)
                    }
                }
            }

            if (journalChanged) {
                if (text.isNotEmpty()) {
                    FileOutputStream(fileJournalTmp).use {
                        it.write(text.toString().toByteArray())
                    }
                    updateJournalFiles()
                }
            }
        }
    }

/*    private fun getEntryFromLine(line: String): Unit{//CacheEntry?{
         try {
            val hash = line.substring(7, 7 + HASH_LENGTH)
            CacheEntry(hash).apply {
                this.state = StateEntry.valueOf(line.substring(0, 6).trim())
                val data = (line.substring(HASH_LENGTH + 8)).split(' ')
                this.time = data[0].toLong()
                this.length = data[1].toLong()
            }
        } catch (_: Exception) {
        }
    }*/

    private fun updateJournalFiles(){//restoreFromTemp: Boolean = false){
        renameFile(fileJournal, fileJournalBackup)
        //if (restoreFromTemp)
        renameFile(fileJournalTmp, fileJournal)
    }

    @Synchronized
    fun add(hash: String, image: BitmapData? = null) {
        val timestamp = image?.time ?: 0L
        entries[hash] = CacheEntry(hash).apply {
            if (timestamp == 0L) {
                this.state = StateEntry.DIRTY
                addJournal(this)
            }
            else {
                this.state  = StateEntry.CLEAN
                this.time   = timestamp
                this.length = image?.length ?: 0L//getFileSize("$cacheDir$hash")
                updateJournal(this)
            }
        }

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
            onDeleteCacheFile?.deleteCacheFile(hash)
            entries.remove(hash)
        }
        removeJournal(hashlist)
    }

    @Synchronized
    fun remove(hash: String, changeState: Boolean): Long {
        if (!changeState) {
            val length = entries[hash]?.length ?: 0L
            entries.remove(hash)
            onDeleteCacheFile?.deleteCacheFile(hash)
            removeJournal(hash, false)
            return length
        }
        entries[hash] = CacheEntry(hash).apply {
            this.state = StateEntry.REMOVE
            this.time  = 0
        }
        updateJournal(entries[hash]!!)
        return 0L
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

    /**
     *  Удалить запись из файла журнала
     *  @param hash хэш файла кэша
     *  @param onlyState true  - будут удалены только записи со state = REMOVE,
     *                   false - будут удалены все записи с указанным хэшем
     */
    private fun removeJournal(hash: String, onlyState: Boolean = true){
        val text = StringBuffer()
        BufferedReader(FileReader(fileJournal)).use {
            it.lineSequence().forEach { line ->
                val skip = if (line.contains(hash)) {
                               if (!onlyState)
                                    true
                               else {
                                    line.startsWith(StateEntry.REMOVE.value)
                               }
                           } else
                               false
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
            deleteFiles(cacheDir)
            //deleteFile(fileJournal)
            fileJournal.createNewFile()
        }
    }

    fun getHashList(excludeHash: String? = null): List<String>{
        val list = mutableListOf<String>()
        for (entry in entries) {
            val exclude = excludeHash != null && excludeHash == entry.key
            if (!exclude && entry.value.state != StateEntry.DIRTY)
            //if (entry.value.state == StateEntry.CLEAN || entry.value.state == StateEntry.REMOVE)
                list.add(entry.value.hash)
        }
        return list
    }

    fun getEntrySize(hash: String): Long {
        val entry = entries[hash]
        return entry?.length ?: 0L
    }

    fun addOnDeleteCacheFile(value: OnDeleteCacheFile) {
        onDeleteCacheFile = value
    }

    companion object {
        private var instance: Journal? = null
        @JvmName("getInstance1")
        fun getInstance(cacheDir: String, onDeleteCacheFile: OnDeleteCacheFile? = null): Journal {
            if (instance == null)
                instance = Journal(cacheDir)

            if (onDeleteCacheFile != null)
                instance?.addOnDeleteCacheFile(onDeleteCacheFile)

            return instance!!
        }
    }

    fun getCacheSize(): Long =
        entries.values.sumOf { it.length }

    @Synchronized
    fun clear() {
        val text = StringBuffer()
        val hashList = mutableListOf<String>()
        entries.forEach { entity ->
            if (entity.value.state == StateEntry.DIRTY)
                hashList.add(entity.key)
            else {
                entity.value.state = StateEntry.REMOVE
                text.append("${entity.value.state.value} ${entity.value.hash} ${entity.value.time} ${entity.value.length}\n")
            }
        }
        hashList.forEach { hash ->
            entries.remove(hash)
            onDeleteCacheFile?.deleteCacheFile(hash)
        }
        if (text.isNotEmpty()) {
            FileOutputStream(fileJournal).use {
                it.write(text.toString().toByteArray())
            }
            text.setLength(0)
        } else {
            deleteFiles(cacheDir)
            entries.clear()
            fileJournal.createNewFile()
        }
    }
}