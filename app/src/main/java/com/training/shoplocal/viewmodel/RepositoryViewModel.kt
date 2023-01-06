package com.training.shoplocal.viewmodel

import android.content.Context
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.annotations.SerializedName
import com.training.shoplocal.*
import com.training.shoplocal.classes.*
import com.training.shoplocal.classes.searcher.SearchQueryStorage
import com.training.shoplocal.repository.Repository
import com.training.shoplocal.screens.ScreenItem
import com.training.shoplocal.screens.ScreenRouter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URL
import java.util.UUID
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import kotlin.math.max

class RepositoryViewModel(private val repository: Repository) : ViewModel() {
    private var maxPortion: Int = 0
    private var UUID_query: UUID = UUID.randomUUID()
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
            maxPortion = 0
            USER_ID = it
            ScreenRouter.navigateTo(ScreenItem.MainScreen)
            getBrands()
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


    fun<T> setOrderDisplay(field: FieldFilter, value: T){
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

    private fun getProducts(part: Int){
       // log("portition = $part")
        log("getProducts")
        if (part != 1 && loadedPortion == maxPortion) return

        if (!lockDB){// && loadedPortion != part) {
            lockDB = true

            repository.getProducts(USER_ID, part) { listProducts ->
                lockDB = false
                //log ("portion $part")
                if (listProducts.isNotEmpty()) {
                    if (part == 1) {
                        if (listProducts[0].name.startsWith('<')) {
                            val index = listProducts[0].name.indexOf('>')
                            try {
                                if (index != -1) {
                                    val count = listProducts[0].name.substring(1, index).toInt()
                                    val name = listProducts[0].name.substring(index + 1)
                                    listProducts[0].name = name
                                    maxPortion = getPortion(count)
                                }
                            } catch (_: Exception) {
                                maxPortion = 0
                            }
                        }
                    }
                    loadedPortion = part
                    setSelectedProduct(Product())
                    val list = _products.value.toMutableList().apply { addAll(listProducts) }
                    _products.value = list
                }
            }
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

    @Synchronized
    fun getNextPortionData(){
       // log("getNextPortionData")
        getProducts(getPortion()+1)
    }

    fun saveScreenProducts(key: ScreenRouter.KEYSCREEN) {
        repository.saveScreenProducts(key, products.value)
    }

    fun restoreScreenProducts(key: ScreenRouter.KEYSCREEN) {
        _products.value = repository.restoreScreenProducts(key)//.toMutableList()
        loadedPortion = getPortion()
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

    fun findProductsRequest(query: String){
        log("findProductsRequest")
        val portion: Int = 1
        UUID_query = UUID.randomUUID()
        repository.findProductsRequest(query, portion, UUID_query.toString(), USER_ID) {listFound ->

            setSelectedProduct(Product())
            products.value.clear()
            _products.value = listFound.toMutableList()
        }
        /*INSERT INTO new_table_name
        SELECT labels.label,shortabstracts.ShortAbstract,images.LinkToImage,types.Type
        FROM ner.images,ner.labels,ner.shortabstracts,ner.types
        WHERE labels.Resource=images.Resource AND labels.Resource=shortabstracts.Resource
                AND labels.Resource=types.Resource;*/
    }

 }