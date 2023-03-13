package com.training.shoplocal.screens.mainscreen.composable

import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.training.shoplocal.log
import com.training.shoplocal.viewmodel.RepositoryViewModel

@Composable
fun ShowUserMessages(open: MutableState<Boolean>){
    /**
     * 0 - ОБЫЧНОЕ СООБЩЕНИЕ
     * 1 - СООБЩЕНИЕ О ДОСТАВКЕ
     * 2 - СООБЩЕНИЕ О СКИДКАХ
     * 3 - СООБЩЕНИЕ ПОЗДРАВЛЕНИЕ
     */
    val USER_MESSAGE_NORMAL           = 0
    val USER_MESSAGE_DELIVERY         = 1
    val USER_MESSAGE_DISCOUNT         = 2
    val USER_MESSAGE_GIFT             = 3
    val viewModel: RepositoryViewModel = viewModel()
    val messages = viewModel.userMessages.collectAsState()
    DisposableEffect(Unit) {
        viewModel.getMessages()
        onDispose {
            viewModel.clearMessages()
        }
    }
    AnimatedScreen(open = open) {

    }
}