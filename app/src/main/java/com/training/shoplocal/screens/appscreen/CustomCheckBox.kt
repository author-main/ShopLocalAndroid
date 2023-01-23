package com.training.shoplocal.screens.appscreen

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.training.shoplocal.ui.theme.TextBrand
import com.training.shoplocal.ui.theme.TextFieldBg
import com.training.shoplocal.ui.theme.TextFieldFont
import com.training.shoplocal.ui.theme.TextPrice

data class CheckedStyle(var border: Dp = 1.dp, var borderColor: Color = TextFieldFont, var backgroundColor: Color = TextFieldBg,
                        var checkmarkColor: Color = TextPrice, var selectedBackgroundColor: Color = TextBrand)

@Composable
fun CustomCheckBox(style: CheckedStyle = CheckedStyle(), checked: Boolean = false, onChecked: (value: Boolean) -> Unit){
    var checkedState by remember {
        mutableStateOf(false)
    }
    checkedState = checked
}