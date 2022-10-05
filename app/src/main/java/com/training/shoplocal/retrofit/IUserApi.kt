package com.training.shoplocal.retrofit

import retrofit2.Call
import retrofit2.http.Body

import retrofit2.http.POST




interface IUserApi {
    @POST("/users")
    fun createUser(@Body user: User): Call<User>
}