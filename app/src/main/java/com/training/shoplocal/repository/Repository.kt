package com.training.shoplocal.repository


import android.util.Base64
import android.content.Context
import androidx.compose.foundation.lazy.grid.LazyGridState
import com.training.shoplocal.AppShopLocal.Companion.appContext
import com.training.shoplocal.FieldFilter
import com.training.shoplocal.classes.*
import com.training.shoplocal.classes.downloader.ImageLinkDownloader
import com.training.shoplocal.classes.searcher.SearchQueryStorage
import com.training.shoplocal.encodeBase64
import com.training.shoplocal.getCacheDirectory
import com.training.shoplocal.log
import com.training.shoplocal.loginview.LoginViewState
import com.training.shoplocal.screens.ScreenRouter
import java.io.File
import java.util.*
import kotlin.collections.HashMap

class Repository: DAOinterface {
    //private val dataDisplay = DataDisplay()
    val loginState = LoginViewState.getLoginState()

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

    //fun getDataDisplay() = dataDisplay

    /**
     *  Реализация методов для получения данных из базы данных MySQL
     */
    fun getProduct(id: Int, action: (product: Product) -> Unit){
        databaseCRUD.getProduct(id, action)
    }

    fun getPromoProducts(id: Int, part: Int, action: (products: List<Product>) -> Unit){
        val order64 = encodeBase64(OrderDisplay.getOrderDislayQuery())
        databaseCRUD.getPromoProducts(id, part, order64, action)
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


    /**
     *  Блок методов для управления журналом поисковых запросов
     */
    fun getSearchHistoryList(fromFile: Boolean): List<String> {
       return SearchQueryStorage.getInstance().getQueries(fromFile)
    }

    fun disposeSearchHistoryList(){
        SearchQueryStorage.getInstance().dispose()
    }

    fun putSearchHistoryQuery(value: String) {
        SearchQueryStorage.getInstance().put(value)
    }

    fun removeSearchHistoryQuery(index: Int){//value: String) {
        SearchQueryStorage.getInstance().remove(index)
    }

    fun saveSearchHistory() {
        SearchQueryStorage.getInstance().saveQueries()
    }

    fun clearSearchHistory() {
        SearchQueryStorage.getInstance().removeAllQueries()
    }

    fun disposeSearchHistory() {
        SearchQueryStorage.getInstance().dispose()
    }
    /**
     *  End Блок методов для управления журналом поисковых запросов
     */


    private val MapScreenProducts = HashMap<ScreenRouter.KEYSCREEN, List<Product>>()
    /**
     * Сохраняем текущие данные списка продуктов
     * @param key идентификатор screen
     * @param product список продуктов текущего экрана
    */
    fun saveScreenProducts(key: ScreenRouter.KEYSCREEN, products: List<Product>) {
        MapScreenProducts[key] = products.toList()
    }
    /**
     * Восстанавливаем список продуктов текущего экрана
     * @param key идентификатор screen
     * @return список продуктов List&ltProduct&gt;
    */
    fun restoreScreenProducts(key: ScreenRouter.KEYSCREEN): MutableList<Product> {
        val list = MapScreenProducts[key]?.toMutableList() ?: mutableListOf()
        MapScreenProducts.remove(key)
        return list
    }


    fun getOrderDisplay(field: FieldFilter): Any{
        val orderDisplay = OrderDisplay.getInstance()
        return when (field) {
            FieldFilter.SORT_TYPE     -> orderDisplay.getSortType()
            FieldFilter.SORT_PROPERTY -> orderDisplay.getSortProperty()
            FieldFilter.BREND         -> orderDisplay.getBrend()
            FieldFilter.CATEGORY      -> orderDisplay.getCategory()
            FieldFilter.FAVORITE      -> orderDisplay.getFavorite()
            FieldFilter.PRICE_RANGE   -> orderDisplay.getPriceRange()
        }
    }

    fun<T> setOrderDisplay(field: FieldFilter, value: T){
        val orderDisplay = OrderDisplay.getInstance()
        when (field) {
            FieldFilter.SORT_TYPE     -> orderDisplay.setSortType(value as SORT_TYPE)
            FieldFilter.SORT_PROPERTY -> orderDisplay.setSortProperty(value as SORT_PROPERTY)
            FieldFilter.BREND         -> orderDisplay.setBrend(value as Int)
            FieldFilter.CATEGORY      -> orderDisplay.setCategory(value as Int)
            FieldFilter.FAVORITE      -> orderDisplay.setFavorite(value as Int)
            FieldFilter.PRICE_RANGE   -> orderDisplay.setPriceRange(value as Pair<Float, Float>)
        }
    }

    private fun getFoundProducts(query: String,
                                 order: String,
                                 portion: Int,
                                 uuid: String,
                                 userid: Int, action: (products: List<Product>) -> Unit){
        databaseCRUD.getFoundProducts(query, order, portion, uuid, userid, action)
    }

    /**
     * @param query строка поиска
     * @param portion порция (часть) подгружаемых данных, значение -1 - инициализация выполнения поискового запроса
     * @param UUID_query уникальный id запроса
     * @param userId id пользователя
     * @param order порядок и фильтр отображения списка продуктов
     */
    fun findProductsRequest(query: String, portion: Int, UUID_query: String, userId: Int, action: (products: List<Product>) -> Unit ){
        val order64 = encodeBase64(OrderDisplay.getOrderDislayQuery())
        val query64 = encodeBase64(query)
        log(query64)
        log(order64)
        getFoundProducts(query64, order64, portion, UUID_query, userId, action)
    }

}