package com.training.shoplocal.repository

import android.app.Activity
import android.content.Context
import androidx.activity.ComponentActivity
import com.training.shoplocal.fingerPrintCanAuthenticate
import com.training.shoplocal.isConnectedNet
import com.training.shoplocal.loginview.LoginViewState
import com.training.shoplocal.classes.User
import com.training.shoplocal.dagger.ActivityMainScope
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
    //override val context: Context,
    override val loginState: LoginViewState,
    override val databaseApi: DatabaseCRUDInterface
): AccessUserInterface {
    private var actionLogin: ((result: Int) -> Unit)? = null
    private var userFingerPrint: UserFingerPrint? = null
    /*@Inject
    lateinit var databaseApi: DatabaseCRUDInterface*/

    override fun setFingerPrint(value: UserFingerPrint) {
        userFingerPrint = value
        setParamUserFingerPrint()
    }

    //private lateinit var loginState: LoginViewState
    //private var userFingerPrint: UserFingerPrint? = null



    //private val userFingerPrint = getUserFingerPrint()

    /*override fun getContextFingerPrint(context: Context) {
        getUserFingerPrint(context as FragmentActivity)
    }*/

    /*  fun updateViewWhen(loginState: LoginViewState){
          this.loginState = loginState
      }*/

    override fun onLogin(
        action: ((result: Int) -> Unit)?,
        email: String,
        password: String,
        finger: Boolean
    ) {
        fun clearLoginPassword(){
        //    if (!finger)
                CoroutineScope(Dispatchers.Main).launch {
                    delay(400)
                    this@AccessUser.loginState.clearPassword()
                }
            this.loginState.hideProgress()
            action?.invoke(-1)
        }

        if (email.isBlank() || password.isBlank() || !validateMail(email)) {
            clearLoginPassword()
            return
        }

        if (isConnectedNet()) {

            val user = User.getUserData() ?: User.getEmptyUser()
            val noSavedUser = user.email == null
            user.email       = email
            user.password    = password
            databaseApi.loginUser(user) {id ->

                    if (id > 0) {
                        saveUserPassword(password)
                        if (noSavedUser)
                            user.saveUserData()
                        action?.invoke(id)
                        if (finger)
                        //    this@AccessUser.
                            loginState.changePassword(password)
                        //this@AccessUser.
                        loginState.clearPassword()
                    } else
                        clearLoginPassword()
            }
            /*databaseApi.loginUser(user, object: retrofit2.Callback<User>{
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    /*
                         0 - No access to the database
                        -1 - User data error
                        -2 - User data reuse
                        -3 - Registration error
                    */
                    val id = response.body()?.id ?: 0
                  //  log("id = $id")
                    if (id > 0) {
                        //user.id = id
                        saveUserPassword(password)
                        user.saveUserData()
                        action?.invoke(id)
                        // log("$id")
                        if (finger)
                            this@AccessUser.loginState.changePassword(password)
                        this@AccessUser.loginState.clearPassword()
                    } else {
                      //  log ("error login")
                        clearLoginPassword()
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    clearLoginPassword()
                }
            })*/
        } else
            clearLoginPassword()
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

        /* val gson = Gson()
         val json = gson.toJson(user)
         log(json)*/


        if (isConnectedNet()) {
            databaseApi.regUser(user) {id ->
                val result = id > 0
                if (result) {
                    saveUserPassword(user.password!!)
                    user.saveUserData()
                }
                action?.invoke(result)
            }
            /*databaseApi.regUser(user, object: retrofit2.Callback<User>{
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
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    action?.invoke(false)
                }
            })*/
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

           /* databaseApi.restoreUser(user, object: retrofit2.Callback<User>{
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    action?.invoke((response.body()?.id ?: 0) > 0)
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    action?.invoke(false)
                }
            })*/
        /*  val gson = Gson()
          val json = gson.toJson(user)
          log(json)*/
    }

    override fun onFingerPrint(action: ((result: Int) -> Unit)?, email: String) {
        if (validateMail(email)) {
            actionLogin = action
            userFingerPrint?.authenticate()
        }
    }

    private fun saveUserPassword(value: String){
        userFingerPrint?.putPassword(value)
    }

  /*  private fun getUserFingerPrint(context: Context) {
        userFingerPrint = if (UserFingerPrint.canAuthenticate()) {
            UserFingerPrint(context).apply main@ {
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
    }*/

    private fun setParamUserFingerPrint(): UserFingerPrint? {
        //lateinit var userFingerPrint: UserFingerPrint
        return if (fingerPrintCanAuthenticate()) {
            //UserFingerPrint(context as FragmentActivity)
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