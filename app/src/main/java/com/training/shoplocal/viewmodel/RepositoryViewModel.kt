package com.training.shoplocal.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.training.shoplocal.MESSAGE
import com.training.shoplocal.R
import com.training.shoplocal.getStringResource
import com.training.shoplocal.loginview.AccessUserInterface
import com.training.shoplocal.repository.Repository
import com.training.shoplocal.screens.ScreenItem
import com.training.shoplocal.screens.ScreenRouter
import com.training.shoplocal.validateMail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first

class RepositoryViewModel(private val repository: Repository) : ViewModel() {
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
    /*init {
        repository.loginState.addOnAccessUser(object: AccessUserInterface {
            override fun onLogin(email: String, password: String, finger: Boolean) {
                TODO("Not yet implemented")
            }

            override fun onRegisterUser(
                action: ((result: Boolean) -> Unit)?,
                vararg userdata: String
            ) {
                TODO("Not yet implemented")
            }

            override fun onRestoreUser(
                action: ((result: Boolean) -> Unit)?,
                email: String,
                password: String
            ) {
                TODO("Not yet implemented")
            }

            override fun onFingerPrint(email: String) {
                TODO("Not yet implemented")
            }
        })
    }*/


    //fun getRepository() = repository
    /*private val _snackbar = MutableStateFlow(false)
    val snackbar = _snackbar.asStateFlow()
    fun showSnackbar(value: Boolean = true){
        _snackbar.value = value
    }*/

    fun getLoginState() = repository.loginState
    fun getPassword()   = repository.loginState.getPassword()
    fun removePassword()   = repository.removePassword()
    fun setEmail(value: String)      = repository.loginState.setEmail(value)
    fun getUserFingerPrint(context: Context) =
        repository.getUserFingerPrint(context)

    fun onRestoreUser(action: ((result: Boolean) -> Unit)?, email: String, password: String) {
        repository.onRestoreUser(action, email, password)
    }

    fun onRegisterUser(action: ((result: Boolean) -> Unit)?, vararg userdata: String) {
        repository.onRegisterUser(action, *userdata)
    }



    fun onLogin(email: String, password: String, finger: Boolean = false) {
        repository.onLogin(action = { id ->
            val result = id > 0
            if (result) {
                ScreenRouter.navigateTo(ScreenItem.MainScreen)
                authorizeUser()
            }
            else
                showSnackbar(message = getStringResource(R.string.message_login_error), type = MESSAGE.ERROR)
        }, email, password, finger)
    }

    fun onFingerPrint(email: String) {
        repository.onFingerPrint(email)
    }
}