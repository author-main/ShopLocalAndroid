package com.training.shoplocal.classes.searcher

import com.training.shoplocal.AppShopLocal
import com.training.shoplocal.fileExists
import java.io.*

class SearchQueryStorage: SearchQueryStorageInterface {
    private var changed = false;
    private val listSQ = mutableListOf<String>()// getQueriesStorage().toMutableList()

    override val fileNameStorage: String =
        AppShopLocal.appContext().applicationInfo.dataDir + "/search.lst"

    override fun put(value: String) {
        if (!listSQ.contains(value)) {
            changed = true
            listSQ.add(0, value)
        } else
            moveFirst(value)
    }

    override fun delete(value: String) {
        changed = listSQ.remove(value)
        /*val removed = listSQ.filter { it == value }
        if (removed.isNotEmpty()) {
            changed = true
            listSQ.removeAll(removed)
        }*/
    }

//    private fun getQueriesStorage(): List<String>{
    override fun getQueries(): List<String> {
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
        return listSQ
    }

    override fun saveQueries(list: List<String>): Boolean {
        return try {
            if (changed) {
                FileOutputStream(File(fileNameStorage)).use {
                    it.write(list.toString().toByteArray())
                }
            }
            true
        } catch (_: IOException){
            false
        }
    }

    override fun moveFirst(value: String) {
        if (!listSQ.contains(value)) {
            changed = true
            listSQ.remove(value)
            listSQ.add(0, value)
        }
    }

    fun clearAndSave(){
        saveQueries(listSQ)
        listSQ.clear()
    }

    companion object {
        private lateinit var instance: SearchQueryStorage
        fun getInstance() =
            if (this::instance.isInitialized)
                instance
            else {
                instance = SearchQueryStorage().apply { getQueries() }
                instance
            }

    }

}