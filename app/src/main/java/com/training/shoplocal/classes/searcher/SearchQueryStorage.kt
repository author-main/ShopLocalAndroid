package com.training.shoplocal
.classes.searcher


import com.training.shoplocal.AppShopLocal
import com.training.shoplocal.deleteFile
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

    override fun remove(index: Int){//value: String) {
        changed = listSQ.removeAt(index).isNotEmpty()
        /*val removed = listSQ.filter { it == value }
        if (removed.isNotEmpty()) {
            changed = true
            listSQ.removeAll(removed)
        }*/
    }

//    private fun getQueriesStorage(): List<String>{
    override fun getQueries(): List<String> {
      /*  listSQ.addAll(listOf(
        "Скайлайн / Skyline (2010) BDRip-HEVC 1080p от RIPS CLUB | D | USA Transfer",
        "Фабельманы / The Fabelmans (2022) WEB-DLRip-AVC от DoMiNo & селезень | A | Яроцкий",
        "Кто убил Санту? Тайна убийства в Мердервилле / Who Killed Santa? A Murderville Murder Mystery (2022) WEBRip 1080p | ColdFilm",
        "Охота / Heonteu / Hunt (2022) WEB-DLRip от New-Team | A | Сербин",
        "Охота / Heonteu / Hunt (2022) WEB-DL 1080p от New-Team | A | Сербин",
        "Фабельманы / The Fabelmans (2022) WEB-DLRip-AVC от ExKinoRay | A",
        "В самое сердце / En plein coeur (1998) BDRip-AVC от msltel | L1",
        "Безумный уик-энд / Tout nous sourit (2020) WEBRip 1080p | D",
        "Эра выживания / Vesper (2022) HDRip от toxics | TVShows",
        "Дневная смена / Day Shift (2022) WEB-DLRip-AVC от DoMiNo & селезень | D | Red Head Sound",
        "Фабельманы / The Fabelmans (2022) WEB-DL 720p от ExKinoRay | A",
        "Секретная штаб-квартира / Secret Headquarters (2022) BDRip от MegaPeer | Jaskier",
        "Фабельманы / The Fabelmans (2022) WEB-DLRip от New-Team | A | Яроцкий",
        "Фабельманы / The Fabelmans (2022) WEB-DL 1080p от New-Team | A | Яроцкий",
        "Капитан Зум: Академия супергероев / Zoom (2006) WEB-DLRip-AVC | P2",
        "Дневная смена / Day Shift (2022) WEBRip 720p от ExKinoRay | D, P, A",
        "Нет / Nope (2022) HDRip-AVC от ExKinoRay | D | IMAX | Лицензия",
        "Воскрешение / Resurrection (2022) BDRip 1080p от ExKinoRay | P, A",
        "Последний клиент / Klienten / The Last Client (2022) BDRip от toxics | iTunes",
        "Аполлон 13 / Apollo 13 (1995) HybridRip-AVC от DoMiNo | D | Open Matte",
        "Эра выживания / Vesper (2022) BDRip 1080p от селезень | P | TVShows",
        "Последний клиент / Klienten / The Last Client (2022) BDRip от MegaPeer | iTunes",
        "Банши Инишерина / The Banshees of Inisherin (2022) WEB-DLRip | P, A",
        "Банши Инишерина / The Banshees of Inisherin (2022) WEB-DL-HEVC 2160p | HDR10+ | P, A",
        "Джильи / Gigli (2003) HDTVRip 1080p | D, P, A | Open Matte",
        "Эра выживания / Vesper (2022) BDRip 720p от DoMiNo & селезень | P | TVShows",
        "Воскрешение / Resurrection (2022) BDRip 1080p от New-Team | Jaskier",
        "Зловещий свет / Prey for the Devil (2022) WEB-DLRip от ExKinoRay | TVShows",
        "Битлз: Четыре плюс один / Пятый в квартете / Backbeat (1994) BDRip 1080p | P2, A",
        "Кутёж 2: Этот чудесный запой / The Binge 2: It's A Wonderful Binge (2022) WEB-DLRip от toxics | TVShows",
        "Ночь убийств / Mountaintop Motel Massacre (1983) BDRip-AVC | P",
        "Воскрешение / Resurrection (2022) BDRip 720p от ExKinoRay | P, A",
        "Зловещий свет / Prey for the Devil (2022) WEB-DLRip от toxics | TVShows",
        "Боррего / Borrego (2022) WEB-DLRip от toxics | Pazl Voice",
        "Последний клиент / Klienten / The Last Client (2022) BDRip-AVC от DoMiNo & селезень | iTunes",
        "Последний клиент / Klienten / The Last Client (2022) BDRip 1080p от селезень | iTunes",
        "Ловушка для родителей / The Parent Trap (1998) HDRip-AVC | КПК | P",
        "Банши Инишерина / The Banshees of Inisherin (2022) WEB-DLRip от toxics | TVShows",
        "Ниндзя 2 / Ninja: Shadow of a Tear (2013) BDRip 720p от Leonardo and Scarabey",
        "Воскрешение / Resurrection (2022) BDRip от MegaPeer | Jaskier",
        "Последний клиент / Klienten / The Last Client (2022) BDRip 720p от селезень | iTunes",
        "Восставший из ада / Hellraiser (2022) WEBRip 1080p от ExKinoRay | P, A",
        "Голливудское Рождество / A Hollywood Christmas (2022) WEB-DLRip от toxics | TVShows",
        "Мой домашний крокодил / Lyle, Lyle, Crocodile (2022) BDRip 1080p от ExKinoRay | Jaskier",
        "Мой домашний крокодил / Lyle, Lyle, Crocodile (2022) BDRip 720p от ExKinoRay | Jaskier"))
*/


        listSQ.clear()
        if (fileExists(fileNameStorage)) {
            try {
                BufferedReader(FileReader(fileNameStorage)).use {
                    it.lineSequence().forEach { line ->
                        listSQ.add(line)//.replace("\n", ""))
                    }
                }
            } catch (_: IOException) {
            }
        }
        return listSQ
    }

    //override fun saveQueries(list: List<String>): Boolean {
    override fun saveQueries(): Boolean {
        return try {
          //  if (changed) {
                val text = StringBuffer()
                for (i in 0 until listSQ.size)
                    text.append("${listSQ[i]}\n")
                FileOutputStream(File(fileNameStorage)).use {
                    it.write(text.toString().toByteArray())
                }
          //  }
            true
        } catch (_: IOException){
            false
        }
    }

    private fun moveFirst(value: String) {
        //if (!listSQ.contains(value)) {
            changed = true
            listSQ.remove(value)
            listSQ.add(0, value)
        //}
    }

    fun dispose(){
        //saveQueries()
        listSQ.clear()
    }

    override fun removeAllQueries() {
        listSQ.clear()
        deleteFile(fileNameStorage)
    }

    companion object {
        private lateinit var instance: SearchQueryStorage
        fun getInstance() =
            if (this::instance.isInitialized)
                instance
            else {
                instance = SearchQueryStorage()//.apply { getQueries() }
                instance
            }

    }

}