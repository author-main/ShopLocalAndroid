package com.training.shoplocal.dialogs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.training.shoplocal.classes.Review
import com.training.shoplocal.ui.theme.PrimaryDark

@Composable
fun DialogReview(openDialog: MutableState<Boolean>, review: Review, widthContent: Dp, textHeight: Dp){
    Dialog(onDismissRequest = { openDialog.value = false},
        properties = DialogProperties(
            dismissOnBackPress = true, dismissOnClickOutside = true
        )
    ) {
        Surface(color = Color.Red){
            Box(modifier = Modifier
                .height(textHeight)
                .verticalScroll(rememberScrollState())
            ) {
                Text(text = review.comment, fontSize = 14.sp)
            }
        }
    }
}