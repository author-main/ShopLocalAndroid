package com.training.shoplocal.retrofit

import com.google.gson.GsonBuilder
import com.training.shoplocal.SERVER_URL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object ApiManager {
    private const val QUERY_REGUSER     = "reg_user"
    private const val QUERY_LOGINUSER   = "login_user"
    private const val QUERY_RESTOREUSER = "restore_user"
    //private const val url = "http://shop_local.ru"
    //private const val url = "http://192.168.1.10"
        //private const val url = "http://192.168.0.103"

    private var service: IUserApi? = null
    init{
//        val client = OkHttpClient.Builder().build()
        val gson = GsonBuilder()
            .setLenient()
            .create()
        val retrofit = Retrofit.Builder()
            .baseUrl(SERVER_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            //.addConverterFactory(GsonConverterFactory.create())
            .build()
        service = retrofit.create(IUserApi::class.java)
    }

    //fun getRetrofitService() = service


    private fun queryUser(query: String, user: User, callback: retrofit2.Callback<User>) {
        val userCall: retrofit2.Call<User> = service!!.queryUser(user, query)
        userCall.enqueue(callback)
    }

    fun regUser(user: User, callback: retrofit2.Callback<User>){
        queryUser(QUERY_REGUSER, user, callback)
    }

    fun loginUser(user: User, callback: retrofit2.Callback<User>){
        queryUser(QUERY_LOGINUSER, user, callback)
    }

    fun restoreUser(user: User, callback: retrofit2.Callback<User>){
        queryUser(QUERY_RESTOREUSER, user, callback)
    }




   /* fun createUser(user: User, callback: retrofit2.Callback<User>) {
        val userCall: retrofit2.Call<User> = service!!.accessUser(user, "reg_user")
        userCall.enqueue(callback)
    }

    fun restoreUser(user: User, callback: retrofit2.Callback<User>) {
        val userCall: retrofit2.Call<User> = service!!.accessUser(user, "restore_user")
        userCall.enqueue(callback)
    }

    fun loginUser(user: User, callback: retrofit2.Callback<User>) {
        val userCall: retrofit2.Call<User> = service!!.accessUser(user, "login_user")
        userCall.enqueue(callback)
    }*/

  /*  fun loginUser(user: User, callback: retrofit2.Callback<User>) {
        val userCall: retrofit2.Call<User> = service!!.createUser(user)
        userCall.enqueue(callback)
    }*/


}