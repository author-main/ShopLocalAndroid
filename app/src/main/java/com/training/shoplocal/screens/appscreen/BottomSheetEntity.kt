package com.training.shoplocal.screens.appscreen

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.training.shoplocal.R
import com.training.shoplocal.log
import com.training.shoplocal.screens.*
import com.training.shoplocal.ui.theme.PrimaryDark
import com.training.shoplocal.ui.theme.TextFieldBg
import com.training.shoplocal.ui.theme.TextFieldFont
import com.training.shoplocal.viewmodel.RepositoryViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
private fun BottomSheetItem(@DrawableRes id: Int, text: String, divider: Boolean = true, action: ()->Unit){
    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable(onClick = {
            action()
        })
        .height(48.dp)
        .background(color = PrimaryDark)
        .padding(start = 15.dp), verticalAlignment = Alignment.CenterVertically){
        Icon(painterResource(id = id), contentDescription = null, tint = TextFieldFont)
        Text(modifier = Modifier.padding(start = 16.dp), text = text, color = TextFieldFont)
    }
    if (divider) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .padding(start = 49.dp)
                .background(TextFieldBg)
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheet(state: ModalBottomSheetState, content: @Composable ()-> Unit = {}){
        ModalBottomSheetLayout(
            sheetContent = {
                BottomSheetContent(state)
            },
            sheetState = state,
            sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            sheetBackgroundColor = PrimaryDark,
            scrimColor = Color.Black.copy(alpha = 0.3f),//Color.Transparent,
            sheetElevation = 3.dp
        ) {
            content()
        }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun BottomSheetContent(state: ModalBottomSheetState){
    val viewModel: RepositoryViewModel = viewModel()
    val scope = rememberCoroutineScope()
    val selectedProduct = viewModel.selectedProduct.collectAsState()
    fun hide(){
        scope.launch {
            state.hide()
        }
    }

    val textItems = stringArrayResource(id = R.array.bottomsheet_product_items)
    BottomSheetItem(R.drawable.ic_addcart, textItems[0]){
        hide()
    }
    BottomSheetItem(R.drawable.ic_brend_bs, textItems[1]){
        val product = viewModel.selectedProduct.value
        val brand = product.brand ?: 0
        if (brand > 0) {

        }
        hide()
    }
    BottomSheetItem(R.drawable.ic_favorite_bs, run {
        if (selectedProduct.value.favorite > 0)
            textItems[5]
        else
            textItems[2]
    }){
        val product = viewModel.selectedProduct.value
        val favorite: Byte =
            if (product.favorite > 0)
                0
            else
                1
        viewModel.setProductFavorite(product.copy(favorite = favorite))
        hide()
    }
    BottomSheetItem(R.drawable.ic_product_bs, textItems[3]){
        val product = viewModel.selectedProduct.value
        val category = product.category ?: 0
        if (category > 0) {
            log("category $category")
        }
        hide()
    }
    BottomSheetItem(R.drawable.ic_cancel_bs, textItems[4], divider = false){
       hide()
    }
}