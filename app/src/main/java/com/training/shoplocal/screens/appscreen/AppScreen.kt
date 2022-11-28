package com.training.shoplocal.screens.appscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.training.shoplocal.classes.downloader.ImageLinkDownloader
import com.training.shoplocal.screens.*
import com.training.shoplocal.screens.appscreen.BottomNavigationBar
import com.training.shoplocal.screens.appscreen.BottomNavigationItem
import com.training.shoplocal.screens.appscreen.Navigation
import com.training.shoplocal.screens.mainscreen.MainScreen
import com.training.shoplocal.ui.theme.PrimaryDark

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AppScreen(){
    val state = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    BottomSheet(state) {
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
                ImageLinkDownloader.cancel()
                when (ScreenRouter.current) {
                    ScreenItem.MainScreen -> {
                        MainScreen(state)
                    }
                    ScreenItem.CatalogScreen -> {
                        CatalogScreen()
                    }
                    ScreenItem.CartScreen -> {
                        CartScreen()
                    }
                    ScreenItem.ProfileScreen -> {
                        ProfileScreen()
                    }
                    else -> {}
                }

            }
        }
    }
}

