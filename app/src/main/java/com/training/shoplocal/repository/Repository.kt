package com.training.shoplocal.repository

import android.content.Context
import com.training.shoplocal.log
import com.training.shoplocal.userfingerprint.UserFingerPrint
import com.training.shoplocal.userfingerprint.UserPasswordStorage
import com.training.shoplocal.loginview.AccessUserInterface
import com.training.shoplocal.loginview.LoginViewState
import com.training.shoplocal.retrofit.ApiManager
import com.training.shoplocal.retrofit.User
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

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

    override fun onRegisterUser(vararg userdata: String) {
        //val service = getRetrofitService()
        val user = User().apply {
            firstName   = userdata[0]
            lastName    = userdata[1]
            phone       = userdata[2]
            email       = userdata[3]
            setPassword(userdata[4])
        }
        ApiManager.createUser(user, object: retrofit2.Callback<User>{
            override fun onResponse(call: Call<User>, response: Response<User>) {
                log("${response.body()?.phone}")
            }

            override fun onFailure(call: Call<User>, t: Throwable) {

            }
        })
    }

    override fun onRestoreUser(email: String) {
        loginState.setEmail(email, true)
        //log(email)
    }

    override fun onFingerPrint(email: String) {
        userFingerPrint?.authenticate()
    }
}