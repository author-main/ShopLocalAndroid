package com.training.shoplocal.passwordview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable

@Composable
fun PasswordView(password: String) {
    val state = rememberSaveable(saver = PasswordViewState.Saver) { PasswordViewState.getPasswordState(password) }
}