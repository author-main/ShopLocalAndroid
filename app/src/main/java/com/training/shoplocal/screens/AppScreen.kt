package com.training.shoplocal.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.training.shoplocal.screens.bottomnavigation.BottomNavigationBar
import com.training.shoplocal.screens.bottomnavigation.BottomNavigationItem
import com.training.shoplocal.screens.bottomnavigation.Navigation

@Composable
fun AppScreen(){
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) {
        Box(modifier = Modifier.padding(it)) {
            Navigation(navController)
            when (ScreenRouter.current) {
                ScreenItem.MainScreen -> {
                    MainScreen()
                }
                else -> {}
            }
        }
    }
}

