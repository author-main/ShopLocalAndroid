package com.training.shoplocal.screens.appscreen

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.training.shoplocal.*
import com.training.shoplocal.R
import com.training.shoplocal.classes.*
import com.training.shoplocal.screens.mainscreen.StarPanel
import com.training.shoplocal.ui.theme.*
import com.training.shoplocal.viewmodel.RepositoryViewModel
import kotlinx.coroutines.delay
import java.time.LocalDateTime

@Composable
fun ShowDetailProduct(value: Product){
    val product = remember {value}
    val viewModel: RepositoryViewModel = viewModel()
    val reviews = viewModel.reviews.collectAsState()
    val fontCondensed = remember { FontFamily(Font(R.font.robotocondensed_light)) }
    val font = remember { FontFamily(Font(R.font.roboto_light)) }
    @Composable
    fun ShowReviews(count: Int){
        val textReview = "$count ${getAfterWord(count, WORD_REVIEW)}"
        Box(
            modifier = Modifier
                .padding(top = 10.dp)
                .clip(RoundedCornerShape(6.dp))
                .fillMaxWidth()
                .background(PrimaryDark)
        ) {
            Column(
                modifier = Modifier
                    .padding(all = 12.dp)
            ) {
                CompositeButton(color = TextFieldBg.copy(alpha = 0.3f), top = {
                    Box(
                        modifier = Modifier
                            .padding(vertical = 4.dp, horizontal = 8.dp)
                    ) {
                        Row() {
                            Text(
                                text = textReview,
                                color = SelectedItemBottomNavi,
                                fontSize = 13.sp
                            )
                            DividerHorizontal(size = 8.dp)
                            StarPanel(count = product.star)
                            DividerHorizontal(size = 4.dp)
                            Text(
                                text = product.star.toString(),
                                color = TextFieldFont,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "/5",
                                color = TextFieldFont,
                                fontSize = 13.sp
                            )
                        }
                    }

                }, bottom = {})
                DividerVertical(size = 8.dp)
                Column(modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(TextFieldBg.copy(alpha = 0.3f))
                    .padding(all = 8.dp)
                    ) {
                    Text(
                        fontFamily = font,
                        text = reviews.value[0].comment,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
    @Composable
    fun AnimatePrice(price: String){
        val infiniteTransition = rememberInfiniteTransition()
        val rotate by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 1000,
                    delayMillis = 5000,
                    easing = FastOutLinearInEasing
                ),
                repeatMode = RepeatMode.Reverse
            )
        )
        Text(
            modifier = Modifier
                .padding(
                    start = 8.dp,
                    end = 8.dp,
                    top = 8.dp
                )
                .graphicsLayer {
                    rotationY = rotate
                },
            text = price,
            color = ColorText,
            fontSize = 23.sp,
            fontWeight = FontWeight.Medium
        )


    }
    data class Integer(var value: Int = 0)
    val countReviews = remember {
        Integer()
    }
    val indexImage = remember {
        mutableStateOf(0)
    }
    val discountPrice = remember {
        if (product.discount > 0)
            getSalePrice(product.price, product.discount)
        else getFormattedPrice(product.price)
    }
    val yourPrice = remember {
        var priceDiscount = getDiscountPrice(product.price, product.discount)
        if (product.price > LIMIT_BLACKFRIDAY)
            priceDiscount = getDiscountPrice(priceDiscount, PERCENT_BLACKFRIDAY)
        getFormattedPrice(priceDiscount)
    }


    DisposableEffect(Unit) {
        viewModel.getReviewProduct(product.id)
        onDispose {
            viewModel.clearReviews()
        }
    }
    LaunchedEffect(reviews.value.size) {
       // log(reviews.value.size.toString())
        if (reviews.value.isNotEmpty()) {
            /*val username = reviews.value[0].username
            val pos = username.indexOf(' ')
            reviews.value[0].username = username.substring(pos+1)*/
            countReviews.value = reviews.value.size//username.substring(0, pos).toInt()
        }
    }
    val interaction = remember { MutableInteractionSource() }
    val scrollState = rememberScrollState()
    Column(modifier = Modifier
        .fillMaxSize()
        .clickable(
            interactionSource = interaction,
            indication = null
        ) {}
        .background(BgScreenDark)) {
        BoxWithConstraints(modifier = Modifier
            .weight(1f)
            /*.clickable(
                interactionSource = interaction,
                indication = null
            ) {}*/
            .background(BgScreenDark)
        ) {
            val size = this.maxWidth/*with(LocalDensity) {
           32.dp.roundToPx().toFloat()
        }*/
            Column(
                modifier = Modifier
                    .verticalScroll(scrollState)
                    .fillMaxSize()
                    .padding(all = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .size(size)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    ShowProductImages(
                        modifier = Modifier
                            .width(size - 32.dp)
                            .height(size - 32.dp)
                            .padding(all = 8.dp), reduce = false, product = product
                    ) {
                        indexImage.value = it
                    }
                }

                product.linkimages?.let { links ->
                    val count = links.size
                    if (count > 1)
                        ShowImagesIndicator(modifier = Modifier.padding(top = 2.dp), index = indexImage, count = count)
                    else
                        DividerVertical(size = 16.dp)
                }

                Row(modifier = Modifier.fillMaxWidth()) {
                    TextPanel(
                        text = "-${product.discount}%",
                        textColor = Color.White,
                        backgroundColor = BgDiscount
                    )
                    val textSale = product.getTypeSale()
                    if (textSale.isNotBlank()) {
                        DividerHorizontal(size = 4.dp)
                        TextPanel(
                            text = textSale,
                            textColor = TextOrange,
                            backgroundColor = PrimaryDark
                        )
                    }
                }


                Box(
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .fillMaxWidth()
                        .background(PrimaryDark)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(all = 8.dp)
                    ) {
                        val textBrend = product.brand?.let { viewModel.getBrand(it) }
                            ?: stringResource(id = R.string.text_noname)
                        CompositeButton(color = TextFieldBg.copy(alpha = 0.5f), top = {
                            Box(
                                modifier = Modifier
                                    .padding(vertical = 4.dp, horizontal = 8.dp)
                            ) {
                                Text(
                                    text = textBrend,
                                    color = SelectedItemBottomNavi,
                                    fontSize = 13.sp
                                )
                            }

                        }, bottom = {}){}
                        Text(
                            modifier = Modifier.padding(top = 4.dp),
                            text = product.name,
                            fontFamily = font,
                            fontSize = 17.sp
                        )
                    }
                }


                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(intrinsicSize = IntrinsicSize.Min)
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val questions = 4
                    val rate = countReviews.value
                    val textRate =
                        if (rate > 0) "$rate ${getAfterWord(rate, WORD_RATE)}" else getStringResource(R.string.text_norate)
                    CompositeButton(
                        modifier = Modifier.weight(1f),
                        top = {
                            StarPanel(count = product.star)
                        },
                        bottom = {
                            Text(
                                modifier = Modifier.padding(top = 4.dp),
                                text = textRate,
                                color = SelectedItemBottomNavi,
                                fontSize = 13.sp
                            )
                        })
                    CompositeButton(
                        modifier = Modifier.weight(1f),
                        top = {
                            Row(
                                modifier = Modifier.padding(top = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    imageVector = ImageVector.vectorResource(R.drawable.ic_review),
                                    colorFilter = ColorFilter.tint(TextFieldFont),
                                    contentDescription = null
                                )
                                Text(
                                    modifier = Modifier.padding(start = 4.dp),
                                    text = "$rate",
                                    color = TextFieldFont,
                                    fontSize = 13.sp
                                )
                            }
                        },
                        bottom = {
                            Text(
                                modifier = Modifier.padding(bottom = 8.dp),
                                text = stringResource(id = R.string.text_review),
                                color = SelectedItemBottomNavi,
                                fontSize = 13.sp
                            )
                        }) {
                    }
                    CompositeButton(
                        modifier = Modifier.weight(1f),
                        top = {
                            Row(
                                modifier = Modifier.padding(top = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    imageVector = ImageVector.vectorResource(R.drawable.ic_question),
                                    colorFilter = ColorFilter.tint(TextFieldFont),
                                    contentDescription = null
                                )
                                Text(
                                    modifier = Modifier.padding(start = 4.dp),
                                    text = "$questions",
                                    color = TextFieldFont,
                                    fontSize = 13.sp
                                )
                            }
                        },
                        bottom = {
                            Text(
                                modifier = Modifier.padding(bottom = 8.dp),
                                text = stringResource(id = R.string.text_question),
                                color = SelectedItemBottomNavi,
                                fontSize = 13.sp
                            )
                        }) {
                    }
                }

                BoxWithConstraints(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .fillMaxWidth()
                        .background(PrimaryDark)
                ) {
                    val paddingRow = 10.dp
                    val paddingColumn = 8.dp
                    val buttonWidth = (this.maxWidth - paddingColumn * 4 - paddingRow * 2) / 2 /*with(LocalDensity.current) {
                    (boxScope.maxWidth).roundToPx().toFloat() / 2
                }*/
                Column(modifier = Modifier
                    .padding(all = paddingRow)
                    .clip(RoundedCornerShape(4.dp))
                    .fillMaxSize()
                    .background(TextFieldBg.copy(alpha = 0.3f))) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier
                            .padding(horizontal = paddingColumn)
                            .padding(top = paddingColumn, bottom = 2.dp)
                        ) {
                            CompositeButton(
                                modifier = Modifier.width(buttonWidth),
                                color = BgTextPrice,
                                top = {

                                    AnimatePrice(yourPrice)
                                    /*Text(
                                        modifier = Modifier.padding(
                                            start = 8.dp,
                                            end = 8.dp,
                                            top = 8.dp
                                        ),
                                        text = yourPrice,
                                        color = ColorText,
                                        fontSize = 23.sp,
                                        fontWeight = FontWeight.Medium
                                    )*/

                                },
                                bottom = {
                                    Text(
                                        modifier = Modifier.padding(
                                            start = 8.dp,
                                            end = 8.dp,
                                            bottom = 8.dp
                                        ),
                                        text = stringResource(id = R.string.text_final_price),
                                        color = ColorText,
                                        fontSize = 14.sp
                                    )
                                })
                            if (product.price > LIMIT_BLACKFRIDAY) {
                                DividerVertical(size = 8.dp)
                                CompositeButton(
                                    modifier = Modifier.width(buttonWidth),
                                    color = TextOrange,
                                    top = {
                                        Text(
                                            modifier = Modifier.padding(
                                                start = 8.dp,
                                                end = 8.dp,
                                                top = 8.dp
                                            ), text = stringResource(
                                                id = R.string.text_black_discount
                                            ), color = PrimaryDark, fontSize = 13.sp
                                        )
                                    },
                                    bottom = {
                                        Text(
                                            modifier = Modifier.padding(
                                                start = 8.dp,
                                                end = 8.dp,
                                                bottom = 8.dp
                                            ),
                                            text = "-$PERCENT_BLACKFRIDAY%",
                                            color = SelectedItem,
                                            fontSize = 19.sp,
                                            fontWeight = FontWeight.Medium
                                        )
                                    })
                            }
                        }
                        Column(modifier = Modifier.padding(paddingColumn)) {
                            CompositeButton(
                                modifier = Modifier.fillMaxWidth(),//width(buttonWidth),
                                color = Color.Transparent,
                                top = {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(
                                            text = discountPrice,// getSalePrice(product.price, product.discount),
                                            color = TextFieldFont,
                                            fontSize = 21.sp,
                                            fontWeight = FontWeight.Medium
                                        )
                                        if (product.discount > 0)
                                            Text(
                                                text = getFormattedPrice(product.price),
                                                color = TextFieldFont.copy(alpha = 0.6f),
                                                fontSize = 17.sp,
                                                style = TextStyle(textDecoration = TextDecoration.LineThrough),
                                                fontWeight = FontWeight.Medium
                                            )
                                    }
                                },
                                bottom = {
                                    Text(
                                        text = stringResource(id = R.string.text_regular_price),
                                        color = TextFieldFont,
                                        fontSize = 14.sp
                                    )
                                })

                        }
                    }
                    Button(onClick = {
                    },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 8.dp, end = 8.dp, bottom = 4.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = ColorDarkBlue)
                    ){
                        Text(
                                text = stringResource(id = R.string.text_buy_oneclick),
                                color = ColorText,
                                fontFamily = font,
                                //fontSize = 15.sp,*/
                                letterSpacing = 0.sp
                            )
                    }
                }
                }
                Box(
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .fillMaxWidth()
                        .background(PrimaryDark)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(all = 12.dp)
                    ) {
                        CompositeButton(color = TextFieldFont, top = {
                            Box(
                                modifier = Modifier
                                    .padding(vertical = 4.dp, horizontal = 8.dp)
                            ) {
                                Text(
                                    text = stringResource(id = R.string.text_description),
                                    color = PrimaryDark,
                                    fontSize = 13.sp
                                )
                            }

                        }, bottom = {})
                        Text(
                            modifier = Modifier.padding(top = 4.dp),
                            fontFamily = font,
                            text = product.description/*,
                            fontSize = 13.sp*/
                        )
                    }
                }
                if (countReviews.value > 0)
                    ShowReviews(countReviews.value)
            }
        }

        Button(onClick = {
        },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp, start = 16.dp, end = 16.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = SelectedItemBottomNavi)
        ){
            val aMonth = remember{ getStringArrayResource(id = R.array.month)}
            val dateDelivery = remember {
                val date = LocalDateTime.now().plusDays(3)
                val day = date.dayOfMonth
                val month = aMonth[date.monthValue - 1]
                "$day $month"
            }




            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = stringResource(id = R.string.text_addcart),
                    color = ColorText,
                    //fontFamily = font,
                    fontSize = 15.sp,
                    letterSpacing = 0.sp
                )
                Text(
                    text = stringResource(id = R.string.text_datedelivery) + " " + dateDelivery,
                    color = ColorText,
                    fontFamily = fontCondensed,
                    letterSpacing = 0.sp
                )
            }
        }
    }
}