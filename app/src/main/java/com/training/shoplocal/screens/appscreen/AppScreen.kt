package com.training.shoplocal.screens.appscreen

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.shrinkVertically
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
                  //val visible = MutableTransitionState(true)
                  /*if (!hiddenNavigation.value)
                      BottomNavigationBar(navController)*/


               /*   androidx.compose.animation.AnimatedVisibility(
                      visible = !hiddenNavigation.value,
                      enter = expandVertically(
                          animationSpec = tween(
                              durationMillis = 70,
                              easing = LinearEasing
                          )
                      ),
                      exit =  shrinkVertically  (
                          animationSpec = tween(
                              durationMillis = 70,
                              easing = LinearEasing
                          )
                      )
                  ) {
                      BottomNavigationBar(navController)
                  }*/
                  if (!hiddenNavigation.value)
                    BottomNavigationBar(navController)



              }
              ) {
                  Box(
                      modifier = Modifier
                          .padding(it)
                  ) {
                          Navigation(navController)
                          fun setOrderDisplayScreen(){
                              OrderDisplay.getInstance().setScreen(ScreenRouter.getKeyValue())
                          }
                          when (ScreenRouter.current) {
                              ScreenItem.MainScreen -> {
                                  setOrderDisplayScreen()
                                        //OrderDisplay.getInstance().setScreen(ScreenRouter.getKeyValue())
                                  //viewModel.imageDownloader.cancelAll()
                                        //ImageLinkDownloader.cancel()
                                        MainScreen(state)
                                //        log("mainscreen")


                              }
                              ScreenItem.CatalogScreen -> {
                                  setOrderDisplayScreen()
                                  //OrderDisplay.getInstance().setScreen(ScreenRouter.getKeyValue())
                                  //viewModel.imageDownloader.cancelAll()
                                  //ImageLinkDownloader.cancel()
                                  CatalogScreen()
                              }
                              ScreenItem.CartScreen -> {
                                  setOrderDisplayScreen()
                                  //OrderDisplay.getInstance().setScreen(ScreenRouter.getKeyValue())
                                  //ImageLinkDownloader.cancel()
                                  //viewModel.imageDownloader.cancelAll()
                                  CartScreen()
                              }
                              ScreenItem.ProfileScreen -> {
                                  setOrderDisplayScreen()
                                  //OrderDisplay.getInstance().setScreen(ScreenRouter.getKeyValue())
                                 // viewModel.imageDownloader.cancelAll()
                                  //ImageLinkDownloader.cancel()
                                  ProfileScreen()
                              }
                              else -> {}
                          }


                  }
              }
          }
   //   }
}

