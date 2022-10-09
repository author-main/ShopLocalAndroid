package com.training.shoplocal.dialogs

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.training.shoplocal.DialogRouter
import com.training.shoplocal.ui.theme.PrimaryDark
import com.training.shoplocal.ui.theme.TextLightGray

@Composable
fun DialogProgress() {
    Popup(
        alignment = Alignment.Center,
        onDismissRequest = { DialogRouter.reset() },
        properties = PopupProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
            focusable = true
        )
    ) {
        Card(
            elevation = 0.dp,
            shape = RoundedCornerShape(12.dp),
            backgroundColor = PrimaryDark,
            contentColor = TextLightGray
        ) {
            Text("Chekit")
        }
    }
}
