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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.training.shoplocal.classes.PASSWORD_LENGTH
import com.training.shoplocal.classes.emptyChar
import com.training.shoplocal.classes.fillChar
import com.training.shoplocal.dialogs.ShowProgress
import com.training.shoplocal.log
import com.training.shoplocal.ui.theme.*
import com.training.shoplocal.validateMail
import com.training.shoplocal.viewmodel.RepositoryViewModel
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
     val focusManager = LocalFocusManager.current
    val visible = MutableTransitionState(false)
    if (state.isPressedButtons() && state.isFocused()) {
        focusManager.clearFocus(true)
        state.setPressedButtons(false)
        state.setFocus(false)
    }
    val chars = state.getPasswordChar()
    val indexChar = chars.lastIndexOf(fillChar)
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        TextField(value = state.getEmail(), onValueChange = {
            state.setEmail(it)
        },
            Modifier
                .fillMaxWidth()
                .padding(start = 32.dp, end = 32.dp)
                .padding(vertical = 16.dp)
                .onFocusChanged { focusState ->
                    state.setPressedButtons(!focusState.isFocused)
                    state.setFocus(focusState.isFocused)
                    state.setErrorEmail(
                        if (focusState.isFocused)
                            false
                        else
                            !validateMail(state.getEmail())
                    )
                },
            trailingIcon = {
                if (state.getErrorEmail())
                    Icon(Icons.Filled.Email, contentDescription = "", tint = SelectedItem)
                else
                    Icon(Icons.Filled.Email, contentDescription = "")
                           },
            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = TextFieldBg,
                cursorColor = TextFieldFont,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(16.dp),
            singleLine = true,
            keyboardActions = KeyboardActions (
                onDone = {
                    state.setPressedButtons(false)
                    focusManager.clearFocus(true)
                    state.setFocus(false)
                }
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        )
        Row {
            for (index in 0 until PASSWORD_LENGTH) {
                val textColor = if (chars[index] == emptyChar)
                    TextLightGray else TextOrange
                Box(
                    modifier = Modifier
                        .size(40.dp, 36.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (state.isAnimated() && index == indexChar) {
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
        if (state.isProgress())
            ShowProgress()
    }
    SideEffect {
        visible.targetState = true
        state.stopAnimate()
    }
}