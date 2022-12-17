package com.training.shoplocal.repository



import android.content.Context
import androidx.compose.foundation.lazy.grid.LazyGridState
import com.training.shoplocal.AppShopLocal.Companion.appContext
import com.training.shoplocal.classes.Brand
import com.training.shoplocal.classes.Category
import com.training.shoplocal.classes.DataDisplay
import com.training.shoplocal.classes.Product
import com.training.shoplocal.classes.downloader.ImageLinkDownloader
import com.training.shoplocal.classes.searcher.SearchQueryStorage
import com.training.shoplocal.getCacheDirectory
import com.training.shoplocal.log
import com.training.shoplocal.loginview.LoginViewState
import com.training.shoplocal.screens.ScreenRouter
import java.io.File

/*data class LazyScrollData (
    var firstVisibleItemIndex: Int,
    var firstVisibleItemScrollOffset: Int
    )*/

class Repository: DAOinterface {
   // private val screenData = HashMap<ScreenRouter.KEYSCREEN, Pair<List<Product>, LazyScrollData>>()
    //private var snapshotData: Pair<MutableList<Product>, LazyGridState>? = null
    private val dataDisplay = DataDisplay()
    val loginState = LoginViewState.getLoginState()

  /*  /**
     * Сохраняем текущие данные списка продуктов
     * @param product список продуктов текущего экрана
     * @param state состояние LazyGrid
     */
    fun saveCurrentScreenData(products: List<Product>, state: LazyGridState, key: ScreenRouter.KEYSCREEN){
        screenData[key] = products to LazyScrollData(state.firstVisibleItemIndex, state.firstVisibleItemScrollOffset)
        //snapshotData = products.toMutableList() to state
    }
    /**
     * Восстанавливаем список продуктов текущего экрана
     * @return Pair&lt;LazyGridState, List<Product>&gt;?, где List&lt;Product&gt; - список продуктов, state - состояние LazyGrid
     */
    fun restoreCurrentScreenData(key: ScreenRouter.KEYSCREEN): LazyScrollData? =
        screenData[key]?.second*/

    /**
     *  Реализация методов для получения доступа (регистрация, вход по паролю,
     *  восстановление, вход по отпечатку)
     *  accessUser - класс реализующий все операции, поведение определено
     *  интерфейсом AccessUserInterface
     */
    override var accessUser: AccessUserInterface = AccessUser().apply {
        this.updateViewWhen(loginState)
    }

    override var databaseCRUD: DatabaseCRUDInterface = DatabaseCRUD()

    /** Context типа FragmentActivity главной активити приходится тянуть для отображения
     *  BiometricPrompt - диалог сканирования отпечатка
     */
    fun setContextFingerPrint(context: Context){
        accessUser.getContextFingerPrint(context)
    }
    fun onRestoreUser(action: ((result: Boolean) -> Unit)?, email: String, password: String) {
        accessUser.onRestoreUser(action, email, password)
    }
    fun onRegisterUser(action: ((result: Boolean) -> Unit)?, vararg userdata: String) {
        accessUser.onRegisterUser(action, *userdata)
    }
    fun onLogin(action: (result: Int) -> Unit, email: String, password: String, finger: Boolean = false) {
        accessUser.onLogin(action, email, password, finger)
    }
    fun onFingerPrint(action: ((result: Int) -> Unit)?, email: String) {
        accessUser.onFingerPrint(action, email)
    }
    fun removePassword(){
        accessUser.onRemoveUserPassword()
    }

    fun getDataDisplay() = dataDisplay

    /**
     *  Реализация методов для получения данных из базы данных MySQL
     */
    fun getProduct(id: Int, action: (product: Product) -> Unit){
        databaseCRUD.getProduct(id, action)
    }

    fun getPromoProducts(id: Int, part: Int, action: (products: List<Product>) -> Unit){
        databaseCRUD.getPromoProducts(id, part, action)
    }

    fun getBrands(action: (brands: List<Brand>) -> Unit){
        databaseCRUD.getBrands(action)
    }

    fun getCategories() {
        databaseCRUD.getCategories()
    }

    suspend fun updateFavorite(id_user: Int, id_product: Int, value: Byte){
        databaseCRUD.updateFavorite(id_user, id_product, value)
    }

    suspend fun getProducts(id: Int, part: Int): List<Product> =
        databaseCRUD.getProducts(id, part)

     fun getSearchHistoryList(): List<String> {
         //return listOf("asdfasdf")
      /*  val list = mutableListOf<String>()
        for (i in 0..50)
            list.add("Поисковая строка $i")
        return SearchQueryStorage.getInstance().getQueries()*/
        return listOf(
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
         "Мой домашний крокодил / Lyle, Lyle, Crocodile (2022) BDRip 720p от ExKinoRay | Jaskier")
    }

    fun disposeSearchHistoryList(){
        SearchQueryStorage.getInstance().dispose()
    }

    private val MapScreenProducts = HashMap<ScreenRouter.KEYSCREEN, List<Product>>()
    fun saveScreenProducts(key: ScreenRouter.KEYSCREEN, products: List<Product>) {
        MapScreenProducts[key] = products.toList()
    }
    fun restoreScreenProducts(key: ScreenRouter.KEYSCREEN): List<Product> =
        MapScreenProducts[key] ?: listOf()


}