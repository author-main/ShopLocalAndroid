package com.training.shoplocal.repository

import com.training.shoplocal.classes.Product

interface DatabaseCRUDInterface {
    fun getPromotionProduct(): List<Product>
}