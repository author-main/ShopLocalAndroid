package com.training.shoplocal.repository

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.fragment.app.FragmentActivity
import com.training.shoplocal.classes.User
import com.training.shoplocal.loginview.LoginViewState
import com.training.shoplocal.userfingerprint.UserFingerPrint
import com.training.shoplocal.userfingerprint.UserFingerPrintInterface

interface AccessUserInterface {
    //val context: Context
    val loginState: LoginViewState
    val databaseApi: DatabaseCRUDInterface
    //fun onLogin(action: ((result: Int) -> Unit)?, email: String, password: String, finger: Boolean = false)
    fun onLogin(action: ((token: String?) -> Unit)?, email: String, password: String, finger: Boolean = false)
    fun onRegisterUser(action: ((result: Boolean) -> Unit)?, vararg userdata: String, )
    fun onRestoreUser(action: ((result: Boolean) -> Unit)?, email: String, password: String)
    fun onFingerPrint(action: ((token: String?) -> Unit)?, email: String)
    //fun getContextFingerPrint(context: Context)
    fun onRemoveUserPassword()
    fun setFingerPrint(value: UserFingerPrint)
}