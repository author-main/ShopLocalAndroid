package com.training.shoplocal.passwordview

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.training.shoplocal.ui.theme.TextLightGray
import com.training.shoplocal.ui.theme.TextOrange

@Composable
fun PasswordView(state: PasswordViewState) {
    val passwordState = rememberSaveable(saver = PasswordViewState.Saver) { state }
    val chars = passwordState.getPasswordChar()
//    Log.v("shoplocal", passwordState.getPassword())
    Row(){
        for (index in 0 until PasswordViewState.PASSWORD_LENGTH) {
            val textColor = if (chars[index] == PasswordViewState.emptyChar)
                TextLightGray else TextOrange
            Text(
                text = chars[index].toString(),
                modifier = Modifier
                    .padding(8.dp),
                fontSize = 27.sp,
                color = textColor
            )
        }
    }
}