package com.training.shoplocal.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.training.shoplocal.repository.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first

class RepositoryViewModel(private val repository: Repository) : ViewModel() {
    private val _snackbarData = MutableStateFlow(Pair<String, Boolean>("", false))
    val snackbarData = _snackbarData.asStateFlow()
    fun showSnackbar(message: String = "", visible: Boolean = true){
        _snackbarData.value = Pair(message, visible)
    }


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
}