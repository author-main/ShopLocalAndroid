package com.training.shoplocal.repository.retrofit

import com.google.gson.annotations.SerializedName
import com.training.shoplocal.classes.Brand
import com.training.shoplocal.classes.Product
import com.training.shoplocal.classes.User
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*


interface DatabaseApiInterface {
    @Headers("Content-Type: application/json")
    @POST("/api/{script}")//reg_user")
    fun queryUser(@Body user: User, @Path("script") phpScript: String): Call<User>

    @GET("/api/get_product")///get_promo_products")
    fun getProduct(@Query("id") id: Int): Call<Product>

    @GET("/api/get_promo_products")
    fun getPromoProducts(@Query("id") id: Int, @Query("part") part: Int): Call<List<Product>>

    @GET("/api/get_brands")
    fun getBrands(): Call<List<Brand>>

    /**
     *  Используем response без обработки результата запроса.
     *  Обновился статус избранного или нет, не важно.
     */
    @FormUrlEncoded
    @POST("/api/update_favorite")
    suspend fun updateFavorite(@Field("id_user") id_user: Int, @Field("id_product") id_product: Int, @Field("favorite") value: Byte): Response<Int>
}