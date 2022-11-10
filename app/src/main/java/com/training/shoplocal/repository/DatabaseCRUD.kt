package com.training.shoplocal.repository

import com.training.shoplocal.classes.Category
import com.training.shoplocal.classes.Product

class DatabaseCRUD: DatabaseCRUDInterface {
    override fun getPromotionProduct(): List<Product> {
        TODO("Not yet implemented")
    }

    override fun getCategories(): List<Category> {
        TODO("Not yet implemented")
    }

    override fun getImagesProduct(id: Int): List<String> {
        TODO("Not yet implemented")
    }
}