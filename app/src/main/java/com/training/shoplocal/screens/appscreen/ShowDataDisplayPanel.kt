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
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun ShowDataDisplayPanel(){
    val dataDisplay by remember { mutableStateOf(OrderDisplay.getInstance(), policy= neverEqualPolicy())}


    Box(modifier = Modifier.fillMaxWidth()
        .height(48.dp)
        .background(MaterialTheme.colors.primary)
    ){
        Text(text = dataDisplay.getFavorite().toString())
    }
}