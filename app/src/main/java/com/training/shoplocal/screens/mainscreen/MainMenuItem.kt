package com.training.shoplocal.screens.mainscreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.training.shoplocal.screens.ScreenItem

sealed class MainMenuItem {
    object None             : MainMenuItem()
    object BrandItem        : MainMenuItem()
    object FavoriteItem     : MainMenuItem()
    object ProductsItem     : MainMenuItem()
    object CancelItem       : MainMenuItem()
}

object MainMenuRouter {
    var current : MainMenuItem by mutableStateOf(MainMenuItem.None)
    fun reset(){
        current = MainMenuItem.None
    }
    fun clickTo(menuitem: MainMenuItem){
        current = menuitem
    }
}