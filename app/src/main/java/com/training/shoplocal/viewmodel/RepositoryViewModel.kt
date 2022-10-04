package com.training.shoplocal.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.training.shoplocal.repository.Repository

class RepositoryViewModel(private val repository: Repository) : ViewModel() {
    //fun getRepository() = repository
    fun getLoginState() = repository.loginState
    fun getPassword()   = repository.loginState.getPassword()
    fun getUserFingerPrint(context: Context) =
        repository.getUserFingerPrint(context)

    fun onRestoreUser(email: String) {
        repository.onRestoreUser(email)
    }

    fun onRegisterUser(vararg userdata: String) {
        repository.onRegisterUser(*userdata)
    }
}