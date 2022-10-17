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
import com.training.shoplocal.ScreenItem

@Composable
fun MainScreen(){
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) {
        Box(modifier = Modifier.padding(it)) {
            Navigation(navController)
        }
    }
}

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(navController, startDestination = BottomNavigationItem.Main.route) {
        composable(BottomNavigationItem.Main.route) {

        }
        composable(BottomNavigationItem.Catalog.route) {

        }
        composable(BottomNavigationItem.Cart.route) {

        }
        composable(BottomNavigationItem.Profile.route) {

        }
    }
}