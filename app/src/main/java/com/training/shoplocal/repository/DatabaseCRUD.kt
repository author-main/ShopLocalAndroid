package com.training.shoplocal.repository

import com.training.shoplocal.classes.*
import com.training.shoplocal.log
import com.training.shoplocal.repository.retrofit.DatabaseApi
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatabaseCRUD @Inject constructor(private val databaseApi: DatabaseApi): DatabaseCRUDInterface {

    //override fun loginUser(user: User, action: (userId: Int) -> Unit) {
    override fun loginUser(user: User, action: (userData: User) -> Unit) {
        databaseApi.loginUser(user, object: retrofit2.Callback<User>{
            override fun onResponse(call: Call<User>, response: Response<User>) {
                //log(response.body())
                /*val id = response.body()?.id ?: 0
                if (id > 0)
                    action(id)*/
                action(response.body() ?: User.getEmptyUser())

            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                action(User.getEmptyUser())
            }
        })
    }

    override fun regUser(user: User, action: (userId: Int) -> Unit) {
        databaseApi.regUser(user, object: retrofit2.Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                val id = response.body()?.id ?: 0
                if (id > 0)
                    action(id)
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                action(-1)
            }
        }
        )
    }

    override fun restoreUser(user: User, action: (userId: Int) -> Unit) {
        databaseApi.restoreUser(user,
            object : retrofit2.Callback<User>{
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    val id = response.body()?.id ?: 0
                    if (id > 0)
                        action(id)
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    action(-1)
                }
            }
        )

    }

    override fun getCategories(action: (categories: List<Category>) -> Unit) {
        databaseApi.getCategories(object: retrofit2.Callback<List<Category>>{
            override fun onResponse(call: Call<List<Category>>, response: Response<List<Category>>) {
                response.body()?.let {
                    action.invoke(
                        it.ifEmpty { listOf() }
                    )
                }
            }
            override fun onFailure(call: Call<List<Category>>, t: Throwable) {
                action.invoke(listOf<Category>())
            }
        })
    }

    override fun getBrands(action: (brands: List<Brand>) -> Unit) {
        databaseApi.getBrands(object: retrofit2.Callback<List<Brand>>{
            override fun onResponse(call: Call<List<Brand>>, response: Response<List<Brand>>) {
                response.body()?.let {
                    action.invoke(
                        it.ifEmpty { listOf() }
                    )
                }
            }
            override fun onFailure(call: Call<List<Brand>>, t: Throwable) {
                action.invoke(listOf<Brand>())
            }
        })
    }

    override fun getProducts(token: String, part: Int, order: String, action: (products: List<Product>) -> Unit) {
        databaseApi.getProducts(id, part, order, object: retrofit2.Callback<List<Product>>{
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {

                    response.body()?.let {
                       // if (it.isNotEmpty()) {
                            action(it)
                       // }
                    }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                //log(t.message ?: "ошибка")
                //log("ошибка")
                action.invoke(listOf<Product>())
            }
        })
    }


    override fun getFoundProducts(
        query: String,
        order: String,
        portion: Int,
        uuid: String,
        token: String,
        action: (products: List<Product>) -> Unit
    ) {
        databaseApi.getFoundProducts(query,
            order,
            portion,
            uuid,
            userid,
            object: retrofit2.Callback<List<Product>>{
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                    //log("response  " + response.body()?.toString())
                    response.body()?.let {
                     //   if (it.isNotEmpty()) {
                            //log("found ok")
                            action.invoke(it)
                     //   }
                    }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
               // log("found failure")
                action.invoke(listOf<Product>())
            }
        })
    }

    override fun getProduct(id: Int, action: (product: Product) -> Unit ){
       // log("getPromotionProduct")
        databaseApi.getProduct(id, object: retrofit2.Callback<Product> {

            override fun onResponse(call: Call<Product>, response: Response<Product>) {
                response.body()?.let {
                        action.invoke(it)
                }
            }

            override fun onFailure(call: Call<Product>, t: Throwable) {

            }

        })
    }

    override fun getCategories() {

    }

    override suspend fun updateFavorite(token: String, id_product: Int, value: Byte): Response<Int> {
        return databaseApi.updateFavorite(token, id_product, value)
    }

    override suspend fun updateUserMessage(id_user: Int, what: Int, id_message: String): Response<Int> {
        return databaseApi.updateUserMessage(id_user, what, id_message)
    }

    override fun getReviewProduct(
        id: Int,
        action: (reviews: List<Review>) -> Unit
    ) {
        databaseApi.getReviewProduct(id, object: retrofit2.Callback<List<Review>>{
            override fun onResponse(call: Call<List<Review>>, response: Response<List<Review>>) {
                response.body()?.let {
                    action(it)
                }
            }

            override fun onFailure(call: Call<List<Review>>, t: Throwable) {
                action(listOf<Review>())
            }
        })
    }    /* override suspend fun getProducts(id: Int, part: Int): List<Product> {
        return DatabaseApi.getProducts(id, part)
    }*/

    override fun getMessages(
        id: Int,
        action: (userMessages: List<UserMessage>) -> Unit
    ) {
        databaseApi.getMessages(id, object: retrofit2.Callback<List<UserMessage>> {

            override fun onResponse(call: Call<List<UserMessage>>, response: Response<List<UserMessage>>) {
                response.body()?.let {
                    action(it)
                }
            }

            override fun onFailure(call: Call<List<UserMessage>>, t: Throwable) {
                    action(listOf<UserMessage>())
            }

        })
    }
}