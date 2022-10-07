package com.training.shoplocal.loginview

interface AccessUserInterface {
    fun onLogin(email: String, password: String, action:()->Unit = {})
    fun onRegisterUser(action: ((result: Boolean) -> Unit)?, vararg userdata: String, )
    fun onRestoreUser(email: String)
    fun onFingerPrint(email: String)
}