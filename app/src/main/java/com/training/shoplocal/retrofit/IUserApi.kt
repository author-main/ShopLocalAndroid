package com.training.shoplocal.retrofit

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers

import retrofit2.http.POST
import retrofit2.http.Path


interface IUserApi {
    @Headers("Content-Type: application/json")
    @POST("/api/{php}")//reg_user")
    fun queryUser(@Body user: User, @Path("php") php: String): Call<User>
}