package com.training.shoplocal.buttonpanel

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ButtonDefaults
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
import com.training.shoplocal.ui.theme.PrimaryDark
import com.training.shoplocal.ui.theme.SelectedItemBottomNavi
import com.training.shoplocal.ui.theme.TextLightGray
import com.training.shoplocal.ui.theme.TextOrange

@Composable
fun ButtonUserAccessPanel(state: LoginViewState){
    fun lostFocus(){
        state.setPressedButtons(true)
        state.clearPassword()
    }
    /*fun clearPassword(){
        val password = state.getPassword()
        if (password.isEmpty()) {
            state.changePassword("12345")
            state.changePassword("")
        }
        else
            state.changePassword("")

    }*/

     Row(//modifier = Modifier.padding(vertical = 16.dp),
         verticalAlignment = Alignment.CenterVertically
     ){
         OutlinedButton(
             colors = ButtonDefaults.buttonColors(backgroundColor = PrimaryDark),
            onClick = {
                lostFocus()
                DialogRouter.navigateTo(DialogItem.RegUserDialog)
            },

            border = null,
            shape = CircleShape,
            contentPadding = PaddingValues(8.dp)
        ) {
            Text(
                text = stringResource(id = R.string.btn_reg),
                color = SelectedItemBottomNavi,// TextOrange,
                fontWeight = FontWeight.Light,
                fontSize = 13.sp,
                letterSpacing = 0.sp
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
            colors = ButtonDefaults.buttonColors(backgroundColor = PrimaryDark),
            onClick = {
                lostFocus()
                //DialogRouter.navigateTo(DialogItem.ProgressDialog)
                DialogRouter.navigateTo(DialogItem.RestoreUserDialog)
            },
            border = null,
            shape = CircleShape,
            contentPadding = PaddingValues(8.dp)
        ) {
            Text(
                text = stringResource(id = R.string.btn_rest),
                color = TextOrange,
                fontWeight = FontWeight.Light,
                fontSize = 13.sp,
                letterSpacing = 0.sp
            )
        }
      }

}