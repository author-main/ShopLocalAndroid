package com.training.shoplocal.repository

import android.content.Context
import com.google.gson.Gson
import com.training.shoplocal.isConnectedNet
import com.training.shoplocal.log
import com.training.shoplocal.loginview.AccessUserInterface
import com.training.shoplocal.loginview.LoginViewState
import com.training.shoplocal.mToast
import com.training.shoplocal.retrofit.ApiManager
import com.training.shoplocal.retrofit.User
import com.training.shoplocal.userfingerprint.UserFingerPrint
import com.training.shoplocal.userfingerprint.UserPasswordStorage
import retrofit2.Call
import retrofit2.Response


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

        val user = User(
                    id          = null,
                    firstname   = userdata[0],
                    lastname    = userdata[1],
                    phone       = userdata[2],
                    email       = userdata[3],
                    password    = userdata[4]
            )

       /* val gson = Gson()
        val json = gson.toJson(user)
        log(json)*/


        if (isConnectedNet())
        ApiManager.createUser(user, object: retrofit2.Callback<User>{
            override fun onResponse(call: Call<User>, response: Response<User>) {
                /*
                     0 - No access to the database
                    -1 - User data error
                    -2 - User data reuse
                    -3 - Registration error
                */
                mToast("${response.body()?.id}")
                log("${response.body()?.id}")
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                log(t.message?:"error")
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