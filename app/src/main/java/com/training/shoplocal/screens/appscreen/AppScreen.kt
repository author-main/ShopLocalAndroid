package com.training.shoplocal.screens.appscreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.training.shoplocal.classes.fodisplay.OrderDisplay
import com.training.shoplocal.classes.downloader.ImageLinkDownloader
import com.training.shoplocal.screens.*
import com.training.shoplocal.screens.mainscreen.MainScreen
import com.training.shoplocal.viewmodel.RepositoryViewModel

//val LocalSearchStorage = staticCompositionLocalOf<SearchQueryStorageInterface?> { error("")}





@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AppScreen(){
      val viewModel: RepositoryViewModel = viewModel()
     // val searchStorage = SearchQueryStorage.getInstance()
          //CompositionLocalProvider(LocalSearchStorage provides viewModel.selectedProduct.collectAsState().value) {
         // CompositionLocalProvider(LocalSearchStorage provides searchStorage) {
          val state = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
          BottomSheet(state) {
              val navController = rememberNavController()
              Scaffold(bottomBar = {
                  val hiddenNavigation = viewModel.hiddenBottomNavigation.collectAsState()
                  if (!hiddenNavigation.value)
                      BottomNavigationBar(navController)

              }
              ) {
                  Box(
                      modifier = Modifier
                          .padding(it)
                  ) {
                          Navigation(navController)
                          when (ScreenRouter.current) {
                              ScreenItem.MainScreen -> {
                                        OrderDisplay.getInstance().setScreenData(ScreenItem.MainScreen)
                                        ImageLinkDownloader.cancel()
                                        MainScreen(state)
                                //        log("mainscreen")


                              }
                              ScreenItem.CatalogScreen -> {
                                  OrderDisplay.getInstance().setScreenData(ScreenItem.CatalogScreen)
                                  ImageLinkDownloader.cancel()
                                  CatalogScreen()
                              }
                              ScreenItem.CartScreen -> {
                                  OrderDisplay.getInstance().setScreenData(ScreenItem.CartScreen)
                                  ImageLinkDownloader.cancel()
                                  CartScreen()
                              }
                              ScreenItem.ProfileScreen -> {
                                  OrderDisplay.getInstance().setScreenData(ScreenItem.ProfileScreen)
                                  ImageLinkDownloader.cancel()
                                  ProfileScreen()
                              }
                              else -> {}
                          }


                  }
              }
          }
   //   }
}

