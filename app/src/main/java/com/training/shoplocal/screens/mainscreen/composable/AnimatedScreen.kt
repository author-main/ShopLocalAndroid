package com.training.shoplocal.screens.mainscreen.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.training.shoplocal.ui.theme.PrimaryDark
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AnimatedScreen(open: MutableState<Boolean>, content: @Composable () -> Unit){
    val openPopup = remember{ mutableStateOf(false) }
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        launch {
            openPopup.value = true
        }
    }


    fun onDissmiss(){
        coroutineScope.launch {
            openPopup.value = false
            delay(300)
            open.value = false
        }
    }

    Popup(
        alignment = Alignment.Center,
        onDismissRequest = {
           onDissmiss()
        },
        properties = PopupProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = false,
            focusable = true
        )
    ) {

        AnimatedVisibility(
            visible = openPopup.value,
            enter = slideInHorizontally(
                animationSpec = tween(500)
            ) { fullWidth ->
                fullWidth * 2
            },
            exit = slideOutHorizontally(
                animationSpec = tween(300) { fullWidth ->
                    -fullWidth * 2
                }
            )
        ) {
            Box(modifier = Modifier
                .fillMaxSize()
                .background(PrimaryDark)) {
                content()
            }
        }
    }
}