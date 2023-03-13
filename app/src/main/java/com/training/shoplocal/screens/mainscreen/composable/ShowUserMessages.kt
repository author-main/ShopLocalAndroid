package com.training.shoplocal.screens.mainscreen.composable

import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.training.shoplocal.log
import com.training.shoplocal.viewmodel.RepositoryViewModel

@Composable
fun ShowUserMessages(open: MutableState<Boolean>){
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