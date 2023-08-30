package com.training.shoplocal.screens.mainscreen.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.training.shoplocal.ui.theme.PrimaryDark
import com.training.shoplocal.ui.theme.TextFieldFont

@Composable
fun BackButton(modifier: Modifier, onClick: () -> Unit){
    Icon(modifier = modifier
        .padding(end = 8.dp)
        .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null
        ) {
            onClick()
        },
        imageVector = Icons.Filled.ArrowBack,
        contentDescription = null,
        tint = TextFieldFont
    )
}

@Composable
fun TopBar(onClick: () -> Unit = {}, content: @Composable () -> Unit ) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(54.dp)
        .background(PrimaryDark)
        .padding(horizontal = 8.dp)
    ){
        Box(modifier = Modifier.fillMaxHeight(),
            contentAlignment = Alignment.Center
        ) {
            BackButton(modifier = Modifier) {
                onClick()
            }
        }
        Box(modifier = Modifier
            .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            content()
        }
    }
}