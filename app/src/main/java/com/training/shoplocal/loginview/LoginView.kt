package com.training.shoplocal.loginview

import android.util.Log
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.training.shoplocal.log
import com.training.shoplocal.ui.theme.*
import com.training.shoplocal.validateMail
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
    //val focusRequester = remember { FocusRequester() }
    val errorEmail = rememberSaveable { mutableStateOf(false) }
    val focused= remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val visible = MutableTransitionState(false)
    val passwordState = remember { state }
    val email = remember { mutableStateOf(state.getEmail()) }


    if (passwordState.isPressedButtons() && focused.value) {
        focusManager.clearFocus(true)
        passwordState.setPressedButtons(false)
        focused.value = false
        //log("lost focus")
    }

    //val passwordState = rememberSaveable(saver = PasswordViewState.Saver) { state }

    val chars = passwordState.getPasswordChar()
    val indexChar = chars.lastIndexOf(LoginViewState.fillChar)
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        TextField(value = email.value, onValueChange = {
            email.value = it
            passwordState.setEmail(it)
        },
            Modifier
                .fillMaxWidth()
                .padding(start = 32.dp, end = 32.dp)
                .padding(vertical = 16.dp)
                .onFocusChanged { focusState ->
                    passwordState.setPressedButtons(!focusState.isFocused)
                    focused.value = focusState.isFocused
                    log("$focusState.toString()")
                    var error = false
                    error = if (focusState.isFocused)
                        false
                    else
                        !validateMail(email.value)
                    errorEmail.value = error
                    /*if (focusState.isFocused)
                        errorEmail.value = false
                    if (focusState.hasFocus)
                        errorEmail.value = !validateMail(email.value)*/

/*                when {
                    focusState.isFocused -> {
                        passwordState.setPressedButtons(false)
                    }
                    focusState.hasFocus -> {
                        passwordState.setPressedButtons(true)
                    }
                }*/

                }
               /* .focusRequester(focusRequester)
                .onFocusChanged {
                    if (it.isFocused)
                        passwordState.setPressedButtons(false)
                }*/,

           /* keyboardActions = KeyboardActions(
                onAny = {
                    Log.v("shoplocal", "focus");
                    passwordState.setFocus(true)}
            ) ,*/
            trailingIcon = {
                if (errorEmail.value)
                    Icon(Icons.Filled.Email, contentDescription = "", tint = SelectedItem)
                else
                    Icon(Icons.Filled.Email, contentDescription = "")
                           },
            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = TextFieldBg,
                cursorColor = TextFieldFont,
                //disabledLabelColor = lightBlue,*/
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            //label = { Text("Email") },
            //placeholder = { Text("Email") },
            shape = RoundedCornerShape(16.dp),
            singleLine = true,
            keyboardActions = KeyboardActions (
                onDone = {
                    passwordState.setPressedButtons(false)
                    focusManager.clearFocus(true)
                    focused.value = false
                }
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
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
        passwordState.stopAnimate()
    }
}