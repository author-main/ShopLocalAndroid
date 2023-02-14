package com.training.shoplocal.screens.appscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import com.training.shoplocal.Dp
import com.training.shoplocal.Px
import com.training.shoplocal.R
import com.training.shoplocal.log
import com.training.shoplocal.ui.theme.TextFieldBg
import com.training.shoplocal.ui.theme.TextFieldFont

/*
@Composable
fun PopupSearchHistory() {
    //Modifier.statusBarsPadding().systemBarsPadding()
    Popup(popupPositionProvider = object : PopupPositionProvider {
        override fun calculatePosition(
            anchorBounds: IntRect,
            windowSize: IntSize,
            layoutDirection: LayoutDirection,
            popupContentSize: IntSize
        ): IntOffset {
            val h = anchorBounds.bottom + 8.Px
            log("h = $h")
            log("debug_popup anchorBounds $anchorBounds")
            log("debug_popup windowSize $windowSize")
            log("debug_popup layoutDirection $layoutDirection")
            log("debug_popup popupContentSize $popupContentSize")
            return IntOffset(0, h)
        }
    }) {
        Box(Modifier
            /*.navigationBarsPadding()
            .statusBarsPadding().systemBarsPadding()*/

            .fillMaxWidth()
            .background(Color(0x77ff0000))){
                Text(text = "Checkit")
        }

    }
}

*/
@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun TextFieldSearch(modifier: Modifier, textSearch: MutableState<String>, onSpeechRecognizer: () -> Unit = {},  onFocused: () -> Unit = {}, onSearch: (query: String) -> Unit){
   /* val textSearch = remember {
        mutableStateOf(query)
    }*/
    var isFocused by remember {
        mutableStateOf(false)
    }
 /*   if (isFocused)
        onFocused()
        //PopupSearchHistory()
    }*/
    val interactionSource = remember { MutableInteractionSource() }
//    val keyboardController = LocalSoftwareKeyboardController.current



    BasicTextField(
        modifier = modifier
            .onFocusChanged {
                if (it.isFocused) {
                    isFocused = true
                    onFocused()
                } else if (!it.hasFocus) {
                    isFocused = false
                    log("lost focus")
                }
            }
            // .weight(1f)
            .height(32.dp)
            .background(color = TextFieldBg, shape = RoundedCornerShape(32.dp)),
        cursorBrush = SolidColor(TextFieldFont),
        value = textSearch.value,
        textStyle = TextStyle(color = TextFieldFont),
        onValueChange = {
            textSearch.value = it
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search,
            keyboardType = KeyboardType.Text
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                if (textSearch.value.isNotBlank())
                    onSearch(textSearch.value.trim())
            }
        )
    ) { innerTextField ->
        /*  val error_speechrecognizer =
              stringResource(id = R.string.text_error_speechrecognizer)*/
        TextFieldDefaults.TextFieldDecorationBox(
            value = "",
            placeholder = {
                if (textSearch.value.isEmpty())
                    Text(
                        text = stringResource(id = R.string.text_search),
                        fontSize = 14.sp,
                        color = TextFieldFont.copy(alpha = 0.4f)
                    )
            },
            leadingIcon = {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_search),
                    contentDescription = null
                )
            },
            trailingIcon = {
                val showClearIcon =
                    textSearch.value.isNotEmpty() && isFocused//searchState.value == SearchState.SEARCH_QUERY
                val iconSize = if (showClearIcon) 16.dp else 24.dp
                Icon(
                    imageVector = if (showClearIcon)
                        ImageVector.vectorResource(R.drawable.ic_cancel_bs)
                    else
                        ImageVector.vectorResource(R.drawable.ic_microphone),
                    contentDescription = null,
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(iconSize)
                        .clickable {
                            if (showClearIcon) {
                                textSearch.value = ""
                            } else {
                                // Вызвать голосовой ввод
                                onSpeechRecognizer()
                            }
                        }
                )
            },

            visualTransformation = VisualTransformation.None,
            innerTextField = innerTextField,
            singleLine = true,
            enabled = true,
            interactionSource = remember {
                interactionSource//MutableInteractionSource()
            },
            contentPadding = PaddingValues(0.dp)
        )
    }


}