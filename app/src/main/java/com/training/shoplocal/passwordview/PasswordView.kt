package com.training.shoplocal.passwordview

import android.graphics.Paint
import android.text.Layout
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Vertical
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.training.shoplocal.ui.theme.TextLightGray
import com.training.shoplocal.ui.theme.TextOrange

@Composable
fun PasswordView(state: PasswordViewState) {
    @Composable
    fun textChar(value: Char, color: Color){
        Text(
            text = value.toString(),
            fontSize = 27.sp,
            color = color
        )
    }
    Log.v("shoplocal", "recomposition ${state.getPassword()}")
    val visible = MutableTransitionState(false)
    val passwordState = rememberSaveable(saver = PasswordViewState.Saver) { state }
    val chars = passwordState.getPasswordChar()
    val indexChar = chars.lastIndexOf(PasswordViewState.fillChar)

    Row(){
        for (index in 0 until PasswordViewState.PASSWORD_LENGTH) {
            val textColor = if (chars[index] == PasswordViewState.emptyChar)
                TextLightGray else TextOrange
            Box(modifier = Modifier
                .size(32.dp, 36.dp),
                contentAlignment = Alignment.Center
            ) {
                if (passwordState.isAnimated() && index == indexChar)
                    androidx.compose.animation.AnimatedVisibility(
                        visibleState = visible,
                        enter = fadeIn(
                            animationSpec = tween(
                                durationMillis = 200,
                                easing = LinearEasing
                            )
                        )
                    ) {
                        textChar(value = chars[index], color = textColor)
                    }
                 else textChar(value = chars[index], color = textColor)
            }
        }
    }
    SideEffect {
        visible.targetState = true
    }
}