package com.training.shoplocal.repository

import android.content.Context
import com.training.shoplocal.log
import com.training.shoplocal.userfingerprint.UserFingerPrint
import com.training.shoplocal.userfingerprint.UserPasswordStorage
import com.training.shoplocal.loginview.AccessUserInterface
import com.training.shoplocal.loginview.LoginViewState

class Repository: CrudInterface, AccessUserInterface {
    private var userFingetPrint: UserFingerPrint? = null
    val passwordState = LoginViewState.getPasswordState()

    init {
        passwordState.addOnAccessUser(this)
    }

    fun getPassword(): String =
        passwordState.getPassword()

    fun getEmail(): String =
        passwordState.getEmail()

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
        log(email)
    }

    override fun onFingerPrint(email: String) {
        userFingetPrint?.authenticate()
    }
}