package com.training.shoplocal.repository.retrofit

import com.google.gson.GsonBuilder
import com.training.shoplocal.SERVER_URL
import com.training.shoplocal.classes.Brand
import com.training.shoplocal.classes.Product
import com.training.shoplocal.classes.User
import com.training.shoplocal.log
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


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

    fun getPromoProducts(id: Int, callback: retrofit2.Callback<List<Product>>){
        try {
            val call: retrofit2.Call<List<Product>> = service!!.getPromoProducts(id)
            call.enqueue(callback)
        } catch(e: Exception){
            //log(e.message ?: "error")
        }
    }


    suspend fun updateFavorite(id_user: Int, id_product: Int, value: Byte): Boolean{
        var result = false
        try {
            val response = service!!.updateFavorite(id_user, id_product, value)
            response.body()?.let{body ->
                result = body == 1
            }
        } catch(e: Exception){
        }
        return result
    }

    fun getBrands(callback: retrofit2.Callback<List<Brand>>){
        try {
            val call: retrofit2.Call<List<Brand>> = service!!.getBrands()
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