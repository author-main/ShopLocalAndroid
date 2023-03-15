package com.training.shoplocal.screens.mainscreen.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.training.shoplocal.getStringArrayResource
import com.training.shoplocal.log
import com.training.shoplocal.viewmodel.RepositoryViewModel
import com.training.shoplocal.R
import com.training.shoplocal.ui.theme.*

@Composable
fun ShowUserMessages(open: MutableState<Boolean>){
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
    val viewModel: RepositoryViewModel = viewModel()
    val messages = viewModel.userMessages.collectAsState()
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

            LazyColumn(modifier = Modifier.padding(horizontal = 16.dp)) {

                itemsIndexed(messages.value, { _, message -> message.id }) { index, item ->

                    /* }

            items(messages.value, {message -> message.id}) { item ->*/
                    val imageId =
                        when (item.type) {
                            USER_MESSAGE_DELIVERY -> R.drawable.ic_delivery
                            USER_MESSAGE_DISCOUNT -> R.drawable.ic_discount
                            USER_MESSAGE_GIFT -> R.drawable.ic_gift
                            else -> R.drawable.ic_usermessage
                        }
                    Column() {
                        Row(modifier = Modifier.padding(vertical = 8.dp)) {
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
                                    Text(
                                        modifier = Modifier.weight(1f),
                                        text = title[item.type],
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 15.sp,
                                        //color = if (item.read == 0) SelectedItemBottomNavi else TextFieldFont
                                    )
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
                                Text(text = item.message, fontFamily = font, fontSize = 14.sp)
                            }
                        }
                        if (index < messages.value.size - 1)
                        /*   Spacer(
                            Modifier
                                .height(1.dp)
                                .fillMaxWidth()
                                .background(
                                    brush = Brush.horizontalGradient(
                                        colors = listOf(
                                            PrimaryDark,
                                            Color(0x5BDDDDDD),
                                            PrimaryDark
                                        )
                                    )
                                )
                        )*/


                            Spacer(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(1.dp)
                                    .background(Color(0x20FFFFFF))
                            )
                    }
                }
            }
        }
    }
}