package com.training.shoplocal.retrofit

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object ApiManager {
    //private val apiManager: ApiManager? = null
    private const val url = "http://shop_local.ru"
    private var service: IUserApi? = null
    //private val apiManager: ApiManager? = null
    init{
        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        service = retrofit.create(IUserApi::class.java)
    }

    //fun getRetrofitService() = service

    fun createUse(user: User, callback: Callback<User>) {
        val userCall: Call<User> = service!!.createUser(user)
        userCall.enqueue(callback)
    }


}