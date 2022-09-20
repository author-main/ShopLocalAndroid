package com.training.shoplocal

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.training.shoplocal.buttonpanel.ButtonPasswordPanel
import com.training.shoplocal.buttonpanel.ButtonUserAccessPanel
import com.training.shoplocal.loginview.LoginView
import com.training.shoplocal.loginview.LoginViewState
import com.training.shoplocal.ui.theme.TextLightGray
import com.training.shoplocal.ui.theme.TextOrange

@Composable
fun LoginScreen(state: LoginViewState){
    Box(contentAlignment = Alignment.TopCenter){
        Text(
            text = stringResource(id = R.string.text_auth),
            color = TextLightGray,
            //fontSize = 13.sp
        )
    }
    Box(contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            LoginView(state)
            Spacer(modifier = Modifier.height(24.dp))
            ButtonPasswordPanel(state::changeChar)
        }
    }
    Box(contentAlignment = Alignment.BottomCenter) {
        ButtonUserAccessPanel()
    }

    /*Box(contentAlignment = Alignment.BottomCenter){
        ButtonPanel(state::changeChar)
    }*/

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