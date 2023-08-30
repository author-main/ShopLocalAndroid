package com.training.shoplocal

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

sealed class DialogItem {
    object None : DialogItem()
    object RegUserDialog     : DialogItem()
    object RestoreUserDialog : DialogItem()
}

object DialogRouter {
    var current : DialogItem by mutableStateOf(DialogItem.None)
    fun reset(){
        current = DialogItem.None
    }

    fun navigateTo(destination: DialogItem){
        current = destination
    }
}