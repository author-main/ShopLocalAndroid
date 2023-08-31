package com.training.shoplocal.viewmodel

import android.content.Context
import android.os.*
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.training.shoplocal.*
import com.training.shoplocal.AppShopLocal.Companion.appComponent
import com.training.shoplocal.classes.*
import com.training.shoplocal.classes.downloader.DiskCache
import com.training.shoplocal.classes.downloader.ImageLinkDownloader
import com.training.shoplocal.classes.downloader.MemoryCache
import com.training.shoplocal.classes.fodisplay.FieldFilter
import com.training.shoplocal.classes.fodisplay.ItemFilter
import com.training.shoplocal.classes.fodisplay.OrderDisplay
import com.training.shoplocal.classes.screenhelpers.DataScreen
import com.training.shoplocal.repository.Repository
import com.training.shoplocal.screens.ScreenRouter
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.HashMap

class RepositoryViewModel(private val repository: Repository) : ViewModel() {
    enum class LoadState {IDLE, LOADING}
    private var loadState = LoadState.IDLE
    private var loadedPortion = 0
    private val _refreshUserMessages = MutableStateFlow<Boolean>(false)
    val refreshUserMessages = _refreshUserMessages.asStateFlow()
    fun updateUserMessages(){
        _refreshUserMessages.value = true
        getMessages()
    }
    private val _countUnreadMessages = MutableStateFlow<Int>(0)
    val countUnreadMessages = _countUnreadMessages.asStateFlow()
    fun setCountUnreadMessages(value: Int){
        _countUnreadMessages.value = value
    }
    private var _userMessages = mutableStateListOf<UserMessage>()
    val userMessages: List<UserMessage> = _userMessages
    private fun setUserMessages(value: List<UserMessage>){
        _userMessages.apply {
            clear()
            addAll(value)
        }
    }
    private val _reviews = MutableStateFlow(listOf<Review>())
    val reviews = _reviews.asStateFlow()
    private fun setReviews(value: List<Review>) {
        _reviews.value = value
    }

    fun clearReviews(){
        _reviews.value = listOf()
    }

    private var onCloseApp: (() -> Unit)? = null
    fun setOnCloseApp(value:() -> Unit ) {
        onCloseApp = value
    }
    fun closeApp(){
        USER_TOKEN = null
        repository.resetLoginData()
        _products.value.clear()
        repository.clearMapScreenProducts()
        brands.values.clear()
        categories.values.clear()
        OrderDisplay.resetFilter()
        containerStack.removeAllElements()
        onCloseApp?.invoke()
    }

    private var searchQuery: String = EMPTY_STRING
    private val _progressCRUD = MutableStateFlow<Boolean>(false)
    val progressCRUD = _progressCRUD.asStateFlow()
    private fun showProgressCRUD() {
        _progressCRUD.value = true
    }
    private fun hideProgressCRUD() {
        _progressCRUD.value = false
    }

    private val _activeContainer = MutableStateFlow(Container.LOGIN)
    val activeContainer = _activeContainer.asStateFlow()
    private val containerStack = Stack<Container>().apply {
        push(Container.LOGIN)
    }
    private val UUID_QUERY = System.nanoTime().toString()
    private val _hiddenBottomNavigation = MutableStateFlow<Boolean>(false)
    val hiddenBottomNavigation = _hiddenBottomNavigation.asStateFlow()

    fun hideBottomNavigation() {
        _hiddenBottomNavigation.value = true
    }

    fun showBottomNavigation() {
        _hiddenBottomNavigation.value = false
    }

    private val _selectedProduct = MutableStateFlow<Product>(Product())
    val selectedProduct = _selectedProduct.asStateFlow()

    @JvmName("setSelectedProduct1")
    fun setSelectedProduct(product: Product){
        _selectedProduct.value = product.copy(favorite = product.favorite)
    }

    private var USER_TOKEN: String? = null
    private var brands = hashMapOf<Int, Brand>()
    private var categories = hashMapOf<Int, Category>()
    private val _products = MutableStateFlow<MutableList<Product>>(mutableListOf<Product>())
    val products = _products.asStateFlow()
    private val actionLogin: (token: String?) -> Unit = {token ->
        if (token != null) {
            hideSnackbar()
            USER_TOKEN = token
            containerStack.pop()
            putComposeViewStack(Container.MAIN)
            getBrands()
            getCategories()
            getMessages(requestNumberUnread = true)
            loadedPortion = 0
            getProducts()
            authorizeUser()
        }
        else {
            showSnackbar(message = getStringResource(R.string.message_login_error), type = MESSAGE.ERROR)
            vibrate(400)
        }
    }
    private val _authorizedUser = MutableStateFlow<Boolean>(false)
    val authorizedUser = _authorizedUser.asStateFlow()
    private val _snackbarData = MutableStateFlow(Triple<String, Boolean, MESSAGE>("", false, MESSAGE.INFO))
    val snackbarData = _snackbarData.asStateFlow()
    fun showSnackbar(message: String = "", type: MESSAGE = MESSAGE.INFO, visible: Boolean = true){
        _snackbarData.value = Triple(message, visible, type)
    }
    private fun hideSnackbar(){
        _snackbarData.value = Triple("", false, MESSAGE.INFO)
    }

    private fun authorizeUser(value: Boolean = true){
        _authorizedUser.value = value
    }
    fun getLoginState() = repository.accessUser.loginState

    fun getPassword()   = repository.getPassword()

    fun removePassword()   = repository.removePassword()

    fun setEmail(value: String)      = repository.setEmail(value)

    fun onRestoreUser(action: ((result: Boolean) -> Unit)?, email: String, password: String) {
        repository.onRestoreUser(action, email, password)
    }

    fun onRegisterUser(action: ((result: Boolean) -> Unit)?, vararg userdata: String) {
        repository.onRegisterUser(action, *userdata)
    }

    fun onLogin(email: String, password: String, finger: Boolean = false) {
        repository.onLogin({ token ->
            actionLogin(token)
        }, email, password, finger)

    }

    fun onFingerPrint(email: String) {
        repository.onFingerPrint(actionLogin, email)
    }

    fun getOrderDisplay(field: FieldFilter) =
        repository.getOrderDisplay(field)


    private fun<T> setOrderDisplay(field: FieldFilter, value: T){
        repository.setOrderDisplay(field, value)
    }

    private fun getProducts(){
        if (!validToken()) return
        if (loadState != LoadState.IDLE) return
        showProgressCRUD()
        loadState = LoadState.LOADING
        val portion = loadedPortion + 1
        repository.getProducts(USER_TOKEN!!, portion) { listProducts ->
            if (listProducts.isNotEmpty()) {
                loadedPortion++
                if (loadedPortion == 1)
                    _products.value.clear()
                _products.value.addAll(listProducts)
            }
            loadState = LoadState.IDLE
            hideProgressCRUD()
        }
    }

    private fun getBrands(){
        repository.getBrands { list ->
            brands = list.associateBy { it.id } as HashMap<Int, Brand>
        }
    }

    private fun getCategories(){
        repository.getCategories { list ->
            categories = list.associateBy { it.id } as HashMap<Int, Category>
        }
    }

    fun getBrand(id: Int) =
        brands[id]?.name ?: EMPTY_STRING
        //brands.find {it.id == id}?.name ?: ""

    fun getCategory(id: Int) =
        categories[id]?.name ?: EMPTY_STRING

    fun getSectionFilter(): HashMap<Int, MutableList<ItemFilter>>{
        val map = kotlin.collections.HashMap<Int, MutableList<ItemFilter>>()
        val list = mutableListOf<ItemFilter>()
        categories.forEach {
            list.add(
                ItemFilter(
                    id = it.key,
                    name = it.value.name
                )
            )
        }
        map[CATEGORY_ITEM] = list.toMutableList()
        list.clear()
        brands.forEach {
            list.add(
                ItemFilter(
                    id = it.key,
                    name = it.value.name
                )
            )
        }
        map[BREND_ITEM] = list
        return map
    }


    fun setProductFavorite(product: Product){
        if (!validToken()) return
        viewModelScope.launch {
          repository.updateFavorite(USER_TOKEN!!, product.id, product.favorite)
        }
        var currentProduct: Product? = null
        products.value.find { it.id== product.id }?.let {
            it.favorite = product.favorite
            currentProduct = it
        }
        if (product.favorite < 1) {
            if (OrderDisplay.getInstance().getFavorite() == 1) {
                currentProduct?.let{
                    //log("delete favorite item")
                    val list = _products.value.toMutableList()
                    list.remove(it)
                    _products.value = list
                }

            }
        }
        setSelectedProduct(product)
    }

    fun getNextPortionData(searchMode: Boolean, textSearch: String){
        if (searchMode) {
            findProductsRequest(textSearch)
        } else
            getProducts()
    }

    fun saveScreenProducts(key: ScreenRouter.KEYSCREEN, firstIndex: Int) {//, firstOffset: Int) {
        repository.saveScreenProducts(key,
            DataScreen(firstIndex, loadedPortion, products.value, OrderDisplay.clone())
        )
    }

    fun restoreScreenProducts(key: ScreenRouter.KEYSCREEN): Int{//} Pair<Int, Int> {
        val data = repository.restoreScreenProducts(key)
        OrderDisplay.restoreDataDisplay(data.datadisplay)
        _products.value = data.products//repository.restoreScreenProducts(key)//.toMutableList()
        loadedPortion = data.loadedPortion // количество загруженных порций
        return data.firstItemIndex// to data.firstItemOffset
    }
    /**
     *  Блок методов для управления журналом поисковых запросов
     */
    fun getSearchHistoryList(fromFile: Boolean = false): List<String> {
        return repository.getSearchHistoryList(fromFile)
    }

    fun disposeSearchHistoryList(){
        repository.disposeSearchHistoryList()
    }

    fun putSearchHistoryQuery(value: String) {
        repository.putSearchHistoryQuery(value)
    }

    fun removeSearchHistoryQuery(index: Int){//value: String) {
        repository.removeSearchHistoryQuery(index)
    }

    fun saveSearchHistory() {
        repository.saveSearchHistory()
    }

    fun disposeSearchHistory() {
        repository.disposeSearchHistory()
    }

    fun clearSearchHistory() {
        repository.clearSearchHistory()
    }
    /**
     *  End Блок методов для управления журналом поисковых запросов
     */

    fun findProductsRequest(query: String, clear: Boolean = false){
        if (!validToken()) return
        if (loadState != LoadState.IDLE)
            return
        loadState = LoadState.LOADING
        if (clear) { //  Очистка результатов поиcка в BD
            repository.findProductsRequest(query, 0, UUID_QUERY, USER_TOKEN!!){}
            loadState = LoadState.IDLE
            return
        }
        searchQuery = query
        showProgressCRUD()
        val portion = loadedPortion + 1
        repository.findProductsRequest(query, portion, UUID_QUERY, USER_TOKEN!!) { listFound ->
            if (listFound.isNotEmpty()) {
                loadedPortion++
                if (loadedPortion == 1)
                    _products.value.clear()
                _products.value.addAll(listFound)
            }
            loadState = LoadState.IDLE
            hideProgressCRUD()
        }
    }

    fun clearResultSearch(){
        findProductsRequest("*", true)
    }

    fun putComposeViewStack(value: Container) {
        val equalValue = containerStack.isNotEmpty() && containerStack.peek() == value
        if (!equalValue) {
            containerStack.push(value)
            _activeContainer.value = value
        }
    }

    fun getPrevContainer(): Container? {
        val prev = try {
            containerStack[containerStack.lastIndex - 1]
        } catch (_: Exception){null}
        return prev
    }

    fun prevComposeViewStack(): Container {
        containerStack.pop()
        val value = containerStack.peek()
        _activeContainer.value = value
        return value
    }

    fun filterProducts(searchMode: Boolean = false){
        loadedPortion = 0
        if (searchMode) {
            findProductsRequest(searchQuery)
        } else {
           // getProducts(1)
            getProducts()
        }
    }

    fun getReviewProduct(id: Int)
    {
        showProgressCRUD()
        repository.getReviewProduct(
            id
        ) {reviews ->
            setReviews(reviews)
            hideProgressCRUD()
        }
    }

    /**
     * Получить сообщения пользователя или количество непрочитанных сообщений
     * @param id идентификатор пользователя, если отрицательное значение - возвращает количество
     * непрочитанных сообщений (в первой записи списка, в поле id)
     */
    fun getMessages(requestNumberUnread: Boolean = false) {
        if (!validToken()) return
        repository.getMessages(USER_TOKEN!!, requestNumberUnread) {messages ->
            if (messages.isNotEmpty()) {
                if (requestNumberUnread)
                    setCountUnreadMessages(messages[0].id)
                else {
                    setUserMessages(messages)
                    setCountUnreadMessages(messages.count { it.read == 0 })
                }
            }
            _refreshUserMessages.value = false
        }
    }
    fun clearMessages(){
        _userMessages.clear()
    }

    fun markDeletedUserMessages(id: Int, deleted: Boolean = true){
        val message = _userMessages.find { msg -> msg.id == id }
        message?.let {userMessage ->
            //_userMessages[index].deleted = deleted
            userMessage.deleted = deleted
            if (deleted) {
                if (userMessage.read == 0)
                    _countUnreadMessages.value -= 1

            } else {
                if (userMessage.read == 0)
                    _countUnreadMessages.value += 1
            }
        }
    }

    fun updateUserMessage(ids: IntArray, what: Int) {
        if (!validToken()) return
        viewModelScope.launch {
              val result = 1

           /*    ******************************************************************************
                                Пока не будем изменять данные на сервере
                 ********************************************************************************/
                /*val result: Int =
                try{
                    val id_message = ids.joinToString(",")
                    log(USER_TOKEN)
                    val response = repository.updateUserMessage(USER_TOKEN!!, what, id_message)
                    response.body()?.toInt() ?: 0
                }
                catch (_: Exception) {
                    0
                }*/
            //    *****************************************************************************


            if (result > 0) {
                var recomposition = false
                val listMessages = _userMessages.toMutableList()
                if (what == USERMESSAGE_READ) {
                    var countUnread = _countUnreadMessages.value
                    ids.forEach { id ->
                        listMessages.find {
                            it.id == id
                        }?.let { message ->
                            message.read = 1
                            recomposition = true
                            countUnread -= 1
                        }
                    }
                    _countUnreadMessages.value = countUnread
                }
                if (recomposition)
                    setUserMessages(listMessages)
            }
        }

    }

    fun startLoadedData(){
        loadedPortion = 0
        loadState = LoadState.IDLE
    }

    private fun validToken() =
        USER_TOKEN != null

 }