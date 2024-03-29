package com.training.shoplocal.screens.mainscreen.composable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.training.shoplocal.R
import com.training.shoplocal.classes.fodisplay.OrderDisplay
import com.training.shoplocal.classes.fodisplay.SORT_PROPERTY
import com.training.shoplocal.ui.theme.TextFieldBg
import com.training.shoplocal.ui.theme.TextFieldFont

@Composable
fun ShowDataDisplayPanel(modifier: Modifier, onClick: (index: Int) -> Unit){//}, hide: Boolean){
//    val viewModel: RepositoryViewModel = viewModel()
    /*val panelHeightPx = with(LocalDensity.current) { 40.dp.roundToPx().toFloat() }
    val panelOffsetHeightPx = remember { mutableStateOf(0f) }
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                val newOffset = panelOffsetHeightPx.value + delta
                panelOffsetHeightPx.value = newOffset.coerceIn(-panelHeightPx, 0f)
                return Offset.Zero
            }
        }
    }*/

   /* var isShowPanel by remember {
        mutableStateOf(true)
    }*/
    var isShowMenu by remember {
        mutableStateOf(false)
    }

 /*   val animatedValue: Float by animateFloatAsState(
        targetValue =  if (hide) -40f else 0f,
        animationSpec = tween(1200),
        finishedListener = {
    //        isShowPanel = !hide
        }
    )*/

    /*androidx.compose.animation.AnimatedVisibility(
        visible = !hide,
        enter = slideInVertically(),
        exit = slideOutVertically(),

    ) {*/


        //log("hide $hide")
     //   if (isShowPanel) {
            val font = remember { FontFamily(Font(R.font.roboto_light)) }
            val sortItems = stringArrayResource(id = R.array.sort_items)
            //val duration = 1300
        /*    AnimatedVisibility(
                visible = !hide,

                enter = fadeIn(
                    animationSpec = keyframes {
                        this.durationMillis = duration
                    }
                ) + slideInVertically(
                    animationSpec = keyframes {
                        this.durationMillis = duration
                    }
                ),// { it * -1},
                exit = fadeOut(
                    animationSpec = keyframes {
                        this.durationMillis = duration
                    }
                ) + slideOutVertically(
                    animationSpec = keyframes {
                        this.durationMillis = duration
                    }
                )
            ) {*/
            Box(modifier = modifier) {

                Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                  //  val recompose = OrderDisplay.getInstance().state
                    Button(
                        elevation = null,
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
                        border = BorderStroke(0.dp, Color.Transparent),
                        contentPadding = PaddingValues(horizontal = 8.dp),
                        onClick = {
                            isShowMenu = true
                        }) {
                        val sortText = sortItems[OrderDisplay.getInstance().getSortProperty().value]
                        val asc = OrderDisplay.getInstance().getSortType().value == 0 // ASCENDING
                        Icon(
                            modifier = Modifier.size(32.dp)
                                .scale(1f, if (asc) -1f else 1f),
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
                            onClick(1)
                            //viewModel.putComposeViewStack(ComposeView.FILTER)
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
                        onClick(0)
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
            //    }

                //   }
           }
    }
}