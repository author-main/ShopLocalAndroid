package com.training.shoplocal.repository

import android.content.Context
import com.training.shoplocal.buttonpanel.userfingerprint.UserFingerPrint
import com.training.shoplocal.buttonpanel.userfingerprint.UserPasswordStorage
import com.training.shoplocal.loginview.LoginViewState

class Repository: CrudInterface {
    private var userFingetPrint: UserFingerPrint? = null
    val passwordState = LoginViewState.getPasswordState().apply {
        this.onLogin = {
            login(it)
        }
    }

    private fun login(value: String):Boolean {
        return false
    }

    fun getUserFingerPrint(context: Context) {
        userFingetPrint = if (UserFingerPrint.canAuthenticate())
                                UserFingerPrint(context).apply {
                                    addPasswordStorage(UserPasswordStorage())
                                }
                                else
                                    null

    }

}