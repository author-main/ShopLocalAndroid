package com.training.shoplocal

import androidx.compose.runtime.Composable
import com.training.shoplocal.passwordview.PasswordView
import com.training.shoplocal.passwordview.PasswordViewState

@Composable
fun LoginScreen(state: PasswordViewState){
    PasswordView(state)
    state.changePassword("12345")
}