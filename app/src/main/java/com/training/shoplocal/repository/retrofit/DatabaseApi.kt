package com.training.shoplocal.repository.retrofit

import com.google.gson.GsonBuilder
import com.training.shoplocal.classes.*
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatabaseApi @Inject constructor(private val retrofitService: DatabaseApiInterface){
        private val QUERY_REGUSER   = "reg_user"
        private val QUERY_LOGINUSER = "login_user"
        private val QUERY_RESTOREUSER = "restore_user"

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
            } catch (_: Exception) {}
        }

        fun getProducts(
            token: String,
            part: Int,
            order: String,
            callback: retrofit2.Callback<List<Product>>
        ) {
            try {
                com.training.shoplocal.log(order)
                val call: retrofit2.Call<List<Product>> = retrofitService.getProducts(token, part, order)
                call.enqueue(callback)
            } catch (_: Exception) {
            }
        }

        fun getFoundProducts(
            query: String,
            order: String,
            portion: Int,
            uuid: String,
            token: String,
            callback: retrofit2.Callback<List<Product>>
        ) {
            try {
                val call: retrofit2.Call<List<Product>> =
                    retrofitService.getFoundProducts(query, order, portion, uuid, token)
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

        suspend fun updateFavorite(token: String, id_product: Int, value: Byte): Response<Int> {
            return retrofitService.updateFavorite(token, id_product, value)
        }

        suspend fun updateUserMessage(token: String, what: Int, id_message: String): Response<Int> {
            return retrofitService.updateUserMessage(token, what, id_message)
        }

        fun getBrands(callback: retrofit2.Callback<List<Brand>>) {
            try {
                val call: retrofit2.Call<List<Brand>> = retrofitService.getBrands()
                call.enqueue(callback)
            } catch (_: Exception) {}
        }

        fun getCategories(callback: retrofit2.Callback<List<Category>>) {
            try {
                val call: retrofit2.Call<List<Category>> = retrofitService.getCategories()
                call.enqueue(callback)
            } catch (_: Exception) {}
        }

        fun getMessages(token: String,
                        requestNumberUnread: Int,
                        callback: retrofit2.Callback<List<UserMessage>>) {
            try {
                val call: retrofit2.Call<List<UserMessage>> = retrofitService.getMessages(token, requestNumberUnread)
                call.enqueue(callback)
            } catch (_: Exception) {}
        }
    }