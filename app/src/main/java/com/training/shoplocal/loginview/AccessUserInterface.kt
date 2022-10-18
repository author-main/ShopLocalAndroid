package com.training.shoplocal.loginview

interface AccessUserInterface {
    fun onLogin(action: ((result: Int) -> Unit)?, email: String, password: String, finger: Boolean = false)
    fun onRegisterUser(action: ((result: Boolean) -> Unit)?, vararg userdata: String, )
    fun onRestoreUser(action: ((result: Boolean) -> Unit)?, email: String, password: String)
    fun onFingerPrint(email: String)
}