package com.training.shoplocal.repository.retrofit

import com.training.shoplocal.classes.Product
import com.training.shoplocal.classes.Products
import com.training.shoplocal.classes.User
import retrofit2.Call
import retrofit2.http.*


interface DatabaseApiInterface {
    @Headers("Content-Type: application/json")
    @POST("/api/{script}")//reg_user")
    fun queryUser(@Body user: User, @Path("script") phpScript: String): Call<User>

    @GET("/api/get_product")///get_promo_products")
    fun getPromotionProducts(@Query("id") id: Int): Call<Product>
}