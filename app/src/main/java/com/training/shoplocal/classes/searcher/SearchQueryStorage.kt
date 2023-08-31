package com.training.shoplocal.classes.searcher


import com.training.shoplocal.AppShopLocal
import com.training.shoplocal.deleteFile
import com.training.shoplocal.fileExists
import java.io.*
import java.util.concurrent.Callable
import java.util.concurrent.Executors

enum class SearchState {
    SEARCH_NONE,
    SEARCH_QUERY,
    SEARCH_RESULT
}

class SearchQueryStorage: SearchQueryStorageInterface {
    private var changed = false
    private val listSQ = mutableListOf<String>()// getQueriesStorage().toMutableList()

    override val fileNameStorage: String =
        AppShopLocal.appContext().applicationInfo.dataDir + "/search.lst"

    override fun put(value: String) {
        if (value.isBlank())
            return
        if (!listSQ.contains(value)) {
            changed = true
            listSQ.add(0, value)
        } else
            moveFirst(value)
    }

    override fun remove(index: Int){//value: String) {
        changed = listSQ.removeAt(index).isNotEmpty()
    }

    override fun getQueries(fromFile: Boolean): List<String> {

    val callable = Callable<List<String>> {
        if (fromFile) {
            listSQ.clear()
            if (fileExists(fileNameStorage)) {
                try {
                    BufferedReader(FileReader(fileNameStorage)).use {
                        it.lineSequence().forEach { line ->
                            listSQ.add(line)
                        }
                    }
                } catch (_: IOException) {
                }
            }
        }
        listSQ
    }
    return Executors.newSingleThreadExecutor().submit(callable).get()
    }

    override fun saveQueries(): Boolean {
        return try {
                val text = StringBuffer()
                for (i in 0 until listSQ.size)
                    text.append("${listSQ[i]}\n")
                FileOutputStream(File(fileNameStorage)).use {
                    it.write(text.toString().toByteArray())
                }
            true
        } catch (_: IOException){
            false
        }
    }

    private fun moveFirst(value: String) {
        changed = true
        listSQ.remove(value)
        listSQ.add(0, value)
    }

    fun dispose(){
        listSQ.clear()
    }

    override fun removeAllQueries() {
        listSQ.clear()
        deleteFile(fileNameStorage)
    }

    companion object {
        private lateinit var instance: SearchQueryStorage
        fun getInstance(): SearchQueryStorage {
            if (!this::instance.isInitialized)
                instance = SearchQueryStorage()
            return instance
        }
    }
}
