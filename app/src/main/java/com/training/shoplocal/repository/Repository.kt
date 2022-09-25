package com.training.shoplocal.repository

import android.content.Context
import com.training.shoplocal.buttonpanel.userfingerprint.UserFingerPrint
import com.training.shoplocal.buttonpanel.userfingerprint.UserPasswordStorage
import com.training.shoplocal.loginview.AccessUserInterface
import com.training.shoplocal.loginview.LoginViewState
import com.training.shoplocal.validateMail

class Repository: CrudInterface, AccessUserInterface {
    private var userFingetPrint: UserFingerPrint? = null
    val passwordState = LoginViewState.getPasswordState()

    init {
        passwordState.addOnAccessUser(this)
    }

    fun getUserFingerPrint(context: Context) {
        userFingetPrint = if (UserFingerPrint.canAuthenticate())
                                UserFingerPrint(context).apply {
                                    addPasswordStorage(UserPasswordStorage())
                                }
                                else
                                    null

    }

    override fun onLogin(password: String): Boolean {
       return false
    }

    override fun onRegisterUser() {
        TODO("Not yet implemented")
    }

    override fun onRestoreUser(email: String) {
        TODO("Not yet implemented")
    }

    override fun onFingerPrint(email: String) {
    }
}