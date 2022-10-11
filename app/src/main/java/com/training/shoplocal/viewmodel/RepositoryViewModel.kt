package com.training.shoplocal.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.training.shoplocal.repository.Repository

class RepositoryViewModel(private val repository: Repository) : ViewModel() {
    //fun getRepository() = repository
    fun getLoginState() = repository.loginState
    fun getPassword()   = repository.loginState.getPassword()
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