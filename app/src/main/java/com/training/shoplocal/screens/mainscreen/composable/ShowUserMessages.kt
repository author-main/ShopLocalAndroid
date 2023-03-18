package com.training.shoplocal.screens.mainscreen.composable

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.training.shoplocal.*
import com.training.shoplocal.R
import com.training.shoplocal.classes.EMPTY_STRING
import com.training.shoplocal.viewmodel.RepositoryViewModel
import com.training.shoplocal.classes.USERMESSAGE_DELETE
import com.training.shoplocal.classes.USERMESSAGE_READ
import com.training.shoplocal.classes.UserMessage
import com.training.shoplocal.ui.theme.*
import kotlinx.coroutines.launch
import kotlin.math.roundToInt





@Composable
private fun CancelAction(modifier: Modifier, isShow: MutableState<Boolean>, index: Int, content: @Composable () -> Unit, onCancel: (cancel: Boolean) -> Unit){

    val snackbarHostState = remember { mutableStateOf(SnackbarHostState()) }

        LaunchedEffect(index) {
            val result = snackbarHostState.value.showSnackbar(
                EMPTY_STRING,
                duration = SnackbarDuration.Short
            )
            if (result == SnackbarResult.Dismissed) {
                onCancel(false)
                    isShow.value = false
                    //        (viewModel as RepositoryViewModel).showSnackbar(visible = false)
                }
              /*  SnackbarResult.ActionPerformed -> {

                }*/
            //}
        }

    //    Box(Modifier.fillMaxSize()){
            SnackbarHost(
                modifier = modifier.padding(bottom = 32.dp),//Modifier.align(Alignment.BottomCenter),
                hostState = snackbarHostState.value,
                //snackbar = { snackbarData: SnackbarData ->

                snackbar = { _ ->
                    Card(
                        shape = RoundedCornerShape(6.dp),
                        backgroundColor = TextFieldBg
                    ) {
                        Box(modifier = Modifier.background(Color.Red).padding(8.dp), contentAlignment = Alignment.Center) {
                            content()
                        }
                    }


                })
     //   }



}

@Composable
private fun ShowWarningInformation(){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        backgroundColor = TextFieldBg.copy(alpha=0.7f)
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(id = R.string.text_warninginformation),
                    fontSize = 14.sp, fontWeight = FontWeight.Medium,
                    color = ColorText.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = stringResource(id = R.string.text_warninginformation1),
                    fontSize = 13.sp, color = ColorText.copy(alpha = 0.5f),
                    textAlign = TextAlign.Center
                )
            }
            Image(modifier = Modifier
                .width(32.dp)
                .padding(start = 8.dp)
                .align(Alignment.CenterVertically),
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_yellow_warning),
            contentDescription = null)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun ShowUserMessages(open: MutableState<Boolean>, onSelectMessage: (message: UserMessage) -> Unit = {}){
    val isShowSnackbar = remember {
        mutableStateOf(false)
    }
    val coroutine = rememberCoroutineScope()
    val width_button =
        with(LocalDensity.current) {
            100.dp.roundToPx().toFloat()
        }
    //val paddingIcon = remember {(100.dp - 24.dp) / 2}

    /**
     * 0 - ОБЫЧНОЕ СООБЩЕНИЕ
     * 1 - СООБЩЕНИЕ О ДОСТАВКЕ
     * 2 - СООБЩЕНИЕ О СКИДКАХ
     * 3 - СООБЩЕНИЕ ПОЗДРАВЛЕНИЕ
     */
    val USER_MESSAGE_NORMAL           = 0
    val USER_MESSAGE_DELIVERY         = 1
    val USER_MESSAGE_DISCOUNT         = 2
    val USER_MESSAGE_GIFT             = 3
    data class Integer(var value: Int)
    val userMessage = remember{UserMessage()}
    val messageIndex = remember{Integer(-1)}
    val viewModel: RepositoryViewModel = viewModel()
    val refreshing = viewModel.refreshUserMessages.collectAsState(false)
    val pullRefreshState = rememberPullRefreshState(refreshing.value, { viewModel.updateUserMessages() })
    val messages = remember {
        viewModel.userMessages
    }//.collectAsState()
    val title = remember{getStringArrayResource(R.array.typemessage)}
    val font = remember { FontFamily(Font(R.font.roboto_light)) }
    val close = remember{ mutableStateOf(false) }
    DisposableEffect(Unit) {
        viewModel.getMessages()
        onDispose {
            //log(messages.toString())
            viewModel.clearMessages()
        }
    }

        AnimatedScreen(open, close) {

            Column(modifier = Modifier.fillMaxSize()) {
                TopBar(onClick = {close.value = true}) {
                    Text(modifier = Modifier.weight(1f),
                        text = stringResource(R.string.text_usermessages),
                        color = TextFieldFont,
                        fontSize = 17.sp
                    )
                }
                ShowWarningInformation()

               /* SwipeRefresh(
                    state = rememberSwipeRefreshState(isRefreshing),
                    onRefresh = { viewModel.updateForecast() },
                    indicator = {state, dp ->
                        SwipeRefreshIndicator(
                            state = state,
                            refreshTriggerDistance = dp,
                            contentColor = Color(150,0,0)
                        )
                    }
                ) {*/
                if (messages.isEmpty())
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = stringResource(id = R.string.text_nousermessages), textAlign = TextAlign.Center, fontSize = 14.sp, color = ColorText.copy(alpha = 0.2f))
                    }
                else
                Box(
                    Modifier
                        .pullRefresh(pullRefreshState)
                        .fillMaxHeight()) {
                    LazyColumn(modifier = Modifier.padding(horizontal = 16.dp)) {

                        itemsIndexed(messages, { _, message -> message.id }) { index, item ->
                            val dismissState = rememberDismissState(
                               /* confirmStateChange = {
                                    if (it == DismissValue.DismissedToStart){
                                       // viewModel.updateUserMessage(item.id, USERMESSAGE_DELETE)
                                        log("delete item ${item.id}...")
                                    }
                                    false
                                }*/
                            )


                            val drag = remember {
                                derivedStateOf {
                                    - dismissState.offset.value <= width_button

                                }
                            }

                            val isDissmissed = dismissState.isDismissed(DismissDirection.EndToStart)
                            //if (isDissmissed) {//(dismissState.isDismissed(DismissDirection.EndToStart)) {
                                //log("delete item ${item.id}...")
                                /*CancelAction(content = {

                                }) {
                                    viewModel.updateUserMessage(item.id, USERMESSAGE_DELETE)

                                }*/
                                LaunchedEffect(isDissmissed) {
                                    if (isDissmissed) {
                                        coroutine.launch {
                                            userMessage.copydata(item)
                                            messageIndex.value = index
                                            dismissState.reset()
                                            if(isShowSnackbar.value)
                                                isShowSnackbar.value = false
                                            isShowSnackbar.value = true
                                        }
                                    }
                                }
                           // }



                                 LaunchedEffect(drag.value) {
                                     if (!drag.value)
                                        vibrate(30)


                                    /* if (!drag.value) {
                                         log("block drag...")
                                         coroutine.launch {
                                             //dismissState.dismissDirection
                                         }
                                     } else {
                                         log("drag...")
                                     }*/
                                 }





                            /* }

            items(messages.value, {message -> message.id}) { item ->*/
                            var colorTitle = ColorText
                            val imageId =
                                when (item.type) {
                                    USER_MESSAGE_DELIVERY -> {
                                        if (item.read == 0)
                                            colorTitle = SelectedItemBottomNavi
                                        R.drawable.ic_delivery
                                    }
                                    USER_MESSAGE_DISCOUNT -> R.drawable.ic_discount
                                    USER_MESSAGE_GIFT -> R.drawable.ic_gift
                                    else -> R.drawable.ic_usermessage
                                }
                            Column(
                                Modifier

                                    .clip(RectangleShape)

                                    .animateItemPlacement()
                                    .clickable {
                                        onSelectMessage(item)
                                        if (item.read == 0)
                                            viewModel.updateUserMessage(item.id, USERMESSAGE_READ)
                                    }
                            ) {
                                SwipeToDismiss(
                                    state = dismissState,
                                    modifier = Modifier
                                        .padding(vertical = Dp(1f)),
                                    directions = setOf(
                                        DismissDirection.EndToStart
                                    ),
                                    dismissThresholds = {FractionalThreshold(0.2f)}/*{ direction ->
                                        FractionalThreshold(if (direction == DismissDirection.EndToStart) 0.2f else 0.05f)
                                    }*/,
                                    background = {
                                        val colorDismiss by animateColorAsState(
                                            when (dismissState.targetValue) {
                                                DismissValue.Default -> PrimaryDark
                                                else -> SelectedItem
                                            }
                                        )
                                        val scale by animateFloatAsState(
                                            if (dismissState.targetValue == DismissValue.Default) 0.75f else 1f
                                        )
                                        Box(
                                            Modifier
                                                // .swipeable()
                                                .fillMaxSize()
                                                .background(colorDismiss),
                                               // .padding(horizontal = Dp(20f)),
                                            contentAlignment = Alignment.CenterEnd
                                        ) {
                                            Box(
                                                Modifier
                                                    .width(100.dp)
                                                    .fillMaxHeight(),
                                                    contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    Icons.Default.Close,
                                                    contentDescription = null,
                                                    modifier = Modifier
                                                        .scale(scale)
                                                )
                                            }
                                        }

                                    },
                                    dismissContent = {


                                        Row(modifier = Modifier
                                            .offset {
                                                IntOffset(
                                                    if (drag.value) 0 else
                                                        -dismissState.offset.value.roundToInt() - width_button.roundToInt(),
                                                    0
                                                )
                                            }
                                            .background(PrimaryDark)
                                            .padding(vertical = 8.dp)) {
                                            Image(
                                                modifier = Modifier
                                                    .padding(end = 8.dp)
                                                    .align(Alignment.CenterVertically)
                                                    .size(48.dp),
                                                imageVector = ImageVector.vectorResource(imageId),
                                                contentScale = ContentScale.FillBounds,
                                                contentDescription = null
                                            )
                                            Column(modifier = Modifier.weight(1f)) {
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {

                                                    Row(
                                                        modifier = Modifier.weight(1f),
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        Text(
                                                            //    modifier = Modifier.weight(1f),
                                                            text = title[item.type],
                                                            fontWeight = FontWeight.Medium,
                                                            fontSize = 15.sp,
                                                            color = colorTitle
                                                            //color = if (item.read == 0) SelectedItemBottomNavi else TextFieldFont
                                                        )
                                                        if (item.read != 0) {
                                                            DividerHorizontal(size = 4.dp)
                                                            Icon(
                                                                modifier = Modifier.size(16.dp),
                                                                imageVector = ImageVector.vectorResource(
                                                                    id = R.drawable.ic_check_circle
                                                                ),
                                                                contentDescription = null,
                                                                tint = SelectedItemBottomNavi
                                                            )
                                                        }
                                                    }
                                                    /* Box(modifier = Modifier
                                    .clip(CircleShape)
                                    .background(if (item.read != 0) Color.Transparent else SelectedItemBottomNavi),
                                ) {*/
                                                    Text(
                                                        // modifier = Modifier.padding(vertical = 2.dp, horizontal = 4.dp),
                                                        /* modifier = Modifier
                                            .clip(CircleShape)
                                            .background(if (item.read != 0) Color.Transparent else SelectedItemBottomNavi),*/
                                                        /*.weight(1f),
                                    textAlign = TextAlign.End,*/
                                                        text = item.date, fontSize = 12.sp,
                                                        color = //if (item.read == 0) ColorText else
                                                        TextFieldFont.copy(alpha = 0.5f)
                                                    )
                                                    // }
                                                }
                                                Text(
                                                    text = item.message,
                                                    fontFamily = font,
                                                    fontSize = 14.sp
                                                )
                                            }
                                        }

                                    })


                                if (index < messages.size - 1)
                                  Spacer(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(1.dp)
                                            .background(Color(0x20FFFFFF))
                                    )
                            }
                        }
                    }

                    if (isShowSnackbar.value)
                        CancelAction(modifier = Modifier.align(Alignment.BottomCenter), index = messageIndex.value, isShow = isShowSnackbar, content = {
                            Text(text = "Checkit...")//userMessage.message)
                        }) {cancel ->
                            if (!cancel)
                                viewModel.updateUserMessage(userMessage.id, USERMESSAGE_DELETE)
                        }


                    PullRefreshIndicator(refreshing.value, pullRefreshState, Modifier.align(Alignment.TopCenter), contentColor = SelectedItemBottomNavi)
                }
              //  }


        }

    }
}