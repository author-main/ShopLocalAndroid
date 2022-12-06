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
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
//fun MainScreen(state: ModalBottomSheetState, scope: CoroutineScope){
fun MainScreen(state: ModalBottomSheetState){
    //var part by remember {mutableStateOf(1)}
    //val state = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()
    val viewModel: RepositoryViewModel = viewModel()
    //val products: MutableList<Product> by viewModel.products.collectAsState()
    //BottomSheet(state) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(BgScreenDark)
        ) {

            val products: MutableList<Product> by viewModel.products.collectAsState()
            val stateGrid = rememberLazyGridState()
            val uploadNextPart = remember {
                derivedStateOf {
                    stateGrid.layoutInfo.visibleItemsInfo.lastOrNull()?.index == stateGrid.layoutInfo.totalItemsCount - 1
                        //&& stateGrid.isScrollInProgress
                            && stateGrid.layoutInfo.viewportEndOffset - stateGrid.layoutInfo.visibleItemsInfo.last().offset.y >= stateGrid.layoutInfo.visibleItemsInfo.last().size.height
                }
            }
            //LaunchedEffect(uploadNextPart.value) {
                if (uploadNextPart.value) {
                    log("next part")
                    viewModel.getNextPortionData()
                }
           // }

            if (products.isNotEmpty()) {
                log("recomposition grid")
                LazyVerticalGrid(modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 10.dp),
                    columns = GridCells.Adaptive(minSize = 150.dp),
                    state = stateGrid,
                    contentPadding = PaddingValues(10.dp),
                    //horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    //log("recomposition Grid")







                    items(products.size) { index ->
                        //log("index ${index+1}, product count = ${products.size}")
                        Row(modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly) {
                            CardProduct(products[index], state = state)
                        }

                        /*val nextPart = remember {
                            derivedStateOf {
                                stateGrid.layoutInfo.visibleItemsInfo.lastOrNull()?.index == stateGrid.layoutInfo.totalItemsCount - 1
                                        //&& stateGrid.isScrollInProgress
                                        && stateGrid.layoutInfo.viewportEndOffset - stateGrid.layoutInfo.visibleItemsInfo.last().offset.y >= stateGrid.layoutInfo.visibleItemsInfo.last().size.height
                            }
                        }*/

                       // LaunchedEffect(nextPart.value) {

                            /*if (nextPart.value) {
                                log("next part")
                                viewModel.getNextPortionData()*/

                              /* scope.launch {
                                    stateGrid.layoutInfo.visibleItemsInfo.lastOrNull()?.let {
                                        stateGrid.animateScrollToItem(it.index)
                                    }
                                }*/
                          //  }
                       // }
                    }
                }
            }
        }
    //}
 }