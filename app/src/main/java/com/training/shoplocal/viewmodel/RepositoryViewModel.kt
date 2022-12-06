package com.training.shoplocal.viewmodel

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.annotations.SerializedName
import com.training.shoplocal.*
import com.training.shoplocal.classes.*
import com.training.shoplocal.repository.Repository
import com.training.shoplocal.screens.ScreenItem
import com.training.shoplocal.screens.ScreenRouter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URL

class RepositoryViewModel(private val repository: Repository) : ViewModel() {
    private var updatePortion = false
    private var lastPortion  = 0
    private val _selectedProduct = MutableStateFlow<Product>(Product())
    val selectedProduct = _selectedProduct.asStateFlow()

    @JvmName("setSelectedProduct1")
    fun setSelectedProduct(product: Product){
       //  log("changed Selected Product, favorite = ${product.favorite}")
        _selectedProduct.value = product.copy(favorite = product.favorite)
    }

    private var USER_ID: Int = -1
    private var brands = listOf<Brand>()
    private val _products = MutableStateFlow<MutableList<Product>>(mutableListOf<Product>())
    val products = _products.asStateFlow()
    private val actionLogin: (result: Int) -> Unit = {
        val result = it > 0
        if (result) {
            USER_ID = it
            getBrands()
            getPromoProducts(1)
            //getPromoProducts(1)//Product(33)
            ScreenRouter.navigateTo(ScreenItem.MainScreen)
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

    fun getDataDisplay(field: FieldFilter): Any{
        val dataDisplay = repository.getDataDisplay()
        return when (field) {
            FieldFilter.SORT_TYPE     -> dataDisplay.getSortType()
            FieldFilter.SORT_PROPERTY -> dataDisplay.getSortProperty()
            FieldFilter.BREND         -> dataDisplay.getBrend()
            FieldFilter.CATEGORY      -> dataDisplay.getCategory()
            FieldFilter.FAVORITE      -> dataDisplay.getFavorite()
            FieldFilter.PRICE_RANGE   -> dataDisplay.getPriceRange()
        }
    }

    fun<T> setDataDisplay(field: FieldFilter, value: T){
        val dataDisplay = repository.getDataDisplay()
        when (field) {
            FieldFilter.SORT_TYPE     -> dataDisplay.setSortType(value as SORT_TYPE)
            FieldFilter.SORT_PROPERTY -> dataDisplay.setSortProperty(value as SORT_PROPERTY)
            FieldFilter.BREND         -> dataDisplay.setBrend(value as Int)
            FieldFilter.CATEGORY      -> dataDisplay.setCategory(value as Int)
            FieldFilter.FAVORITE      -> dataDisplay.setFavorite(value as Int)
            FieldFilter.PRICE_RANGE   -> dataDisplay.setPriceRange(value as Pair<Float, Float>)
        }
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

    private fun getPromoProducts(part: Int){
        if (updatePortion)
            return
        updatePortion = true
      /*  val skip = lastPortion in 1..part
        if (skip) {
            portion -= 1
            return
        }*/
      //  log("part = $part")
        if (lastPortion == part) {
            updatePortion = false
            return
        }
        repository.getPromoProducts(USER_ID, part) { products ->
            if (products.isNotEmpty()) {
                setSelectedProduct(Product())
                val list = _products.value.toMutableList().apply {
                    addAll(products)
                }
                _products.value = list//products.toMutableList()
                lastPortion = part
            }
            updatePortion = false
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
        getPromoProducts(lastPortion + 1)
    }

 }