package com.training.shoplocal.repository


import android.content.Context
import androidx.compose.ui.text.style.TextOverflow
import com.training.shoplocal.classes.*
import com.training.shoplocal.classes.fodisplay.FieldFilter
import com.training.shoplocal.classes.fodisplay.OrderDisplay
import com.training.shoplocal.classes.fodisplay.SORT_PROPERTY
import com.training.shoplocal.classes.fodisplay.SORT_TYPE
import com.training.shoplocal.classes.screenhelpers.DataScreen
import com.training.shoplocal.classes.searcher.SearchQueryStorage
import com.training.shoplocal.encodeBase64
import com.training.shoplocal.loginview.LoginViewState
import com.training.shoplocal.screens.ScreenRouter
import retrofit2.Response
import javax.inject.Inject
import kotlin.collections.HashMap

class Repository @Inject constructor(
    override val databaseCRUD: DatabaseCRUDInterface
): DAOinterface {
    override lateinit var accessUser: AccessUserInterface

//private val dataDisplay = DataDisplay()
   // val loginState = accessUser.loginState
  /*  init {
        DiskCache.initStorage(AppShopLocal.appContext().applicationInfo.dataDir + "/cache/")
        memoryCache.initStorage()
    }*/



    /**
     *  Реализация методов для получения доступа (регистрация, вход по паролю,
     *  восстановление, вход по отпечатку)
     *  accessUser - класс реализующий все операции, поведение определено
     *  интерфейсом AccessUserInterface
     */
    /*override var accessUser: AccessUserInterface = AccessUser().apply {
        this.updateViewWhen(loginState)
    }

    override var databaseCRUD: DatabaseCRUDInterface = DatabaseCRUD()*/

    /** Context типа FragmentActivity главной активити приходится тянуть для отображения
     *  BiometricPrompt - диалог сканирования отпечатка
     */

  /*  fun setAccessUser(value: AccessUserInterface){
        accessUser = value
    }*/

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
      /*  val actionState : (result: Int) -> Unit = {
            loginState.fillPassword()
            action?.invoke(it)
        }
        accessUser.onFingerPrint(actionState, email)*/
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

    fun getProducts(id: Int, part: Int, action: (products: List<Product>) -> Unit){
        //val order64 = encodeBase64(OrderDisplay.getOrderDislayQuery())
        //log(OrderDisplay.getFilterQuery())
        //log(OrderDisplay.getFilterQuery())
        val order64 = encodeBase64(OrderDisplay.getFilterQuery())
        //log(order64)
        databaseCRUD.getProducts(id, part, order64, action)
    }

    fun getBrands(action: (brands: List<Brand>) -> Unit){
        databaseCRUD.getBrands(action)
    }

    fun getCategories(action: (categories: List<Category>) -> Unit) {
        databaseCRUD.getCategories(action)
    }

    suspend fun updateFavorite(id_user: Int, id_product: Int, value: Byte): Response<Int>{
        return databaseCRUD.updateFavorite(id_user, id_product, value)
    }

    suspend fun updateUserMessage(id_user: Int, what: Int, id_message: String): Response<Int>{
        return databaseCRUD.updateUserMessage(id_user, what, id_message)
    }

    /*suspend fun getProducts(id: Int, part: Int): List<Product> =
        databaseCRUD.getProducts(id, part)*/


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


    private val MapScreenProducts = HashMap<ScreenRouter.KEYSCREEN, DataScreen>()
    /**
     * Сохраняем текущие данные списка продуктов
     * @param key идентификатор screen
     * @param data данные текущего экрана
    */
    fun saveScreenProducts(key: ScreenRouter.KEYSCREEN, data: DataScreen) {
        MapScreenProducts[key] = data.copy(products = data.products.toMutableList())//maxPortion to products.toList()
     //   log(MapScreenProducts[key]?.products?.size.toString())
    }
    /**
     * Восстанавливаем список продуктов текущего экрана
     * @param key идентификатор screen
     * @return данные текущего экрана
    */
    fun clearMapScreenProducts(){
        MapScreenProducts.clear()
    }

    fun restoreScreenProducts(key: ScreenRouter.KEYSCREEN): DataScreen {
        //список продуктов List&ltProduct&gt;
      //  log(MapScreenProducts[key]?.firstItemIndex.toString())
        val dataScreen =
            MapScreenProducts[key]?.copy() ?: DataScreen()
        MapScreenProducts.remove(key)
        return dataScreen
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
            FieldFilter.SCREEN        -> orderDisplay.getScreen()
            FieldFilter.DISCOUNT      -> orderDisplay.getDiscount()
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun<T> setOrderDisplay(field: FieldFilter, value: T){
        val orderDisplay = OrderDisplay.getInstance()
        when (field) {
            FieldFilter.SORT_TYPE     -> orderDisplay.setSortType(value as SORT_TYPE)
            FieldFilter.SORT_PROPERTY -> orderDisplay.setSortProperty(value as SORT_PROPERTY)
            FieldFilter.BREND         -> orderDisplay.setBrend(value as String)
            FieldFilter.CATEGORY      -> orderDisplay.setCategory(value as String)
            FieldFilter.FAVORITE      -> orderDisplay.setFavorite(value as Int)
            FieldFilter.PRICE_RANGE   -> {
                val rangePrice: Pair<Float, Float> = value as Pair<Float, Float>
                orderDisplay.setPriceRange(rangePrice.first, rangePrice.second)
            }
            FieldFilter.DISCOUNT      -> orderDisplay.setDiscount(value as Int)
            FieldFilter.SCREEN        -> orderDisplay.setScreen(value as Int)
        }
    }

    private fun getFoundProducts(query: String,
                                 order: String,
                                 portion: Int,
                                 uuid: String,
                                 userid: Int,
                                 action: (products: List<Product>) -> Unit){
        databaseCRUD.getFoundProducts(query, order, portion, uuid, userid, action)
    }

    fun getReviewProduct(id: Int,
                         action: (reviews: List<Review>) -> Unit){
        databaseCRUD.getReviewProduct(id, action)
    }



    /**
     * @param query строка поиска
     * @param portion порция (часть) подгружаемых данных, значение -1 - инициализация выполнения поискового запроса
     * @param UUID_query уникальный id запроса
     * @param userId id пользователя
     */
    fun findProductsRequest(query: String, portion: Int, UUID_query: String, userId: Int, action: (products: List<Product>) -> Unit ){
        /*val orderDisplay = OrderDisplay.getInstance()
        orderDisplay.setSortType(SORT_TYPE.DESCENDING)
        orderDisplay.setSortProperty(SORT_PROPERTY.POPULAR)
        orderDisplay.setBrend(1)
        orderDisplay.setCategory(3)
        orderDisplay.setFavorite(1)
        orderDisplay.setPriceRange(10.00f to 100.00f)*/
        //log(UUID_query)
        /*log("filter ${OrderDisplay.getFilterQuery()}")
        log("search query $query")*/
        val order64 = encodeBase64(OrderDisplay.getFilterQuery())
        val query64 = encodeBase64(query)
        /*log("query = $query64")
        log("filter = $order64")
        log("UUID = $UUID_query")
        log("userid = $userId")*/

        getFoundProducts(query64, order64, portion, UUID_query, userId, action)
    }

    fun getMessages(id: Int,
                    action: (userMessages: List<UserMessage>) -> Unit){
        databaseCRUD.getMessages(id, action)
    }

    fun resetLoginData() {
        accessUser.loginState.reset()
    }

    fun getPassword() =
        accessUser.loginState.getPassword()

    fun setEmail(value: String) {
        accessUser.loginState.setEmail(value)
    }



}