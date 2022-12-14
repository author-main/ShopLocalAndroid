package com.training.shoplocal.classes.searcher

import com.training.shoplocal.AppShopLocal
import com.training.shoplocal.fileExists
import java.io.*

class SearchQueryStorage: SearchQueryStorageInterface {
    private val listSQ = getQueriesStorage().toMutableList()

    override val fileNameStorage: String =
        AppShopLocal.appContext().applicationInfo.dataDir + "/search.lst"

    override fun put(value: String) {
        TODO("Not yet implemented")
    }

    override fun delete(value: String) {
        TODO("Not yet implemented")
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
            FileOutputStream(File(fileNameStorage)).use{
                it.write(list.toString().toByteArray())
                true
            }
        } catch (_: IOException){
            false
        }
    }
}