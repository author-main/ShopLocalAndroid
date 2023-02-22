package com.training.shoplocal.repository

import com.training.shoplocal.classes.Brand
import com.training.shoplocal.classes.Category
import com.training.shoplocal.classes.Product
import com.training.shoplocal.classes.Review

interface DatabaseCRUDInterface {
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
    suspend fun updateFavorite(id_user: Int, id_product: Int, value: Byte)
  //  suspend fun getProducts( id: Int, part: Int) : List<Product>
}