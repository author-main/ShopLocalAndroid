package com.training.shoplocal

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import com.training.shoplocal.passwordview.PasswordView
import com.training.shoplocal.passwordview.PasswordViewState

@Composable
fun LoginScreen(state: PasswordViewState){
    state.changePassword("12")
    Box(contentAlignment = Alignment.Center) {
        PasswordView(state)
    }

}