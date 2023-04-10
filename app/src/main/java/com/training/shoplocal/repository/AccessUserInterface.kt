package com.training.shoplocal.repository

import android.content.Context
import com.training.shoplocal.loginview.LoginViewState

interface AccessUserInterface {
    val loginState: LoginViewState
    fun onLogin(action: ((result: Int) -> Unit)?, email: String, password: String, finger: Boolean = false)
    fun onRegisterUser(action: ((result: Boolean) -> Unit)?, vararg userdata: String, )
    fun onRestoreUser(action: ((result: Boolean) -> Unit)?, email: String, password: String)
    fun onFingerPrint(action: ((result: Int) -> Unit)?, email: String)
    fun getContextFingerPrint(context: Context)
    fun onRemoveUserPassword()
}