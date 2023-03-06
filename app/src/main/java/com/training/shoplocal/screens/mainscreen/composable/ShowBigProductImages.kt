package com.training.shoplocal.screens.mainscreen.composable

import androidx.compose.animation.*
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.training.shoplocal.DialogRouter
import com.training.shoplocal.classes.Container
import com.training.shoplocal.classes.Product
import com.training.shoplocal.screens.mainscreen.ImageLink
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ShowBigProductImages(open: MutableState<Boolean>, product: Product, index: Int) {
    val openPopup = remember{ mutableStateOf(false) }
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        launch {
            //delay(500)
            openPopup.value = true
        }
    }


        Popup(
            alignment = Alignment.Center,
            onDismissRequest = {
                coroutineScope.launch {
                    openPopup.value = false
                    delay(500)
                    open.value = false
                }
                },
            properties = PopupProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = false,
                focusable = true
            )
        ) {

                AnimatedVisibility(
                    visible = openPopup.value,
                    enter = scaleIn(
                        animationSpec = tween(500)
                    ),
                    exit = scaleOut(
                        animationSpec = tween(500)
                    )
                ) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = Color.White
                    ) {
                        ShowProductImages(modifier = Modifier.fillMaxSize().padding(8.dp), product = product, reduce = false, startIndex = index) {

                        }
                }

            }
    }
}