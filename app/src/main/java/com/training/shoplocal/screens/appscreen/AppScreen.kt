package com.training.shoplocal.screens.appscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.training.shoplocal.screens.ScreenItem
import com.training.shoplocal.screens.ScreenRouter
import com.training.shoplocal.screens.appscreen.BottomNavigationBar
import com.training.shoplocal.screens.appscreen.BottomNavigationItem
import com.training.shoplocal.screens.appscreen.Navigation
import com.training.shoplocal.screens.mainscreen.BottomSheet
import com.training.shoplocal.screens.mainscreen.MainScreen
import com.training.shoplocal.ui.theme.PrimaryDark

@Composable
fun AppScreen(){
    BottomSheet() {
        val navController = rememberNavController()
        Scaffold(bottomBar = {
            BottomNavigationBar(navController)
        }
        ) {
            Box(
                modifier = Modifier
                    .padding(it)
            ) {
                Navigation(navController)
                /*when (ScreenRouter.current) {
                    ScreenItem.MainScreen -> {
                        MainScreen()
                    }
                    else -> {}
                }*/

            }
        }
    }
}

