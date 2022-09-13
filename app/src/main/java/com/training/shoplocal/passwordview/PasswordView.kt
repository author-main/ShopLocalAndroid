package com.training.shoplocal.passwordview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable

@Composable
fun PasswordView(state: PasswordViewState) {
    val passwordState = rememberSaveable(saver = PasswordViewState.Saver) { state }
    val chars = passwordState.getPasswordChar()
    val index = 0
}