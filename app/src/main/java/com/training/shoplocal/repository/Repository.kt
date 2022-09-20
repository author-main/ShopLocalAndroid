package com.training.shoplocal.repository

import com.training.shoplocal.loginview.LoginViewState

class Repository: CrudInterface {
    val passwordState = LoginViewState.getPasswordState().apply {
        this.onLogin = {
            login(it)
        }
    }

    private fun login(value: String):Boolean {
        return false
    }
}