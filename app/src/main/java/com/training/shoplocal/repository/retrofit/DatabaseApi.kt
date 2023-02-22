package com.training.shoplocal.repository.retrofit

import com.google.gson.GsonBuilder
import com.training.shoplocal.classes.*
import com.training.shoplocal.log
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


object DatabaseApi {
    private const val QUERY_REGUSER     = "reg_user"
    private const val QUERY_LOGINUSER   = "login_user"
    private const val QUERY_RESTOREUSER = "restore_user"
    //private const val url = "http://shop_local.ru"
    //private const val url = "http://192.168.1.10"
        //private const val url = "http://192.168.0.103"

    private var service: DatabaseApiInterface? = null
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
        service = retrofit.create(DatabaseApiInterface::class.java)
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

    fun getProduct(id: Int, callback: retrofit2.Callback<Product>){
        try {
            val call: retrofit2.Call<Product> = service!!.getProduct(id)
            call.enqueue(callback)
        } catch(e: Exception){
            //log(e.message ?: "error")
        }
    }

    fun getProducts(id: Int, part: Int, order: String, callback: retrofit2.Callback<List<Product>>){
        try {
            val call: retrofit2.Call<List<Product>> = service!!.getProducts(id, part, order)
            call.enqueue(callback)
        } catch(e: Exception){
            //log(e.message ?: "error")
        }
    }


    fun getFoundProducts(query: String,
                         order: String,
                         portion: Int,
                         uuid: String,
                         userid: Int,
                         callback: retrofit2.Callback<List<Product>>){
        try {
            val call: retrofit2.Call<List<Product>> = service!!.getFoundProducts(query, order, portion, uuid, userid)
            call.enqueue(callback)
        } catch(_: Exception){}
    }

    fun getReviewProduct(id: Int,
                         callback: retrofit2.Callback<List<Review>>
                        )
    {
        try {
            val call: retrofit2.Call<List<Review>> = service!!.getReviewProduct(id)
            call.enqueue(callback)
        } catch(_: Exception){}
    }



 /*   suspend fun getProducts(id: Int, part: Int) : List<Product> {
        var list = emptyList<Product>()
        try {
            val response = service!!.getProducts(id, part)
            response.body()?.let{body ->
                list = body
            }
        } catch(e: Exception){
            //log(e.message ?: "error")
        }
        return list
    }*/

    suspend fun updateFavorite(id_user: Int, id_product: Int, value: Byte){
        try {
            service!!.updateFavorite(id_user, id_product, value)
        } catch(_: Exception){
        }
    }

    fun getBrands(callback: retrofit2.Callback<List<Brand>>){
        try {
            val call: retrofit2.Call<List<Brand>> = service!!.getBrands()
            call.enqueue(callback)
        } catch(e: Exception){
           // log(e.message ?: "error")
        }
    }

    fun getCategories(callback: retrofit2.Callback<List<Category>>){
        try {
            val call: retrofit2.Call<List<Category>> = service!!.getCategories()
            call.enqueue(callback)
        } catch(e: Exception){
            // log(e.message ?: "error")
        }
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