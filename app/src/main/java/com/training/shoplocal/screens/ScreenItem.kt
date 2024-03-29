package com.training.shoplocal.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

sealed class ScreenItem(val key: ScreenRouter.KEYSCREEN) {
    data object LoginScreen      : ScreenItem(ScreenRouter.KEYSCREEN.LOGIN)
    data object MainScreen       : ScreenItem(ScreenRouter.KEYSCREEN.MAIN)
    data object CatalogScreen    : ScreenItem(ScreenRouter.KEYSCREEN.CATALOG)
    data object CartScreen       : ScreenItem(ScreenRouter.KEYSCREEN.CART)
    data object ProfileScreen    : ScreenItem(ScreenRouter.KEYSCREEN.PROFILE)
}

object ScreenRouter {
    enum class KEYSCREEN(val value: Int) {LOGIN(0), MAIN(1), CATALOG(2), CART(3), PROFILE(4)}
    var current : ScreenItem by mutableStateOf(ScreenItem.LoginScreen)
    fun reset(){
        current = ScreenItem.LoginScreen
    }
    fun navigateTo(destination: ScreenItem){
        if (current != destination)
            current = destination
    }
    fun getKeyValue(): Int =
        current.key.value
}