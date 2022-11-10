package com.training.shoplocal.repository

import com.training.shoplocal.classes.Category
import com.training.shoplocal.classes.Product

interface DatabaseCRUDInterface {
    fun getCategories(): List<Category>
    fun getPromotionProduct(): List<Product>
    fun getImagesProduct(id: Int): List<String>
}