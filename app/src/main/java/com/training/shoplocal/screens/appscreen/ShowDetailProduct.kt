package com.training.shoplocal.screens.appscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.focus.focusRequester
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
    Box(modifier = Modifier
        .fillMaxSize()
 //       .focusRequester(requester)
        .background(BgScreenDark)
    ){
        /*LaunchedEffect(Unit) {
            requester.requestFocus()
        }*/
    }
}