package com.training.shoplocal.repository

import com.training.shoplocal.classes.*
import retrofit2.Response

interface DatabaseCRUDInterface {
    //fun loginUser(  user: User, action: (userId: Int) -> Unit = {})
    fun loginUser(  user: User, action: (userData: User) -> Unit = {})
    fun regUser(    user: User, action: (userId: Int) -> Unit = {})
    fun restoreUser(user: User, action: (userId: Int) -> Unit = {})
    fun getCategories()
    fun getProduct( id: Int, action: (product: Product) -> Unit = {})
    fun getProducts( token: String,
                     part: Int,
                     order: String,
                     action:(products: List<Product>) -> Unit = {})
    fun getFoundProducts(query: String,
                         order: String,
                         portion: Int,
                         uuid: String,
                         token: String,
                         action: (products: List<Product>) -> Unit = {})
    fun getBrands( action: (brands: List<Brand>) -> Unit = {})
    fun getReviewProduct(token: String,
                         action: (reviews: List<Review>) -> Unit = {})
    fun getCategories( action: (categories: List<Category>) -> Unit = {})
    fun getMessages(token: String, action: (userMessages: List<UserMessage>) -> Unit = {})


    suspend fun updateFavorite(token: String, id_product: Int, value: Byte): Response<Int>
    suspend fun updateUserMessage(token: String, what: Int, id_message: String): Response<Int>
  //  suspend fun getProducts( id: Int, part: Int) : List<Product>
}