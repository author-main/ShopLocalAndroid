package com.training.shoplocal.repository

import com.training.shoplocal.classes.Product
import com.training.shoplocal.loginview.AccessUserInterface

interface DAOinterface {
    //var accesUser: AccessUser?
    fun getProducts(): MutableList<Product>?
}