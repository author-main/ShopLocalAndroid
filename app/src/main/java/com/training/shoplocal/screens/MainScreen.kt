package com.training.shoplocal.screens

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.training.shoplocal.ScreenItem

@Composable
fun MainScreen(){
    val bottomNavigationItems = listOf(
        BottomNavigationItem.Main,
        BottomNavigationItem.Catalog,
        BottomNavigationItem.Cart,
        BottomNavigationItem.Profile
    )
    val navController = rememberNavController()
}