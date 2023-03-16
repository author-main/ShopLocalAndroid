package com.training.shoplocal.repository.retrofit

import com.training.shoplocal.classes.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*


interface DatabaseApiInterface {
    @Headers("Content-Type: application/json")
    @POST("/api/{script}")//reg_user")
    fun queryUser(@Body user: User, @Path("script") phpScript: String): Call<User>

    @GET("/api/get_product")///get_promo_products")
    fun getProduct(@Query("id") id: Int): Call<Product>

    @GET("/api/get_products")
    fun getProducts(@Query("id") id: Int,
                    @Query("part") part: Int,
                    @Query("order") order: String): Call<List<Product>>

    @GET("/api/get_found_products")
    fun getFoundProducts(@Query("query") query: String,
                         @Query("order") order: String,
                         @Query("portion") portion: Int,
                         @Query("uuid") uuid: String,
                         @Query("userid") userid: Int): Call<List<Product>>


    @GET("/api/get_reviews_product")
    fun getReviewProduct(@Query("id") id: Int): Call<List<Review>>


    /*@GET("/api/get_promo_products")
    suspend fun getProducts(@Query("id") id: Int, @Query("part") part: Int): Response<List<Product>>*/


    @GET("/api/get_brands")
    fun getBrands(): Call<List<Brand>>

    @GET("/api/get_categories")
    fun getCategories(): Call<List<Category>>

    @GET("/api/get_messages")
    fun getMessages(@Query("id") id:Int): Call<List<UserMessage>>

    /**
     *  Используем response без обработки результата запроса.
     *  Обновился статус избранного или нет, не важно.
     */
    @FormUrlEncoded
    @POST("/api/update_favorite")
    suspend fun updateFavorite(@Field("id_user") id_user: Int, @Field("id_product") id_product: Int, @Field("favorite") value: Byte): Response<Int>

    @FormUrlEncoded
    @POST("/api/update_message")
    suspend fun updateUserMessage(@Field("id_user") id_user: Int, @Field("what") what: Int, @Field("id_message") id_message: Int): Response<Int>

}