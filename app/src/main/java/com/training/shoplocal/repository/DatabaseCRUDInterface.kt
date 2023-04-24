package com.training.shoplocal.repository

import com.training.shoplocal.classes.*
import retrofit2.Response

interface DatabaseCRUDInterface {
    //fun loginUser(  user: User, action: (userId: Int) -> Unit = {})
    fun loginUser(mail: String, password: String, action: (user: User) -> Unit = {})
    fun regUser(    user: User, action: (userId: Int) -> Unit = {})
    fun restoreUser(user: User, action: (userId: Int) -> Unit = {})
    fun getCategories()
    fun getProduct( id: Int, action: (product: Product) -> Unit = {})
    fun getProducts( id: Int,
                     part: Int,
                     order: String,
                     action:(products: List<Product>) -> Unit = {})
    fun getFoundProducts(query: String,
                         order: String,
                         portion: Int,
                         uuid: String,
                         userid: Int,
                         action: (products: List<Product>) -> Unit = {})
    fun getBrands( action: (brands: List<Brand>) -> Unit = {})
    fun getReviewProduct(id: Int,
                         action: (reviews: List<Review>) -> Unit = {})
    fun getCategories( action: (categories: List<Category>) -> Unit = {})
    fun getMessages(id: Int, action: (userMessages: List<UserMessage>) -> Unit = {})


    suspend fun updateFavorite(id_user: Int, id_product: Int, value: Byte): Response<Int>
    suspend fun updateUserMessage(id_user: Int, what: Int, id_message: String): Response<Int>
  //  suspend fun getProducts( id: Int, part: Int) : List<Product>
}