package com.training.shoplocal.screens.remember

import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.training.shoplocal.classes.Product


class ProductsState(initList: List<Product>) {
    val products = mutableStateOf(initList)
    fun updateProducts(list: List<Product>) {
        products.value = list.toList()
    }
}

@Composable
fun rememberProductsState(initList: List<Product>): ProductsState = remember {
    ProductsState(initList)
}