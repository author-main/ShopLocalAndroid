package com.training.shoplocal.screens.appscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.training.shoplocal.ui.theme.*

data class CheckedStyle(var border: Dp = 2.dp, var borderColor: Color = TextFieldFont, var backgroundColor: Color = CheckBoxBg,
                        var checkmarkColor: Color = TextPrice, var selectedBackgroundColor: Color = TextBrand,
                        var textColor: Color = TextFieldFont)

@Composable
fun CustomCheckBox(modifier: Modifier,style: CheckedStyle = CheckedStyle(), text: String, checked: Boolean = false, onChecked: (value: Boolean) -> Unit){
    var isChecked by remember {
        mutableStateOf(checked)
    }
    Row(modifier = modifier
        .toggleable(value = isChecked,role = Role.Checkbox){
            isChecked = it
        },
          verticalAlignment = Alignment.CenterVertically
    ){
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .requiredSize(17.dp)
                .background(if (isChecked) style.selectedBackgroundColor else style.borderColor)
                .padding(if (isChecked) 0.dp else style.border)
                .clip(CircleShape)
                .background(if (isChecked) style.selectedBackgroundColor else style.backgroundColor)
        ) {
            if(isChecked){
                Icon(modifier = Modifier.padding(1.dp),
                    imageVector = Icons.Default.Check, contentDescription = null,
                tint = style.checkmarkColor
                    )
            }
        }
        Text(modifier = Modifier.padding(start = 6.dp),
            text = text,
            color = style.textColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}