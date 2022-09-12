package com.training.shoplocal

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

sealed class ScreenItem {
    //object None : ScreenItem()
    object LoginScreen : ScreenItem()
}

object ScreenRouter {
    var current : ScreenItem by mutableStateOf(ScreenItem.LoginScreen)
    fun reset(){
        current = ScreenItem.LoginScreen
    }
    fun navigateTo(destination: ScreenItem){
        current = destination
    }
}