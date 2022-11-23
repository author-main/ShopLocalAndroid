package com.training.shoplocal.repository

import com.training.shoplocal.classes.Category
import com.training.shoplocal.classes.Product
import com.training.shoplocal.classes.Products

interface DatabaseCRUDInterface {
    fun getCategories()
    fun getProduct( id: Int, action: (product: Product) -> Unit = {})
}