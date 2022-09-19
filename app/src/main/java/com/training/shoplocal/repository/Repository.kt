package com.training.shoplocal.repository

import com.training.shoplocal.passwordview.PasswordViewState

class Repository: CrudInterface {
    val passwordState = PasswordViewState.getPasswordState().apply {
        this.onLogin = {
            login(it)
        }
    }

    private fun login(value: String):Boolean {
        return false
    }
}