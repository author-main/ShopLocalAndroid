package com.training.shoplocal.screens.appscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.training.shoplocal.classes.OrderDisplay
import com.training.shoplocal.log
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun ShowDataDisplayPanel(){
//    val dataDisplay by remember { mutableStateOf(OrderDisplay.getInstance(), policy= neverEqualPolicy())}
    /*val state = remember {
        OrderDisplay.getInstance()
    }*/

    Box(modifier = Modifier
        .fillMaxWidth()
        .height(48.dp)
        .background(MaterialTheme.colors.primary)
    ){
        if (OrderDisplay.getInstance().state)
            Text(text = "${OrderDisplay.getInstance().getFavorite()}")
        else
            Text(text = "${OrderDisplay.getInstance().getFavorite()}")
    }
}