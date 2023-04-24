package com.training.shoplocal.classes

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.training.shoplocal.AppShopLocal

data class User (
    @SerializedName("id")           var id: Int?,
    @SerializedName("email")        var email: String?,
    @SerializedName("firstname")    val firstname: String?,
    @SerializedName("lastname")     val lastname: String?,
    @SerializedName("phone")        val phone: String?,
    @SerializedName("password")     var password: String?,
    @SerializedName("token")        var token:    String?
) {
    /*private val sharedPrefs: SharedPreferences =
        AppShopLocal.appContext().getSharedPreferences(FILE_PREFERENCES, Context.MODE_PRIVATE)*/


    fun isNotEmpty() = token?.isNotEmpty() ?: false

    fun saveUserData(){
        id          = null
        password    = null
        token       = null
        val gson = Gson()
        val json = gson.toJson(this)
        sharedPrefs.edit().putString("user", json).apply()
    }

    companion object {
        private val sharedPrefs: SharedPreferences =
            AppShopLocal.appContext().getSharedPreferences(FILE_PREFERENCES, Context.MODE_PRIVATE)
        fun getUserData(): User? {
            return try {
                val gson = Gson()
                val json: String? = sharedPrefs.getString("user", null)
                gson.fromJson(json, User::class.java)
            } catch (e: Exception) {
                null
            }
        }

        fun getEmptyUser(): User {
            return User(
                id          = null,
                firstname   = null,
                lastname    = null,
                phone       = null,
                email       = null,
                password    = null,
                token       = null
            )
        }



    }
}


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
