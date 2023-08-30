package com.training.shoplocal.dialogs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.training.shoplocal.DialogItem
import com.training.shoplocal.DialogRouter
import com.training.shoplocal.R
import com.training.shoplocal.classes.Review
import com.training.shoplocal.classes.TAB_CHAR
import com.training.shoplocal.screens.mainscreen.StarPanel
import com.training.shoplocal.screens.mainscreen.composable.DividerVertical
import com.training.shoplocal.ui.theme.*

@Composable
fun DialogReview(openDialog: MutableState<Boolean>, review: Review){//}, widthContent: Dp, textHeight: Dp){
    val font = remember {
        FontFamily(Font(R.font.roboto_light))
    }
    Dialog(onDismissRequest = { openDialog.value = false},
        properties = DialogProperties(
            dismissOnBackPress = true, dismissOnClickOutside = true
        )
    ) {
        Surface(
            border = BorderStroke(2.dp, TextFieldBg),
            color = PrimaryDark,
            shape = RoundedCornerShape(12.dp)
        ){
            Column(modifier = Modifier
                .padding(16.dp)) {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp),
                verticalAlignment = Alignment.CenterVertically){
                    Column(modifier = Modifier.weight(1f)){
                        Text(text = review.username, fontSize = 14.sp, fontFamily = font, color = SelectedItemBottomNavi)
                        Text(text = review.date, fontSize = 12.sp, fontFamily = font, color = TextFieldFont.copy(alpha = 0.6f))
                    }
                    StarPanel(count = review.countstar.toFloat(), starSize = 20.dp, starHorzInterval = 6.dp)
                }
                Box(
                    modifier = Modifier
                        .heightIn(1.dp, 296.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = "$TAB_CHAR${review.comment}",
                        fontSize = 14.sp,
                        fontFamily = font,
                        color = ColorText
                    )
                }
                DividerVertical(size = 12.dp)
                Spacer(
                    Modifier
                        .height(1.dp)
                        .fillMaxWidth()
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    MaterialTheme.colors.primary,
                                    Color(0x1BDDDDDD),
                                    MaterialTheme.colors.primary
                                )
                            )
                        )
                )
                DividerVertical(size = 12.dp)
                OutlinedButton(modifier = Modifier.align(Alignment.CenterHorizontally),
                    colors = ButtonDefaults.buttonColors(backgroundColor = PrimaryDark),
                    onClick = {
                        openDialog.value = false
                    },
                    border = BorderStroke(1.dp, TextFieldFont.copy(alpha = 0.2f)),
                    shape = CircleShape,
                    contentPadding = PaddingValues(8.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.text_ok),
                        color = SelectedItemBottomNavi,
                        fontWeight = FontWeight.Light,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}