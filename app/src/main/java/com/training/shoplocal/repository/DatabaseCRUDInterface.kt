package com.training.shoplocal.repository

import com.training.shoplocal.classes.Brand
import com.training.shoplocal.classes.Product

interface DatabaseCRUDInterface {
    fun getCategories()
    fun getProduct( id: Int, action: (product: Product) -> Unit = {})
    fun getPromoProducts( action: (products: List<Product>) -> Unit = {})
    fun getBrands( action: (brands: List<Brand>) -> Unit = {})
    suspend fun updateFavorite(id: Int, value: Byte): Boolean

}