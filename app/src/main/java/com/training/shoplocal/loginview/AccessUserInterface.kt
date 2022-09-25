package com.training.shoplocal.loginview

interface AccessUserInterface {
    fun onLogin(password: String): Boolean
    fun onRegisterUser()
    fun onRestoreUser(email: String)
    fun onFingerPrint(email: String)
}