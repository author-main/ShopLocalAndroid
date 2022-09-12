package com.training.shoplocal.passwordview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@Composable
fun PasswordView(password: String) {
    val state = remember { mutableStateOf(password) }

}