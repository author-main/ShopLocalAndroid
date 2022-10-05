package com.training.shoplocal.retrofit

class User {
    private var id: Int = 0
    var email: String = ""
    var firstName: String = ""
    var lastName : String = ""
    var phone : String = ""
    private var password : String = ""

    fun getPassword() = password
    fun setPassword(value: String) {
        password = value
    }
}
