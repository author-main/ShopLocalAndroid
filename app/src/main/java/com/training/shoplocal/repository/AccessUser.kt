package com.training.shoplocal.repository

import android.app.Activity
import android.content.Context
import androidx.activity.ComponentActivity
import com.training.shoplocal.fingerPrintCanAuthenticate
import com.training.shoplocal.isConnectedNet
import com.training.shoplocal.loginview.LoginViewState
import com.training.shoplocal.classes.User
import com.training.shoplocal.dagger.ActivityMainScope
import com.training.shoplocal.log
import com.training.shoplocal.userfingerprint.UserFingerPrint
import com.training.shoplocal.userfingerprint.UserFingerPrintListener
import com.training.shoplocal.validateMail
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import javax.crypto.Cipher
import javax.inject.Inject

class AccessUser(
    override val loginState: LoginViewState,
    override val databaseApi: DatabaseCRUDInterface
): AccessUserInterface {
    private var actionLogin: ((token: String?) -> Unit)? = null
    private var userFingerPrint: UserFingerPrint? = null

    override fun setFingerPrint(value: UserFingerPrint) {
        userFingerPrint = value
        setParamUserFingerPrint()
    }
    var processLogin = false

    override fun onLogin(
        action: ((token: String?) -> Unit)?,
        email: String,
        password: String,
        finger: Boolean
    ) {

        if (processLogin) return
        processLogin = true

        fun clearLoginPassword(){
                CoroutineScope(Dispatchers.Main).launch {
                    delay(400)
                    this@AccessUser.loginState.clearPassword()
                }
            this.loginState.hideProgress()
            action?.invoke(null)
        }

        if (email.isBlank() || password.isBlank() || !validateMail(email)) {
            clearLoginPassword()
            return
        }

        if (isConnectedNet()) {
           databaseApi.loginUser(email, password) {user ->
                    if (user.validUser()) {
                        saveUserPassword(password)
                        if (User.getUserData() == null)
                            user.saveUserData()
                        //action?.invoke(user.id!!)
                        action?.invoke(user.token!!)
                        if (finger)
                            loginState.changePassword(password)
                        loginState.clearPassword()
                    } else
                        clearLoginPassword()
                processLogin = false
            }
        } else {
            clearLoginPassword()
            processLogin = false
        }
    }

    override fun onRegisterUser(action: ((result: Boolean) -> Unit)?, vararg userdata: String) {
        val user = User(
            id          = null,
            firstname   = userdata[0],
            lastname    = userdata[1],
            phone       = userdata[2],
            email       = userdata[3],
            password    = userdata[4],
            token       = null
        )
        if (isConnectedNet()) {
            databaseApi.regUser(user) {id ->
                val result = id > 0
                if (result) {
                    saveUserPassword(user.password!!)
                    user.saveUserData()
                }
                action?.invoke(result)
            }
         }
    }

    override fun onRestoreUser(action: ((result: Boolean) -> Unit)?, email: String, password: String) {

        val user = User(
            id          = null,
            firstname   = null,
            lastname    = null,
            phone       = null,
            email       = email,
            password    = password,
            token       = null
        )
        if (isConnectedNet())
            databaseApi.regUser(user) {id ->
                action?.invoke(id > 0)
            }
    }

    override fun onFingerPrint(action: ((token: String?) -> Unit)?, email: String) {
        if (validateMail(email)) {
            actionLogin = action
            userFingerPrint?.authenticate()
        }
    }

    private fun saveUserPassword(value: String){
        userFingerPrint?.putPassword(value)
    }

    private fun setParamUserFingerPrint(): UserFingerPrint? {
        return if (fingerPrintCanAuthenticate()) {
            userFingerPrint?.apply main@ {
                    userFingerPrintListener = object : UserFingerPrintListener {
                        override fun onComplete(cipher: Cipher?) {
                            cipher?.let {
                                this@AccessUser.loginState.showProgress()
                                onLogin(action = actionLogin, this@AccessUser.loginState.getEmail(), this@main.getPassword(it) ?: "", finger = true)
                            }
                        }
                    }
                }
        }
        else
            null
    }

    override fun onRemoveUserPassword() {
        userFingerPrint?.removePassword()
    }
}