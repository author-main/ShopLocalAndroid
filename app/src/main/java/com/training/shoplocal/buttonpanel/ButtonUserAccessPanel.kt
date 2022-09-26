package com.training.shoplocal.buttonpanel

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.training.shoplocal.DialogItem
import com.training.shoplocal.DialogRouter
import com.training.shoplocal.R
import com.training.shoplocal.log
import com.training.shoplocal.loginview.LoginViewState
import com.training.shoplocal.ui.theme.TextLightGray
import com.training.shoplocal.ui.theme.TextOrange

@Composable
fun ButtonUserAccessPanel(state: LoginViewState){
    val password = state.getPassword()
     Row(//modifier = Modifier.padding(vertical = 16.dp),
         verticalAlignment = Alignment.CenterVertically
     ){
         OutlinedButton(
            onClick = {
                state.setPressedButtons(true)
                DialogRouter.navigateTo(DialogItem.RestoreUserDialog)
            },

            border = null,
            shape = CircleShape,
            contentPadding = PaddingValues(8.dp)
        ) {
            Text(
                text = stringResource(id = R.string.btn_reg),
                color = TextOrange,
                fontWeight = FontWeight.Light,
                fontSize = 13.sp
            )
        }

        Text(
            modifier = Modifier
                .padding(horizontal = 4.dp),
            text = "|",
            color = TextLightGray,
            fontSize = 14.sp,
        )

        OutlinedButton(
            onClick = {
                state.setPressedButtons(true)
                state.forceRecomposition()
                DialogRouter.navigateTo(DialogItem.RestoreUserDialog)
                //DialogRouter.navigateTo(DialogItem.RestoreUserDialog)
            },
            border = null,
            shape = CircleShape,
            contentPadding = PaddingValues(8.dp)
        ) {
            Text(
                text = stringResource(id = R.string.btn_rest),
                color = TextOrange,
                fontWeight = FontWeight.Light,
                fontSize = 13.sp
            )
        }
      }

}