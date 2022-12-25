package com.training.shoplocal.repository

import com.training.shoplocal.classes.Brand
import com.training.shoplocal.classes.Product

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
    suspend fun updateFavorite(id_user: Int, id_product: Int, value: Byte): Boolean
  //  suspend fun getProducts( id: Int, part: Int) : List<Product>
}