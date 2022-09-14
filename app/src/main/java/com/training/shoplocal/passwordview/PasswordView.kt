package com.training.shoplocal.passwordview

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.training.shoplocal.ui.theme.TextLightGray
import com.training.shoplocal.ui.theme.TextOrange

@Composable
fun PasswordView(state: PasswordViewState) {
    val passwordState = rememberSaveable(saver = PasswordViewState.Saver) { state }
    val chars = passwordState.getPasswordChar()
    val indexChar = chars.lastIndexOf(PasswordViewState.fillChar)
    //val scale = remember { Animatable(initialValue = 0f) }
 //   Log.v("shoplocal", "password=${passwordState.getPassword()}, prev=${passwordState.prev}")
    Row(){
        for (index in 0 until PasswordViewState.PASSWORD_LENGTH) {
            val textColor = if (chars[index] == PasswordViewState.emptyChar)
                TextLightGray else TextOrange
          /*  val animate by animateFloatAsState(
                targetValue = 20f,
                animationSpec = tween(
                    durationMillis = 1500
                )
            )*/
            if (passwordState.isAnimated() && index == indexChar) {
                Log.v("shoplocal", "animate")
                SideEffect {
                    passwordState.stopAnimate()
                }
            }
            /*LaunchedEffect(passwordState.isAnimated() && index == indexChar) {
                scale.animateTo(
                    targetValue = 10f,
                    animationSpec = tween(durationMillis = 2000),
                )
            }*/

            Text(
                text = chars[index].toString(),
                modifier = Modifier
                    .padding(8.dp)
                  /*  .graphicsLayer(
                        scaleX = scale.value,
                        scaleY = scale.value
                    )*/,
                fontSize = 27.sp,
                color = textColor
            )
        }
    }
  /*  SideEffect {
        passwordState.stopAnimate()
    }*/
}