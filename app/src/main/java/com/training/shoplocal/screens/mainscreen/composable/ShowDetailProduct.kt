package com.training.shoplocal.screens.mainscreen.composable

import android.content.ClipData.Item
import android.graphics.Paint
import android.graphics.Rect
import android.util.TypedValue
import android.widget.Space
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFontFamilyResolver
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.training.shoplocal.*
import com.training.shoplocal.R
import com.training.shoplocal.classes.*
import com.training.shoplocal.dialogs.DialogReview
import com.training.shoplocal.screens.mainscreen.StarPanel
import com.training.shoplocal.screens.mainscreen.composable.*
import com.training.shoplocal.ui.theme.*
import com.training.shoplocal.viewmodel.RepositoryViewModel
import kotlinx.coroutines.delay
import java.time.LocalDateTime

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ShowDetailProduct(value: Product){
    val product = remember {value}
    val viewBigImages = remember {
        mutableStateOf(false)
    }
    val indexImage = remember {
        mutableStateOf(0)
    }

    if (viewBigImages.value)
        ShowBigProductImages(open = viewBigImages, product = product, index = indexImage.value)

    val viewModel: RepositoryViewModel = viewModel()
    val reviews = viewModel.reviews.collectAsState()
    val dialogReview = remember {
        Review(comment = "", username = "", countstar = 1, date = "")
    }
    val fontCondensed = remember { FontFamily(Font(R.font.robotocondensed_light)) }
    val font = remember { FontFamily(Font(R.font.roboto_light)) }
    val openDialogReview = remember {
        mutableStateOf(false)
    }

    val currentDensity = LocalDensity.current
    val density = remember { currentDensity }
    val currentContext = LocalContext.current
    val context = remember {currentContext}

    fun getTextHeight(fontSize: Int, lines: Int): Float{//Pair<Float, Int>{ // междустроковый интервал, высота строки
        var lineSpace : Float
        var textHeight: Int
        val fontSizePx =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                fontSize.toFloat(), context.resources.displayMetrics)
        val paint = Paint()
        paint.apply {
            lineSpace = fontSpacing
            textSize = fontSizePx//sizePx
            val bound = Rect()
            val str = "Aq"
            getTextBounds(str, 0, 2, bound)
            textHeight = bound.height()
        }
        return (lines - 2) * lineSpace + lines * textHeight
    }



    @Composable
    fun ShowDialogReview(){//widthContent: Dp){
        if (openDialogReview.value) {
            //val lines = if (dialogReview.lines > 15) 15 else dialogReview.lines
            //log ("lines = $lines")
            //val textHeight = density.run { getTextHeight(14, lines).toDp()}
            DialogReview(openDialogReview, dialogReview)//, widthContent, textHeight)
        }
    }

    @Composable
    fun ShowReviews(modifier: Modifier){
        val textHeight = remember {
            getTextHeight(fontSize = 14, lines = 8)
        }
        @Composable
        fun ItemReview(value: Review, columnWidth: Dp, onClick: (review: Review) -> Unit){
            val review = remember {
                value
            }
            ShowDialogReview()//(widthContent = columnWidth)
            Column(modifier = Modifier
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null
                ) {
                    if (review.hasOverflow) {
                        onClick(review)
                        openDialogReview.value = true
                    }
                }
                //.fillMaxWidth()
                /*    .clip(RoundedCornerShape(4.dp))
                .background(TextFieldBg.copy(alpha = 0.3f))*/
                .padding(all = 8.dp)
            ) {
                Row(modifier = Modifier
                    .requiredWidth(columnWidth)
                    .padding(bottom = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Column(){
                        Text(text = review.username, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                        Text(text = review.date, fontSize = 12.sp, color = TextFieldFont.copy(alpha = 0.6f))
                    }
                    //Spacer(modifier = Modifier.weight(1f))
                    StarPanel(count = review.countstar.toFloat(), starSize = 16.dp, starHorzInterval = 8.dp)
                }
                    //val scrollState = rememberScrollState()
                Box(modifier = Modifier
                    .requiredHeight(
                        density.run {
                            textHeight.toDp()
                        }
                    )
                    .requiredWidth(columnWidth)){
                    //.verticalScroll(scrollState)) {
                    val textReview = "$TAB_CHAR${review.comment}"
                    Text(
                        fontFamily = font,
                       // maxLines = 8,
                        softWrap = true,
                        text = textReview,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 14.sp,
                        onTextLayout = { textLayoutResult ->
                           review.hasOverflow = textLayoutResult.hasVisualOverflow
                        }

                                /*onTextLayout = { result: TextLayoutResult ->
                            val cursorRect = result.getCursorRect(0)

                            val info = "firstBaseline: ${result.firstBaseline}, " +
                                    "lastBaseline: ${result.lastBaseline}\n" +
                                    "cursorRect: $cursorRect\n" +
                                    "getLineBottom: ${result.getLineBottom(0)}, " +
                                    "getBoundingBox: ${result.getBoundingBox(0)}"
                            val heightInPx =
                                result.size.height.toFloat()
                                    log(info)
                        }*/
                    )
                }
            }
        }

        val textReview = remember {
            "${reviews.value.size} ${getAfterWord(reviews.value.size, WORD_REVIEW)}"
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
                            StarPanel(count = product.star)//, starSize = 24.dp, starHorzInterval = 8.dp)
                            DividerHorizontal(size = 4.dp)
                            Text(
                                text = getFormattedStar(product.star).replace(',', '.'),
                                color = ColorText,
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
                val lazyRowState = rememberLazyListState()
                val flingBehavior = rememberSnapFlingBehavior(lazyListState = lazyRowState)
                BoxWithConstraints(modifier = modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(TextFieldBg.copy(alpha = 0.3f))
                ) {
                    val width = this.maxWidth - 16.dp
                    LazyRow(
                        state = lazyRowState, flingBehavior = flingBehavior,
                        modifier = Modifier.fillMaxSize()/*modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(TextFieldBg.copy(alpha = 0.3f))*/
                    ) {
                        items(reviews.value) { item ->
                            ItemReview(item, width){
                                    dialogReview.comment     = it.comment
                                    dialogReview.username    = it.username
                                    dialogReview.countstar   = it.countstar
                                    dialogReview.date        = it.date
                                  // dialogReview.hasOverflow = it.hasOverflow
                            }
                        }
                    }
                }
                /*for (i in 0 until reviews.value.size) {
                    DividerVertical(size = 8.dp)
                    ItemReview(reviews.value[i])
                }*/

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
        // val interaction = remember { MutableInteractionSource() }
    val scrollState = rememberScrollState()
    Column(modifier = Modifier
        .fillMaxSize()
        .clickable(
            interactionSource = MutableInteractionSource(),
            indication = null
        ) {

        }
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
                        .clickable(
                            interactionSource = MutableInteractionSource(),
                            indication = null
                        ) {
                            viewBigImages.value = true
                        }
                        .clip(RoundedCornerShape(8.dp))
                        .size(size)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    ShowProductImages(
                        modifier = Modifier
                            .width(size - 32.dp)
                            .height(size - 32.dp)
                            .padding(all = 8.dp), reduce = false, product = product,
                        onChangeImage =  {
                        indexImage.value = it
                    }) {
                        viewBigImages.value = true
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
                    ShowReviews(Modifier.fillMaxWidth())
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