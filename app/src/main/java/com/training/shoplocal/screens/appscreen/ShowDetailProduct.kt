package com.training.shoplocal.screens.appscreen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.training.shoplocal.classes.Product

@Composable
fun ShowDetailProduct(modifier: Modifier, value: Product){
    val product = remember {
        value
    }
}