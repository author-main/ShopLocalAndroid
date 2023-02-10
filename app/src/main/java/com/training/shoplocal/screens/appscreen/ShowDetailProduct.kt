package com.training.shoplocal.screens.appscreen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.training.shoplocal.classes.Product

@Composable
fun ShowDetailProduct(value: Product){
    val product = remember {
        value
    }
}