package com.training.shoplocal.classes

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.training.shoplocal.AppShopLocal

data class User (
    @SerializedName("id")           var id: Int?,
    @SerializedName("email")        val email: String?,
    @SerializedName("firstname")    val firstname: String?,
    @SerializedName("lastname")     val lastname: String?,
    @SerializedName("phone")        val phone: String?,
    @SerializedName("password")     var password: String?
) {
    /*private val sharedPrefs: SharedPreferences =
        AppShopLocal.appContext().getSharedPreferences(FILE_PREFERENCES, Context.MODE_PRIVATE)*/

    fun saveUserData(){
        id          = null
        password    = null
        val gson = Gson()
        val json = gson.toJson(this)
        sharedPrefs.edit().putString("user", json).apply();
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
