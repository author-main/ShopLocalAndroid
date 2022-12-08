package com.training.shoplocal.screens.mainscreen

import androidx.compose.runtime.collectAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
    //val scope = rememberCoroutineScope()
    val viewModel: RepositoryViewModel = viewModel()
    //val products: MutableList<Product> by viewModel.products.collectAsState()
    //BottomSheet(state) {
    Column(modifier = Modifier.fillMaxWidth()) {
    TopAppBar(backgroundColor = Color.Red) {
        Text(text = "ghjdthrf")
    }


        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(BgScreenDark)
        ) {
            val products: MutableList<Product> by viewModel.products.collectAsState()
            val stateGrid = rememberLazyGridState()
            if (products.isNotEmpty()) {
                LazyVerticalGrid(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 10.dp),
                    columns = GridCells.Adaptive(minSize = 150.dp),
                    state = stateGrid,
                    contentPadding = PaddingValues(10.dp),
                    //horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    items(products, { product -> product.id }) { product ->
                        //log("recomposition Grid")
                        // items(products.size, key = {}) { index ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            CardProduct(product, state = state)
                        }
                    }
                }
            }

            val nextPart = remember {
                derivedStateOf {
                    stateGrid.layoutInfo.visibleItemsInfo.lastOrNull()?.index == stateGrid.layoutInfo.totalItemsCount - 1
                            //&& stateGrid.isScrollInProgress
                            && stateGrid.layoutInfo.viewportEndOffset - stateGrid.layoutInfo.visibleItemsInfo.last().offset.y >= stateGrid.layoutInfo.visibleItemsInfo.last().size.height / 2
                }
            }
            LaunchedEffect(nextPart.value) {
                if (nextPart.value) {
                    viewModel.getNextPortionData()
                }
            }
        }

    }
 }