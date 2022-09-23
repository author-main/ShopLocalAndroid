package com.training.shoplocal

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester.Companion.createRefs
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.training.shoplocal.buttonpanel.ButtonPasswordPanel
import com.training.shoplocal.buttonpanel.ButtonUserAccessPanel
import com.training.shoplocal.loginview.LoginView
import com.training.shoplocal.loginview.LoginViewState
import com.training.shoplocal.ui.theme.TextLightGray
import com.training.shoplocal.ui.theme.TextOrange


@Composable
fun HeaderView(modifier: Modifier){
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
//        Spacer(modifier = Modifier.height(32.dp))
        Image(
            modifier = Modifier
                .size(150.dp, 200.dp),
            imageVector = ImageVector.vectorResource(R.drawable.ic_local_shop),
            contentDescription = null
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(id = R.string.text_auth),
            color = TextLightGray,
        )
    }
}

@Composable
fun BodyView(state: LoginViewState, modifier: Modifier){
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        LoginView(state)
        Spacer(modifier = Modifier.height(8.dp))
        ButtonPasswordPanel(state::changeChar)
    }
}

@Composable
fun FooterView(state: LoginViewState, modifier: Modifier){
    Box(modifier) {
        ButtonUserAccessPanel()
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginScreen(state: LoginViewState){
    ConstraintLayout {
        val (header, body, footer) = createRefs()
     //   Box(contentAlignment = Alignment.TopCenter) {
//            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                HeaderView(modifier=Modifier.constrainAs(header){
                    top.linkTo(parent.top, margin = 32.dp)
                    centerHorizontallyTo(parent)
                })
               // BodyView(state)
                //FooterView(state)

        BodyView(state, modifier=Modifier.constrainAs(body){
            top.linkTo(header.bottom)
            bottom.linkTo(footer.top)
            centerHorizontallyTo(parent)
        })

        FooterView(state, modifier=Modifier.constrainAs(footer){
            bottom.linkTo(parent.bottom, margin = 16.dp)
            centerHorizontallyTo(parent)
        })
//            }
       // }
    }
}
