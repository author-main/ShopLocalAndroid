package com.training.shoplocal.repository.retrofit.response_classes

import com.google.gson.annotations.SerializedName
import com.training.shoplocal.classes.Product

class Products {
    @SerializedName("products")
    private var items: List<Product>? = null
    fun getItems() =
        items

    fun setItems(list: List<Product>) {
        items = list
    }

    /*fun isEmpty() =
        items?.isEmpty() ?: true*/

    fun isNotEmpty() =
        items?.isNotEmpty() ?: false
}