package com.training.shoplocal.screens

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.training.shoplocal.AppShopLocal
import com.training.shoplocal.R
import com.training.shoplocal.TEXT_BOTTOMNAVIGATION


enum class BottomNavigationItemData(@DrawableRes var icon: Int, val iconOn: Int? = null, val text: String) {
    MAIN        (R.drawable.ic_error,     R.drawable.ic_error,    TEXT_BOTTOMNAVIGATION[0]),
    CATALOG     (R.drawable.ic_error,     R.drawable.ic_error,    TEXT_BOTTOMNAVIGATION[1]),
    CART        (R.drawable.ic_error,     R.drawable.ic_error,    TEXT_BOTTOMNAVIGATION[2]),
    PROFILE     (R.drawable.ic_error,     R.drawable.ic_error,    TEXT_BOTTOMNAVIGATION[3])
}

sealed class BottomNavigationItem(val route: String, val data: BottomNavigationItemData){
    object Main:        BottomNavigationItem("route_main", BottomNavigationItemData.MAIN)
    object Catalog:     BottomNavigationItem("route_catalog", BottomNavigationItemData.CATALOG)
    object Cart:        BottomNavigationItem("route_cart", BottomNavigationItemData.CART)
    object Profile:     BottomNavigationItem("route_profile", BottomNavigationItemData.PROFILE)
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val bottomNavigationItems = listOf(
        BottomNavigationItem.Main,
        BottomNavigationItem.Catalog,
        BottomNavigationItem.Cart,
        BottomNavigationItem.Profile
    )
}
