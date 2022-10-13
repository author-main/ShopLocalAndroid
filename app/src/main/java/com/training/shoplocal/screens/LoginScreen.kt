package com.training.shoplocal.screens


import com.training.shoplocal.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester.Companion.createRefs
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import com.training.shoplocal.buttonpanel.ButtonPasswordPanel
import com.training.shoplocal.buttonpanel.ButtonUserAccessPanel
import com.training.shoplocal.dialogs.ShowMessage
import com.training.shoplocal.loginview.LoginView
import com.training.shoplocal.loginview.LoginViewState
import com.training.shoplocal.ui.theme.TextLightGray
import com.training.shoplocal.ui.theme.TextOrange
import com.training.shoplocal.viewmodel.RepositoryViewModel


@Composable
fun HeaderView(modifier: Modifier){
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
//        Spacer(modifier = Modifier.height(32.dp))
        Image(
            modifier = Modifier
                .size(170.dp, 200.dp),
            imageVector = ImageVector.vectorResource(R.drawable.ic_local_shop),
            contentDescription = null
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(id = R.string.text_auth),
            color = TextLightGray
        )
    }
}

@Composable
fun BodyView(state: LoginViewState, modifier: Modifier){
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        LoginView(state)
            //Spacer(modifier = Modifier.height(8.dp))
        //ButtonPasswordPanel(state::changeChar)
        ButtonPasswordPanel(state)
    }
}

@Composable
fun FooterView(state: LoginViewState, modifier: Modifier){
    Box(modifier) {
        ButtonUserAccessPanel(state)
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginScreen(state: LoginViewState){
    val viewModel: RepositoryViewModel = viewModel()
    val dataSnackbar: Pair<String, Boolean> by viewModel.snackbarData.collectAsState()
    ConstraintLayout {
        val (header, body, footer) = createRefs()
        HeaderView(modifier=Modifier.constrainAs(header){
            top.linkTo(parent.top, margin = 32.dp)
            centerHorizontallyTo(parent)
        })
        BodyView(state, modifier=Modifier.constrainAs(body){
            top.linkTo(header.bottom)
            bottom.linkTo(footer.top)
            centerHorizontallyTo(parent)
        })
        FooterView(state, modifier=Modifier.constrainAs(footer){
            bottom.linkTo(parent.bottom, margin = 16.dp)
            centerHorizontallyTo(parent)
        })
    }
    if (dataSnackbar.second)
        ShowMessage(message = dataSnackbar.first, viewModel = viewModel)
}
