package com.training.shoplocal

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

sealed class ScreenItem {
    object None : ScreenItem()
    object HomeScreen : ScreenItem()
}

object ScreenRouter {
    var current : ScreenItem by mutableStateOf(ScreenItem.None)
    fun reset(){
        current = ScreenItem.None
    }
    fun navigateTo(destination: ScreenItem){
        current = destination
    }
}