package com.training.shoplocal.repository.retrofit

import com.google.gson.GsonBuilder
import com.training.shoplocal.classes.*
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Inject
import javax.inject.Singleton

/*@Singleton
class RetrofitService @Inject constructor(){
        private val service: DatabaseApiInterface
        init{
            val gson = GsonBuilder()
                .setLenient()
                .create()
            val retrofit = Retrofit.Builder()
                .baseUrl(SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
            service = retrofit.create<DatabaseApiInterface>()
            //service = retrofit.create(DatabaseApiInterface::class.java)
        }
        fun getService() = service
}

@Singleton
class DatabaseApi @Inject constructor(private val retrofitApi: RetrofitService){
    //companion object {
        private val retrofitService = retrofitApi.getService()*/
@Singleton
class DatabaseApi @Inject constructor(private val retrofitService: DatabaseApiInterface){
        private val QUERY_REGUSER   = "reg_user"
        private val QUERY_LOGINUSER = "login_user"
        private val QUERY_RESTOREUSER = "restore_user"
/*    init{
        val gson = GsonBuilder()
            .setLenient()
            .create()
        val retrofit = Retrofit.Builder()
            .baseUrl(SERVER_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            //.addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofitInstance = retrofit.create(DatabaseApiInterface::class.java)
    }

    //fun getRetrofitretrofitInstance() = retrofitInstance
*/

        private fun queryUser(query: String, user: User, callback: retrofit2.Callback<User>) {
            val userCall: retrofit2.Call<User> = retrofitService.queryUser(user, query)
            userCall.enqueue(callback)
        }

        fun regUser(user: User, callback: retrofit2.Callback<User>) {
            queryUser(QUERY_REGUSER, user, callback)
        }

        fun loginUser(mail: String, password: String, callback: retrofit2.Callback<User>) {
            val user = User.getEmptyInstance()
            user.email      = mail
            user.password   = password
            queryUser(QUERY_LOGINUSER, user, callback)
        }

        fun restoreUser(user: User, callback: retrofit2.Callback<User>) {
            queryUser(QUERY_RESTOREUSER, user, callback)
        }

        fun getProduct(id: Int, callback: retrofit2.Callback<Product>) {
            try {
                val call: retrofit2.Call<Product> = retrofitService.getProduct(id)
                call.enqueue(callback)
            } catch (e: Exception) {
                //log(e.message ?: "error")
            }
        }

        fun getProducts(
            id: Int,
            part: Int,
            order: String,
            callback: retrofit2.Callback<List<Product>>
        ) {
            try {
                val call: retrofit2.Call<List<Product>> = retrofitService.getProducts(id, part, order)
                call.enqueue(callback)
            } catch (e: Exception) {
                //log(e.message ?: "error")
            }
        }


        fun getFoundProducts(
            query: String,
            order: String,
            portion: Int,
            uuid: String,
            userid: Int,
            callback: retrofit2.Callback<List<Product>>
        ) {
            try {
                val call: retrofit2.Call<List<Product>> =
                    retrofitService.getFoundProducts(query, order, portion, uuid, userid)
                call.enqueue(callback)
            } catch (_: Exception) {
            }
        }

        fun getReviewProduct(
            id: Int,
            callback: retrofit2.Callback<List<Review>>
        ) {
            try {
                val call: retrofit2.Call<List<Review>> = retrofitService.getReviewProduct(id)
                call.enqueue(callback)
            } catch (_: Exception) {
            }
        }


        /*   suspend fun getProducts(id: Int, part: Int) : List<Product> {
        var list = emptyList<Product>()
        try {
            val response = retrofitInstance!!.getProducts(id, part)
            response.body()?.let{body ->
                list = body
            }
        } catch(e: Exception){
            //log(e.message ?: "error")
        }
        return list
    }*/

        suspend fun updateFavorite(id_user: Int, id_product: Int, value: Byte): Response<Int> {
            /*try {
            retrofitInstance!!.updateFavorite(id_user, id_product, value)
        } catch(_: Exception){
        }*/
            return retrofitService.updateFavorite(id_user, id_product, value)
        }

        suspend fun updateUserMessage(id_user: Int, what: Int, id_message: String): Response<Int> {
            return retrofitService.updateUserMessage(id_user, what, id_message)
        }

        fun getBrands(callback: retrofit2.Callback<List<Brand>>) {
            try {
                val call: retrofit2.Call<List<Brand>> = retrofitService.getBrands()
                call.enqueue(callback)
            } catch (e: Exception) {
                // log(e.message ?: "error")
            }
        }

        fun getCategories(callback: retrofit2.Callback<List<Category>>) {
            try {
                val call: retrofit2.Call<List<Category>> = retrofitService.getCategories()
                call.enqueue(callback)
            } catch (e: Exception) {
                // log(e.message ?: "error")
            }
        }

        fun getMessages(id: Int, callback: retrofit2.Callback<List<UserMessage>>) {
            try {
                val call: retrofit2.Call<List<UserMessage>> = retrofitService.getMessages(id)
                call.enqueue(callback)
            } catch (e: Exception) {
                // log(e.message ?: "error")
            }
        }
    }











   /* fun createUser(user: User, callback: retrofit2.Callback<User>) {
        val userCall: retrofit2.Call<User> = retrofitInstance!!.accessUser(user, "reg_user")
        userCall.enqueue(callback)
    }

    fun restoreUser(user: User, callback: retrofit2.Callback<User>) {
        val userCall: retrofit2.Call<User> = retrofitInstance!!.accessUser(user, "restore_user")
        userCall.enqueue(callback)
    }

    fun loginUser(user: User, callback: retrofit2.Callback<User>) {
        val userCall: retrofit2.Call<User> = retrofitInstance!!.accessUser(user, "login_user")
        userCall.enqueue(callback)
    }*/

  /*  fun loginUser(user: User, callback: retrofit2.Callback<User>) {
        val userCall: retrofit2.Call<User> = retrofitInstance!!.createUser(user)
        userCall.enqueue(callback)
    }*/




//}