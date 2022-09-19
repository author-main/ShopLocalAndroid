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
        delay(1200)
        changeChar.invoke('5')
        delay(2000)
        changeChar.invoke('7')
    }
}

/*
fun ButtonPanel(onClick: ((char:Char) -> Unit)?){
    onClick?.invoke(char)
}
 */