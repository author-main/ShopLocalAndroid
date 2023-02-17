package com.training.shoplocal.screens.appscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.training.shoplocal.classes.Product
import com.training.shoplocal.log
import com.training.shoplocal.ui.theme.BgScreenDark
import com.training.shoplocal.ui.theme.PrimaryDark

@Composable
fun ShowDetailProduct(value: Product){
    val product = remember {
        value
    }
    //val requester: FocusRequester = FocusRequester()
    val interaction = remember { MutableInteractionSource() }
    BoxWithConstraints(modifier = Modifier
        .fillMaxSize()
        //       .focusRequester(requester)
        .clickable(
            interactionSource = interaction,
            indication = null
        ) {}
        .background(BgScreenDark)
    ){
        /*LaunchedEffect(Unit) {
            requester.requestFocus()
        }*/
        val size = this.maxWidth - 32.dp/*with(LocalDensity) {
           32.dp.roundToPx().toFloat()
        }*/
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(all = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ShowProductImages(
                modifier = Modifier
                    .width(size)
                    .height(size)
                    .padding(all = 8.dp)
                , product = product
            ) {
                log("index = $it")
            }
        }
    }
}