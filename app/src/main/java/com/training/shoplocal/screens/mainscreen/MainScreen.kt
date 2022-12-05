package com.training.shoplocal.screens.mainscreen

import androidx.compose.runtime.collectAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
    //val state = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val viewModel: RepositoryViewModel = viewModel()
    val products: MutableList<Product> by viewModel.products.collectAsState()
    //BottomSheet(state) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(BgScreenDark)
        ) {

            if (products.isNotEmpty()) {
                LazyVerticalGrid(modifier = Modifier.fillMaxSize()
                    .padding(horizontal = 10.dp),
                    columns = GridCells.Adaptive(minSize = 150.dp),
                    state = rememberLazyGridState(),
                    contentPadding = PaddingValues(10.dp),
                    //horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    //log("recomposition Grid")
                    items(products.size) { index ->
                        Row(modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly) {
                            CardProduct(products[index], state = state)
                        }
                    }
                }
            }
        }
    //}
 }