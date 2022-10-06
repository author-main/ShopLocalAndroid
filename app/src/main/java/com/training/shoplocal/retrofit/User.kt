package com.training.shoplocal.retrofit

import com.google.gson.annotations.SerializedName


data class User (
    @SerializedName("id")           val id: Int?,
    @SerializedName("email")        val email: String?,
    @SerializedName("firstname")    val firstname: String?,
    @SerializedName("lastname")     val lastname: String?,
    @SerializedName("phone")        val phone: String?,
    @SerializedName("password")     val password: String?
)


/*class User {
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
}*/
