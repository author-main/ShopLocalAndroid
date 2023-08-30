package com.training.shoplocal.screens.remember

import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.viewmodel.compose.viewModel
import com.training.shoplocal.classes.Product
import com.training.shoplocal.log
import com.training.shoplocal.screens.ScreenRouter
import com.training.shoplocal.viewmodel.RepositoryViewModel


private val MapScreenProducts = mutableMapOf<ScreenRouter.KEYSCREEN, List<Product>>()

class ProductsState(initList: List<Product>) {
    val products = mutableStateOf(initList)
    fun updateProducts(list: List<Product>) {
        products.value = list.toList()
    }
}

@Composable
fun rememberProductsState(key: ScreenRouter.KEYSCREEN, initValue: List<Product>): ProductsState {
    val productsState = remember{
        ProductsState(MapScreenProducts[key] ?: initValue)
    }
    log("${productsState.products.value.size}")
    DisposableEffect(Unit) {
        onDispose {
            //val lastProducts        = productsState.products
            MapScreenProducts[key]  = productsState.products.value
        }
    }
    return productsState

}