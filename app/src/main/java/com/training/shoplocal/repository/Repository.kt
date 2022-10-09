package com.training.shoplocal.repository

import android.annotation.SuppressLint
import android.content.Context
import com.training.shoplocal.isConnectedNet
import com.training.shoplocal.log
import com.training.shoplocal.loginview.AccessUserInterface
import com.training.shoplocal.loginview.LoginViewState
import com.training.shoplocal.retrofit.ApiManager
import com.training.shoplocal.retrofit.User
import com.training.shoplocal.userfingerprint.UserFingerPrint
import com.training.shoplocal.userfingerprint.UserFingerPrintListener
import retrofit2.Call
import retrofit2.Response
import javax.crypto.Cipher


class Repository: CrudInterface, AccessUserInterface {
    private var userFingerPrint: UserFingerPrint? = null
    val loginState = LoginViewState.getLoginState()

    init {
        loginState.addOnAccessUser(this)
    }

    /*fun getLoginPassword(): String =
        loginState.getPassword()

    fun getLoginEmail(): String =
        loginState.getEmail()*/

    fun getUserFingerPrint(context: Context) {
        userFingerPrint = if (UserFingerPrint.canAuthenticate()) {
            UserFingerPrint(context).apply main@ {
                userFingerPrintListener = object : UserFingerPrintListener {
                    override fun onComplete(cipher: Cipher?) {
                        cipher?.let {
                            onLogin(loginState.getEmail(), this@main.getPassword(it) ?: "", finger = true)
                        }
                    }
                }
                //addPasswordStorage(UserPasswordStorage())
            }
        }
                                else
                                    null

    }

    override fun onLogin(email: String, password: String, finger: Boolean) {
        fun clearLoginPassword(){
            if (!finger)
                loginState.clearPassword()
            loginState.hideProgress()
        }
        if (email.isBlank() || password.isBlank()) {
            clearLoginPassword()
            return
        }
        if (isConnectedNet()) {
            val user = User(
                id          = null,
                firstname   = null,
                lastname    = null,
                phone       = null,
                email       = email,
                password    = password
            )
            ApiManager.loginUser(user, object: retrofit2.Callback<User>{
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    /*
                         0 - No access to the database
                        -1 - User data error
                        -2 - User data reuse
                        -3 - Registration error
                    */
                    val id = response.body()?.id ?: 0
                    //val result = (response.body()?.id ?: 0) > 0
                    if (id > 0) {
                        saveUserPassword(password)
                        user.saveUserData()
                       // log("$id")
                        if (finger)
                            loginState.changePassword(password)
                        loginState.showHomeScreen()
                    } else
                        clearLoginPassword()
                    //action?.invoke(result)
                    //mToast("${response.body()?.id}")
                    //log("${response.body()?.id}")*/
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    clearLoginPassword()
                }
            })
        } else
            clearLoginPassword()



       /* if (!finger && !loggedUser)
            loginState.clearPassword()*/




        /*loginState.changePassword(password)
        loginState.showHomeScreen()*/
    }

    override fun onRegisterUser(action: ((result: Boolean) -> Unit)?, vararg userdata: String) {
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
                //val id = response.body()?.id ?: 0
                val result = (response.body()?.id ?: 0) > 0
                if (result) {
                    saveUserPassword(user.password!!)
                    user.saveUserData()
                }
                action?.invoke(result)
                //mToast("${response.body()?.id}")
                //log("${response.body()?.id}")*/
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                action?.invoke(false)
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

    fun saveUserPassword(value: String){
        userFingerPrint?.putPassword(value)
    }

}