package com.training.shoplocal.screens

import androidx.annotation.DrawableRes
import com.training.shoplocal.AppShopLocal
import com.training.shoplocal.R
import com.training.shoplocal.TEXT_BOTTOMNAVIGATION


enum class BottomNavigationItemData(@DrawableRes var icon: Int, val iconOn: Int? = null, val text: String) {
    MAIN        (R.drawable.ic_error,     R.drawable.ic_error,    TEXT_BOTTOMNAVIGATION[0]),
    CATALOG     (R.drawable.ic_error,     R.drawable.ic_error,    TEXT_BOTTOMNAVIGATION[1]),
    CART        (R.drawable.ic_error,     R.drawable.ic_error,    TEXT_BOTTOMNAVIGATION[2]),
    PROFILE     (R.drawable.ic_error,     R.drawable.ic_error,    TEXT_BOTTOMNAVIGATION[3])
}

sealed class BottomNavigationItem(val data: BottomNavigationItemData){
    object Main:        BottomNavigationItem(BottomNavigationItemData.MAIN)
    object Catalog:     BottomNavigationItem(BottomNavigationItemData.CATALOG)
    object Cart:        BottomNavigationItem(BottomNavigationItemData.CART)
    object Profile:     BottomNavigationItem(BottomNavigationItemData.PROFILE)
}