package com.training.shoplocal.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.training.shoplocal.*
import com.training.shoplocal.classes.Product
import com.training.shoplocal.classes.SORT_PROPERTY
import com.training.shoplocal.classes.SORT_TYPE
import com.training.shoplocal.repository.Repository
import com.training.shoplocal.screens.ScreenItem
import com.training.shoplocal.screens.ScreenRouter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class RepositoryViewModel(private val repository: Repository) : ViewModel() {
    /*private val sortData = SortData()
    private val filterData = FilterData()*/
    //val activeProduct = Product()

    private val actionLogin: (result: Int) -> Unit = {
        val result = it > 0
        if (result) {
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
    /*fun getUserFingerPrint(context: Context) =
        repository.getUserFingerPrint(context)*/

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

    fun getDataDisplay(field: Field_Filter): Any{
        val dataDisplay = repository.getDataDisplay()
        return when (field) {
            Field_Filter.SORT_TYPE     -> dataDisplay.getSortType()
            Field_Filter.SORT_PROPERTY -> dataDisplay.getSortProperty()
            Field_Filter.BREND         -> dataDisplay.getBrend()
            Field_Filter.CATEGORY      -> dataDisplay.getCategory()
            Field_Filter.FAVORITE      -> dataDisplay.getFavorite()
            Field_Filter.PRICE_RANGE   -> dataDisplay.getPriceRange()
        }
    }

    fun<T> setDataDisplay(field: Field_Filter, value: T){
        val dataDisplay = repository.getDataDisplay()
        when (field) {
            Field_Filter.SORT_TYPE     -> dataDisplay.setSortType(value as SORT_TYPE)
            Field_Filter.SORT_PROPERTY -> dataDisplay.setSortProperty(value as SORT_PROPERTY)
            Field_Filter.BREND         -> dataDisplay.setBrend(value as Int)
            Field_Filter.CATEGORY      -> dataDisplay.setCategory(value as Int)
            Field_Filter.FAVORITE      -> dataDisplay.setFavorite(value as Int)
            Field_Filter.PRICE_RANGE   -> dataDisplay.setPriceRange(value as Pair<Float, Float>)
        }
    }

    fun passContextFingerPrint(context: Context) {
        repository.setContextFingerPrint(context)
    }

    fun getProducts(): MutableList<Product>? {
        return repository.getProducts()
    }


}