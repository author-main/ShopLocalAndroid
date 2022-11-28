package com.training.shoplocal.screens.mainscreen

import androidx.compose.runtime.collectAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.training.shoplocal.classes.Product
import com.training.shoplocal.log
import com.training.shoplocal.screens.appscreen.BottomSheet
import com.training.shoplocal.ui.theme.BgScreenDark
import com.training.shoplocal.ui.theme.PrimaryDark
import com.training.shoplocal.viewmodel.RepositoryViewModel
import kotlinx.coroutines.CoroutineScope


@OptIn(ExperimentalMaterialApi::class)
@Composable
//fun MainScreen(state: ModalBottomSheetState, scope: CoroutineScope){
fun MainScreen(state: ModalBottomSheetState){
    val viewModel: RepositoryViewModel = viewModel()
    val products: MutableList<Product> by viewModel.products.collectAsState()
    Box(modifier = Modifier
        .fillMaxSize()
        .background(BgScreenDark)
    ) {

      if (products.isNotEmpty()) {
          CardProduct(products[0], state = state)
      }
    }
 }