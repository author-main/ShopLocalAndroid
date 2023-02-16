package com.training.shoplocal.screens.appscreen

import android.text.Layout.Alignment
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.training.shoplocal.classes.Product
import com.training.shoplocal.log

@Composable
fun ShowProductImages(modifier: Modifier, product: Product){
    val product = remember {
        product
    }
    Box(modifier = modifier
        .background(Color.Red)
    ){
        Text (text = "Checkit")
    }
    log("Product ${product.name}")
}