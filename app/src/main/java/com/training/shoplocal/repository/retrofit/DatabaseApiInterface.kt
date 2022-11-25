package com.training.shoplocal.repository.retrofit

import com.google.gson.annotations.SerializedName
import com.training.shoplocal.classes.Product
import com.training.shoplocal.classes.User
import com.training.shoplocal.repository.retrofit.response_classes.Products
import retrofit2.Call
import retrofit2.http.*


interface DatabaseApiInterface {
    @Headers("Content-Type: application/json")
    @POST("/api/{script}")//reg_user")
    fun queryUser(@Body user: User, @Path("script") phpScript: String): Call<User>

    @GET("/api/get_product")///get_promo_products")
    fun getProduct(@Query("id") id: Int): Call<Product>

    @GET("/api/get_promo_products")
    fun getPromoProducts(): Call<Products>
}