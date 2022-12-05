package com.training.shoplocal.repository

import com.training.shoplocal.classes.Brand
import com.training.shoplocal.classes.Product

interface DatabaseCRUDInterface {
    fun getCategories()
    fun getProduct( id: Int, action: (product: Product) -> Unit = {})
    fun getPromoProducts( id: Int, part: Int,action: (products: List<Product>) -> Unit = {})
    fun getBrands( action: (brands: List<Brand>) -> Unit = {})
    suspend fun updateFavorite(id_user: Int, id_product: Int, value: Byte): Boolean

}