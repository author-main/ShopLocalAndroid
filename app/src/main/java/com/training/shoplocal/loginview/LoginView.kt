package com.training.shoplocal.loginview

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.training.shoplocal.ui.theme.*
import java.time.format.TextStyle

@Composable
fun LoginView(state: LoginViewState) {
    @Composable
    fun textChar(value: Char, color: Color){
        Text(
            text = value.toString(),
            fontSize = 25.sp,
            color = color
        )
    }
    //Log.v("shoplocal", "recomposition ${state.getPassword()}")
    val email = remember { mutableStateOf("") }
    val visible = MutableTransitionState(false)
    val passwordState = remember { state }
    //val passwordState = rememberSaveable(saver = PasswordViewState.Saver) { state }
    val chars = passwordState.getPasswordChar()
    val indexChar = chars.lastIndexOf(LoginViewState.fillChar)
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        TextField(value = email.value, onValueChange = {
            email.value = it
        },
            Modifier.padding(vertical = 16.dp),
            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = TextFieldBg,
                cursorColor = TextFieldFont,
                //disabledLabelColor = lightBlue,*/
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            label = { Text("Email") },
            shape = RoundedCornerShape(8.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
        Row() {
            for (index in 0 until LoginViewState.PASSWORD_LENGTH) {
                val textColor = if (chars[index] == LoginViewState.emptyChar)
                    TextLightGray else TextOrange
                Box(
                    modifier = Modifier
                        .size(40.dp, 36.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (passwordState.isAnimated() && index == indexChar) {
                        //Log.v("shoplocal", "animated")
                        androidx.compose.animation.AnimatedVisibility(
                            visibleState = visible,
                            enter = fadeIn(
                                animationSpec = tween(
                                    durationMillis = 300,
                                    easing = LinearEasing
                                )
                            )
                        ) {
                            textChar(value = chars[index], color = textColor)
                        }
                    } else textChar(value = chars[index], color = textColor)
                }
            }
        }
    }
    SideEffect {
        visible.targetState = true
    }
}