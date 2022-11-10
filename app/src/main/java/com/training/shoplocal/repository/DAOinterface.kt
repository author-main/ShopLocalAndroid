package com.training.shoplocal.repository

import com.training.shoplocal.classes.Product

interface DAOinterface {
    fun getProducts(): MutableList<Product>?
}