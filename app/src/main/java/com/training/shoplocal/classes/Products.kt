package com.training.shoplocal.classes

import com.google.gson.annotations.SerializedName

class Products {
    @SerializedName("list")
    private var items: List<Product>? = null
    fun getItems() =
        items

    fun setItems(list: List<Product>) {
        items = list
    }

    fun isEmpty() =
        items?.isEmpty() ?: true

    fun isNotEmpty() =
        items?.isNotEmpty() ?: true
}