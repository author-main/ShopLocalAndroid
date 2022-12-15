package com.training.shoplocal.screens.appscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.training.shoplocal.classes.Product
import com.training.shoplocal.classes.downloader.ImageLinkDownloader
import com.training.shoplocal.classes.searcher.SearchQueryStorage
import com.training.shoplocal.classes.searcher.SearchQueryStorageInterface
import com.training.shoplocal.log
import com.training.shoplocal.screens.*
import com.training.shoplocal.screens.appscreen.BottomNavigationBar
import com.training.shoplocal.screens.appscreen.BottomNavigationItem
import com.training.shoplocal.screens.appscreen.Navigation
import com.training.shoplocal.screens.mainscreen.MainScreen
import com.training.shoplocal.ui.theme.PrimaryDark
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
                         // log("clear downloader")
                          when (ScreenRouter.current) {
                              ScreenItem.MainScreen -> {
                                  ImageLinkDownloader.cancel()
                                  MainScreen(state)
                              }
                              ScreenItem.CatalogScreen -> {
                                  ImageLinkDownloader.cancel()
                                  CatalogScreen()
                              }
                              ScreenItem.CartScreen -> {
                                  ImageLinkDownloader.cancel()
                                  CartScreen()
                              }
                              ScreenItem.ProfileScreen -> {
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

