package com.training.shoplocal.repository

import com.training.shoplocal.classes.Product

interface CrudInterface {
    fun getProducts(): MutableList<Product>?
}