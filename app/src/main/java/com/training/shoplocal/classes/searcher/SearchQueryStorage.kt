package com.training.shoplocal.classes.searcher

import com.training.shoplocal.AppShopLocal
import com.training.shoplocal.fileExists
import java.io.*

class SearchQueryStorage: SearchQueryStorageInterface {
    private var changed = false;
    private val listSQ = getQueriesStorage().toMutableList()

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
        listSQ.remove(value)
        /*val removed = listSQ.filter { it == value }
        if (removed.isNotEmpty()) {
            changed = true
            listSQ.removeAll(removed)
        }*/
    }

    private fun getQueriesStorage(): List<String>{
        if (!fileExists(fileNameStorage))
            return listOf<String>()
        val list = mutableListOf<String>()
        try {
            BufferedReader(FileReader(fileNameStorage)).use {
                it.lineSequence().forEach { line ->
                    list.add(line)
                }
            }
        } catch(_: IOException) {}
        return list
    }

    override fun getQueries(): List<String> {
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
}