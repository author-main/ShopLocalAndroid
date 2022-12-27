package com.training.shoplocal.screens.appscreen

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.training.shoplocal.R
import com.training.shoplocal.classes.OrderDisplay
import com.training.shoplocal.classes.SORT_PROPERTY
import com.training.shoplocal.log
import com.training.shoplocal.ui.theme.TextFieldBg
import com.training.shoplocal.ui.theme.TextFieldFont
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun ShowDataDisplayPanel(hide: Boolean){
    var isShowPanel by remember {
        mutableStateOf(true)
    }
    var isShowMenu by remember {
        mutableStateOf(false)
    }
    val animatedValue: Float by animateFloatAsState(
        targetValue =  if (hide) -40f else 0f,
        animationSpec = tween(1200),
        finishedListener = {
    //        isShowPanel = !hide
        }
    )

    /*androidx.compose.animation.AnimatedVisibility(
        visible = !hide,
        enter = slideInVertically(),
        exit = slideOutVertically(),

    ) {*/


        //log("hide $hide")
        if (isShowPanel) {
            val font = remember { FontFamily(Font(R.font.roboto_light)) }
            val sortItems = stringArrayResource(id = R.array.sort_items)
            Box (modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .background(Color.Red)//MaterialTheme.colors.primary)
                .offset(0.dp, animatedValue.dp)
            ) {
                Row(
                    modifier = Modifier
                       // .height(intrinsicSize = IntrinsicSize.Min)
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val recompose = OrderDisplay.getInstance().state
                    Button(
                        elevation = null,
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
                        border = BorderStroke(0.dp, Color.Transparent),
                        contentPadding = PaddingValues(horizontal = 8.dp),
                        onClick = { isShowMenu = true }) {
                        val sortText = sortItems[OrderDisplay.getInstance().getSortProperty().value]
                        Icon(
                            modifier = Modifier.size(32.dp),
                            tint = TextFieldFont,
                            imageVector = ImageVector.vectorResource(R.drawable.ic_sort),
                            contentDescription = null
                        )
                        Text(
                            text = sortText,
                            color = TextFieldFont,
                            fontFamily = font,
                            fontSize = 12.sp,
                            letterSpacing = 0.sp
                        )
                    }
                    Spacer(Modifier.weight(1f))
                    Button(
                        elevation = null,
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
                        border = BorderStroke(0.dp, Color.Transparent),
                        contentPadding = PaddingValues(horizontal = 8.dp),
                        onClick = {
                        }) {
                        Icon(
                            modifier = Modifier.size(32.dp),
                            tint = TextFieldFont,
                            imageVector = ImageVector.vectorResource(R.drawable.ic_filter),
                            contentDescription = null
                        )
                        Text(
                            text = stringResource(id = R.string.text_filter),
                            color = TextFieldFont,
                            fontFamily = font,
                            fontSize = 12.sp,
                            letterSpacing = 0.sp
                        )
                    }

                }

                DropdownMenu(modifier = Modifier.background(TextFieldBg),
                    expanded = isShowMenu,
                    offset = DpOffset(x = 16.dp, y = 0.dp),
                    onDismissRequest = { isShowMenu = false }
                ) {
                    fun changeSortProperty(value: SORT_PROPERTY) {
                        OrderDisplay.getInstance().setSortProperty(value)
                        isShowMenu = false
                    }
                    DropdownMenuItem(onClick = {
                        changeSortProperty(SORT_PROPERTY.PRICE)
                    }) {
                        Text(
                            sortItems[0],
                            fontSize = 14.sp
                            //style = MaterialTheme.typography.h4
                        )
                    }
                    DropdownMenuItem(onClick = {
                        changeSortProperty(SORT_PROPERTY.POPULAR)
                    }) {
                        Text(
                            sortItems[1],
                            fontSize = 14.sp
                            // style = MaterialTheme.typography.h4
                        )
                    }
                    //Divider()
                    DropdownMenuItem(onClick = {
                        changeSortProperty(SORT_PROPERTY.RATING)
                    }) {
                        Text(
                            sortItems[2],
                            fontSize = 14.sp
                            // style = MaterialTheme.typography.h4
                        )
                    }
                }

            }

    }
}