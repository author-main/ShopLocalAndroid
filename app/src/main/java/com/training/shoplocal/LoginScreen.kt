package com.training.shoplocal

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.training.shoplocal.buttonpanel.ButtonPanel
import com.training.shoplocal.passwordview.PasswordView
import com.training.shoplocal.passwordview.PasswordViewState
import kotlinx.coroutines.delay

@Composable
fun LoginScreen(state: PasswordViewState){
    Box(contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            PasswordView(state)
            Spacer(modifier = Modifier.height(24.dp))
            ButtonPanel(state::changeChar)
        }

    }
  /*  LaunchedEffect(true) {
        delay(1000)
        state.changePassword("1")
        delay(1000)
        state.changePassword("12")
        delay(500)
        state.changePassword("123")
        delay(1000)
        state.changePassword("12")
    }*/
}