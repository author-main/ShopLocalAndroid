package com.training.shoplocal.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.training.shoplocal.*
import com.training.shoplocal.classes.*
import com.training.shoplocal.classes.downloader.MemoryCache
import com.training.shoplocal.classes.fodisplay.FieldFilter
import com.training.shoplocal.classes.screenhelpers.DataScreen
import com.training.shoplocal.repository.Repository
import com.training.shoplocal.screens.ScreenItem
import com.training.shoplocal.screens.ScreenRouter
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.*

class RepositoryViewModel(private val repository: Repository) : ViewModel() {
    private val composeViewStack = Stack<ComposeView>().apply {
        push(ComposeView.LOGIN)
    }
    /*var deviceUuid =
        UUID(androidId.hashCode(), tmDevice.hashCode() as Long shl 32 or tmSerial.hashCode())*/
    private val UUID_QUERY = System.nanoTime().toString()
    private var maxPortion: Int = -1
    //private val UUID_QUERY: String = UUID.randomUUID().toString()
    private var lockDB = false
    //private val reflexRepository = Repository::class.java.methods
    //log(reflexRepository.toString())
    private val _hiddenBottomNavigation = MutableStateFlow<Boolean>(false)
    val hiddenBottomNavigation = _hiddenBottomNavigation.asStateFlow()
    fun hideBottomNavigation(value: Boolean = true) {
        _hiddenBottomNavigation.value = value
    }

    private var loadedPortion = 0
    private val _selectedProduct = MutableStateFlow<Product>(Product())
    val selectedProduct = _selectedProduct.asStateFlow()

    @JvmName("setSelectedProduct1")
    fun setSelectedProduct(product: Product){
       //  log("changed Selected Product, favorite = ${product.favorite}")
        _selectedProduct.value = product.copy(favorite = product.favorite)
    }

    private var USER_ID: Int = -1
    private var brands = listOf<Brand>()

    /*private val _products = MutableStateFlow<MutableList<Product>>(mutableListOf<Product>())
    val products = _products.asStateFlow()*/

    private val _products = MutableStateFlow<MutableList<Product>>(mutableListOf<Product>())
    val products = _products.asStateFlow()

    private val actionLogin: (result: Int) -> Unit = {
        val result = it > 0
        if (result) {
            maxPortion = -1
            USER_ID = it
            ScreenRouter.navigateTo(ScreenItem.MainScreen)
            setOrderDisplay(FieldFilter.SCREEN, ScreenItem.MainScreen.key.value)
            composeViewStack.pop()
            putComposeViewStack(ComposeView.MAIN)
            getBrands()
//            exchangeDataMap[ExchangeData.GET_PRODUCTS] = false
            loadedPortion = 0
            getProducts(1)
            authorizeUser()
        }
        else
            showSnackbar(message = getStringResource(R.string.message_login_error), type = MESSAGE.ERROR)
    }
    private val _authorizedUser = MutableStateFlow<Boolean>(false)
    val authorizedUser = _authorizedUser.asStateFlow()
    private val _snackbarData = MutableStateFlow(Triple<String, Boolean, MESSAGE>("", false, MESSAGE.INFO))
    val snackbarData = _snackbarData.asStateFlow()
    fun showSnackbar(message: String = "", type: MESSAGE = MESSAGE.INFO, visible: Boolean = true){
        _snackbarData.value = Triple(message, visible, type)
    }
    private fun authorizeUser(value: Boolean = true){
        _authorizedUser.value = value
    }
    fun getLoginState() = repository.loginState

    fun getPassword()   = repository.loginState.getPassword()

    fun removePassword()   = repository.removePassword()

    fun setEmail(value: String)      = repository.loginState.setEmail(value)

    fun onRestoreUser(action: ((result: Boolean) -> Unit)?, email: String, password: String) {
        repository.onRestoreUser(action, email, password)
    }

    fun onRegisterUser(action: ((result: Boolean) -> Unit)?, vararg userdata: String) {
        repository.onRegisterUser(action, *userdata)
    }

    fun onLogin(email: String, password: String, finger: Boolean = false) {
        repository.onLogin({ result ->
            actionLogin(result)

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

    fun passContextFingerPrint(context: Context) {
        repository.setContextFingerPrint(context)
    }

    private fun getProduct(id: Int){
        repository.getProduct(id) { product ->
            //log(product.toString())
            //_products.value = products.toMutableList()
        }
    }

    /**
     * Получить max число порций, количество записей возвращается в первой строке результа
     * выборки продуктов в формате &lt;count&gt; name
     * @paran value первый элемент списка продуктов
     * @return Pair<max, name>, где max - число порций, name - наименование продукта
     */
    private fun getMaxPortion(value: String): Pair<Int, String>{
        var calcMaxPortion = 0
        var name = ""
        if (value.startsWith('<')) {
            val index = value.indexOf('>')
            try {
                if (index != -1) {
                    val count = value.substring(1, index).toInt()
                    name = value.substring(index + 1)
                    calcMaxPortion = getPortion(count)
                }
            } catch (_: Exception) {
            }
        }
        return calcMaxPortion to name
    }


    private fun getProducts(part: Int){
      // val exchangeData = exchangeDataMap[ExchangeData.GET_PRODUCTS] ?: false
        if (lockDB) return
            lockDB = true
          //  exchangeDataMap[ExchangeData.GET_PRODUCTS] = true


            /*val coroutine = CoroutineScope(Job() + Dispatchers.IO)
        coroutine.launch {*/
            repository.getProducts(USER_ID, part) { listProducts ->
                if (listProducts.isNotEmpty()) {
                    if (part == 1) {
                        val extractedData = getMaxPortion(listProducts[0].name)
                        maxPortion = extractedData.first
                        listProducts[0].name = extractedData.second
                        _products.value.clear()
                    }
                    loadedPortion = part
                    setSelectedProduct(Product())
                    val list = _products.value.toMutableList().apply { addAll(listProducts) }
                    _products.value = list
                } else {
                    if (part == 1)
                        _products.value = mutableListOf()
                }
                lockDB = false
            //}

        }
    }

    private fun getBrands(){
        repository.getBrands() { it ->
            brands = it
        }
    }

    fun getBrand(id: Int) =
        brands.find {it.id == id}?.name ?: ""

    fun setProductFavorite(id: Int, value: Boolean){
        val favorite: Byte = if (value) 1 else 0
        products.value.find { it.id== id }?.let{
            it.favorite = favorite
            setSelectedProduct(it)
            viewModelScope.launch {
                repository.updateFavorite(USER_ID, it.id, favorite)
            }
        }
    }

    fun nextPortionAvailable() =
        loadedPortion + 1<= maxPortion

    fun getNextPortionData(searchMode: Boolean, textSearch: String){
      /* val nextPortion = loadedPortion + 1//getPortion()+1
        if (nextPortion <= maxPortion) */
        if (nextPortionAvailable()) {
            //log("load partion")
            val nextPortion = loadedPortion + 1
            if (searchMode) {
                //log("next portion = $nextPortion, max portion = $maxPortion")
                findProductsRequest(textSearch, nextPortion)
            } else {
                getProducts(nextPortion)
            }
        }
    }

    fun saveScreenProducts(key: ScreenRouter.KEYSCREEN, firstIndex: Int) {//, firstOffset: Int) {
        repository.saveScreenProducts(key,
//            DataScreen(firstIndex, firstOffset, maxPortion, products.value)
            DataScreen(firstIndex, maxPortion, products.value)
        )
    }

    fun restoreScreenProducts(key: ScreenRouter.KEYSCREEN): Int{//} Pair<Int, Int> {
        val data = repository.restoreScreenProducts(key)
        maxPortion = data.maxPortion
        _products.value = data.products//repository.restoreScreenProducts(key)//.toMutableList()
        loadedPortion = getPortion() // количество загруженных порций
        //log("firstIndex ${data.firstItemIndex}, firstOffset ${data.firstItemOffset}")
        return data.firstItemIndex// to data.firstItemOffset
    }

    private fun getPortion(size: Int = 0): Int{
        val count = if (size == 0)
                        products.value.size
                    else size
        /*val value = products.value.size % SIZE_PORTION
        var portion = products.value.size/ SIZE_PORTION*/
        val value   = count % SIZE_PORTION
        var portion = count / SIZE_PORTION
        if (value > 0)
            portion += 1
        return portion
    }

    /**
     *  Блок методов для управления журналом поисковых запросов
     */
    fun getSearchHistoryList(fromFile: Boolean = false): List<String> {
/*        val callable = Callable<List<String>> {
            repository.getSearchHistoryList()
        }
        return Executors.newSingleThreadExecutor().submit(callable).get()*/
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

    fun findProductsRequest(query: String, value: Int = 0){
        if (value == -1) {
            repository.findProductsRequest(query, 0, UUID_QUERY, USER_ID){}
            return
        }


/*        val list = _products.value.toMutableList()
        list.addAll(_products.value)
        _products.value = mutableListOf()
        _products.value = list
        return*/
        if (lockDB) return
        var portion = value
        if (value == 0) {
            loadedPortion = 0
            //UUID_QUERY = UUID.randomUUID()
            portion = 1
            lockDB = false
                //_products.value.clear()// = mutableListOf()
            //log("clear products")
        }
        lockDB = true
        //log ("find portion = $portion")
        //repository.findProductsRequest(query, portion, UUID_QUERY.toString(), USER_ID) { listFound ->
            repository.findProductsRequest(query, portion, UUID_QUERY, USER_ID) { listFound ->
             //   log ("size ${listFound.size}")
                setSelectedProduct(Product())
                if (listFound.isNotEmpty()) {
                    //_products.value = listFound.toMutableList()
                    //log("get portion $portion")
                    if (portion == 1) {
                        val extractedData = getMaxPortion(listFound[0].name)
                        maxPortion = extractedData.first
                        listFound[0].name = extractedData.second
                        _products.value.clear()
                    }
                    val list = _products.value.toMutableList().apply { addAll(listFound) }
                    _products.value = list
                    loadedPortion = portion
                } else {
                    if (portion == 1)
                        _products.value = mutableListOf()
                }
                lockDB = false
            }

        /*INSERT INTO new_table_name
        SELECT labels.label,shortabstracts.ShortAbstract,images.LinkToImage,types.Type
        FROM ner.images,ner.labels,ner.shortabstracts,ner.types
        WHERE labels.Resource=images.Resource AND labels.Resource=shortabstracts.Resource
                AND labels.Resource=types.Resource;*/
    }

    fun clearResultSearch(){
        findProductsRequest("*", -1)
    }

    fun putComposeViewStack(value: ComposeView) {
        val equalValue = composeViewStack.isNotEmpty() && composeViewStack.peek() == value
        if (!equalValue)
            composeViewStack.push(value)
      //  log(composeViewStack)
    }

    fun removeComposeViewStack(): ComposeView {
//        val value = composeViewStack.pop()
//        log(composeViewStack)
        return composeViewStack.pop()
    }

    fun getComposeViewStack(): ComposeView {
        return composeViewStack.peek()
    }

    fun existImageCache(filename: String?, convert: Boolean = false):Boolean {
        if (filename == null)
            return false
        val hash = if (convert)
            md5(filename) else filename
        //log("hash = $hash")
        var exist = false
        if (MemoryCache.exist(hash))
            exist = true
        else {
            val pathFile = CACHE_DIR + filename //getCacheDirectory() + filename
          //  log("path = $pathFile")
            if (fileExists(pathFile))
                exist = true
        }
        return exist
    }


 }