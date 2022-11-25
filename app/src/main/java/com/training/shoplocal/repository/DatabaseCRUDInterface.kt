package com.training.shoplocal.repository

import com.training.shoplocal.classes.Product

interface DatabaseCRUDInterface {
    fun getCategories()
    fun getProduct( id: Int, action: (product: Product) -> Unit = {})
    fun getPromoProducts( action: (products: List<Product>) -> Unit = {})
}