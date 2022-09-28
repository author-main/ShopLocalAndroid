package com.training.shoplocal.repository

import android.content.Context
import com.training.shoplocal.log
import com.training.shoplocal.userfingerprint.UserFingerPrint
import com.training.shoplocal.userfingerprint.UserPasswordStorage
import com.training.shoplocal.loginview.AccessUserInterface
import com.training.shoplocal.loginview.LoginViewState

class Repository: CrudInterface, AccessUserInterface {
    private var userFingerPrint: UserFingerPrint? = null
    val loginState = LoginViewState.getLoginState()

    init {
        loginState.addOnAccessUser(this)
    }

    fun getPassword(): String =
        loginState.getPassword()

    fun getEmail(): String =
        loginState.getEmail()

    fun getUserFingerPrint(context: Context) {
        userFingerPrint = if (UserFingerPrint.canAuthenticate())
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
        loginState.setEmail(email, true)
        //log(email)
    }

    override fun onFingerPrint(email: String) {
        userFingerPrint?.authenticate()
    }
}