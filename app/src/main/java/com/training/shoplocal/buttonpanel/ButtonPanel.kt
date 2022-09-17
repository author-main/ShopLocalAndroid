package com.training.shoplocal.buttonpanel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.training.shoplocal.passwordview.PasswordViewState
import kotlinx.coroutines.delay

@Composable
fun ButtonPanel(changeChar: (value: Char)-> Unit){
    LaunchedEffect(true) {
        delay(1000)
        changeChar.invoke(' ')
    }
}

/*
fun ButtonPanel(onClick: ((char:Char) -> Unit)?){
    onClick?.invoke(char)
}
 */