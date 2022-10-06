package com.training.shoplocal.retrofit

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers

import retrofit2.http.POST




interface IUserApi {
    @Headers("Content-Type: application/json")
    @POST("/api/reg_user")
    fun createUser(@Body user: User): Call<User>
}