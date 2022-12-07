package com.training.shoplocal.screens.mainscreen

import androidx.compose.ui.platform.LocalDensity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.LinearGradient
import androidx.annotation.DrawableRes
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.node.modifierElementOf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.lifecycle.viewmodel.compose.viewModel
import com.training.shoplocal.*
import com.training.shoplocal.AppShopLocal.Companion.appContext
import com.training.shoplocal.R
import com.training.shoplocal.classes.Product
import com.training.shoplocal.classes.downloader.Callback
import com.training.shoplocal.classes.downloader.ImageLinkDownloader
import com.training.shoplocal.screens.appscreen.BottomSheet
import com.training.shoplocal.ui.theme.*
import com.training.shoplocal.viewmodel.RepositoryViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.IOException
import java.lang.Math.sqrt
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

private enum class IMAGE_STATE {
    NONE,
    PROCESS,
    COMPLETED,
    FAILURE
}

@Composable
fun AnimateLinkDownload(componentSize: Size) {
    if (componentSize.width > 0) {
        val dpSize = LocalDensity.current.run {
            componentSize.toDpSize()
        }
        val padding = LocalDensity.current.run {
            16.dp.toPx()
        }
        val heightGradient = kotlin.math.sqrt(componentSize.width * componentSize.width +
                    componentSize.height * componentSize.height) + padding
        val widthGradient  = 120f
        val infiniteTransition = rememberInfiniteTransition()
        val animatedPos by infiniteTransition.animateFloat(
            initialValue = -2*widthGradient,
            targetValue = componentSize.width + widthGradient,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 1000,
                    easing = FastOutLinearInEasing
                ),
                repeatMode = RepeatMode.Reverse
            )
        )
        Canvas(
            modifier = Modifier.size(dpSize)
        ) {
            rotate(degrees = 45f) {
                translate(animatedPos, -100f) {
                    drawRect(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color.White,
                                Color(0xFFDDDDDD),
                                Color.White
                            ),
                            startX = 0f,
                            endX   = widthGradient
                        ),
                        size = Size(widthGradient, heightGradient)
                    )
                }
            }
        }
    }
}

@Composable
fun DiscountPanel(modifier: Modifier, percent: Int){
    Card(modifier = modifier, backgroundColor = BgDiscount,
    shape = RoundedCornerShape(6.dp)
    ){
        Text(modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
            text = "-$percent%",
            fontSize = 11.sp,
            color = TextDiscount)
    }
}

@Composable
fun StarPanel(count: Float){
    val df = DecimalFormat("#.#")
    df.roundingMode = RoundingMode.HALF_EVEN
    //val rounded = df.format(count)
    val partNumber = df.format(count).split(DECIMAL_CEPARATOR)
    val intPart = partNumber[0].toInt()
    val floatPart = if (partNumber.size == 2) partNumber[1].toInt() else 0
    val starPart = intPart + 1

    val bm = BitmapFactory.decodeResource(appContext().resources, R.drawable.ic_star)
    Row(modifier = Modifier.padding(top = 2.dp),
    //    horizontalArrangement = Arrangement.spacedBy(1.dp)
    ) {
        for (i in 0..4) {
            val color = if (i <= intPart - 1)
                ImageStarOn
            else
                ImageStarOff
            Box{
            Image( modifier = Modifier.requiredSize(12.dp),
                bitmap = bm.asImageBitmap(),
                colorFilter = ColorFilter.tint(color),
                contentDescription = null
            )
            // < * Отрисовка части звезды
            if (i == starPart - 1 && floatPart > 0) {
                val part       = bm.width / 10f
                val widthStar  = floatPart * part
                val bmPart: Bitmap = Bitmap.createBitmap(bm, 0, 0, widthStar.toInt(), bm.height)
                Image(modifier = Modifier.height(12.dp),
                    bitmap = bmPart.asImageBitmap(),
                    colorFilter = ColorFilter.tint(ImageStarOn),
                    contentDescription = null
                )
            }
            // * >
            }


        }
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun CardProduct(product: Product, state: ModalBottomSheetState){
    val viewModel: RepositoryViewModel = viewModel()
    val brand: String = product.brand?.let { viewModel.getBrand(it) } ?: ""
    val scope = rememberCoroutineScope()
    @Composable
    fun ButtonMore(modifier: Modifier, action: ()-> Unit){
        Image(
            painter = painterResource(R.drawable.ic_more),
            contentDescription = null,
            contentScale = ContentScale.None,
            colorFilter = ColorFilter.tint(ImageButton),
            modifier = modifier
                .background(Color.White, CircleShape)
                .size(24.dp)
                .clip(CircleShape)
                .border(1.dp, BorderButton, CircleShape)
                .clickable {
                    scope.launch {
                        viewModel.setSelectedProduct(product)
                        state.show()
                    }
                }
        )
    }
    @Composable
    fun ButtonFavorite(modifier: Modifier, action: (checked: Boolean)-> Unit){
        val observeSelectedProduct = viewModel.selectedProduct.collectAsState()
        val isFavorite = remember {
            derivedStateOf {
                if (observeSelectedProduct.value.id != -1)
                    product.favorite > 0
                else {
                    if (product.id == observeSelectedProduct.value.id)
                        observeSelectedProduct.value.favorite > 0
                    else
                        product.favorite > 0
                }
            }
        }
        //log ("recomposition favorite")
    Image(
            painter = painterResource(R.drawable.ic_favorite),
            contentDescription = null,
            contentScale = ContentScale.None,
            colorFilter = ColorFilter.tint(Color.White),
            modifier = modifier
                .size(24.dp)
        )
        Image(
            painter = if (isFavorite.value) painterResource(R.drawable.ic_favorite)
            else painterResource(R.drawable.ic_favorite_border),
            contentDescription = null,
            contentScale = ContentScale.None,
            colorFilter = if (isFavorite.value) ColorFilter.tint(ImageFavoriteOn)
            else ColorFilter.tint(ImageFavoriteOff),
            modifier = modifier
                .clip(CircleShape)
                .size(24.dp)
                .clickable/*(interactionSource = remember { MutableInteractionSource() },
                           indication = rememberRipple(radius = 16.dp)) */{
                    val value: Byte = if (product.favorite > 0) 0 else 1
                    product.favorite = value//if (isFavorited.value) 1 else 0
                    action(value > 0)
                }
        )
    }

    fun getLinkImage(index: Int, images: List<String>?): String?{
        return images?.let{
            if (index < it.size)
                it[index]
            else
                null
        }
    }

    val countItems = product.linkimages?.size ?: 1 // у продукта должно быть хотя бы одно изображение
     val listImages = remember{Array<Pair<IMAGE_STATE, ImageBitmap>>(countItems) {
        IMAGE_STATE.NONE to EMPTY_IMAGE
    }.toMutableList()}
    val context = LocalContext.current
    val labelFont = FontFamily(Font(R.font.robotocondensed_light))
    val visible = remember{MutableTransitionState(false)}
    val animateSize = remember{mutableStateOf(Size.Zero)}
    val imageLink = getLinkImage(0, product.linkimages)
    val downloadedImage = remember{ mutableStateOf(
        (listImages[0].first == IMAGE_STATE.COMPLETED && !listImages[0].second.isEmpty()) || listImages[0].first == IMAGE_STATE.FAILURE
    ) }
    if (!downloadedImage.value) {
        // Запуск в области compose, если compose завершится. Блок внутри будет завершен без
        // утечки памяти и процессов.
        LaunchedEffect(true) {
            // Если не нужно уменьшать изображение,
            // используйте ImageLinkDownload.downloadImage вместо
            // ImageLinkDownload.downloadCardImage
             ImageLinkDownloader.downloadCardImage(
                imageLink?.let { "$SERVER_URL/images/$it" }, object : Callback {
                    override fun onComplete(image: Bitmap) {
                       // bitmap.value = image.asImageBitmap()
                        listImages[0] = IMAGE_STATE.COMPLETED to image.asImageBitmap()
                        downloadedImage.value = true
                    }

                    override fun onFailure() {
                        // здесь можно установить картинку по умолчанию,
                        // в случае если картинка не загрузилась
                        //listImages[0] = ваше изображение
                        listImages[0]  = IMAGE_STATE.FAILURE to EMPTY_IMAGE
                        downloadedImage.value = true
                    }
                })
        }
    } else
        visible.targetState = true
    Box(modifier = Modifier
        .width(150.dp)
        .padding(vertical = 10.dp))
        {
        Column(
        )
        {
            Card(modifier = Modifier
                .requiredSize(150.dp)
                .padding(bottom = 8.dp),
                backgroundColor = BgCard,
                shape = RoundedCornerShape(8.dp)
            ) {
                Box(modifier = Modifier
                    .padding(8.dp)
                    .onGloballyPositioned { coordinates ->
                        animateSize.value = coordinates.size.toSize()
                    },
                    contentAlignment = Alignment.Center
                    ) {
                    if (!downloadedImage.value) {
                        AnimateLinkDownload(animateSize.value)
                    }
                        androidx.compose.animation.AnimatedVisibility(
                            visibleState = visible,
                            enter = fadeIn(
                                animationSpec = tween(
                                    durationMillis = 150,
                                    easing = LinearEasing
                                )
                            )
                        ) {
                            val lazyRowState = rememberLazyListState()
                            val needDownloadImages = remember {
                                derivedStateOf {
                                                (countItems > 1
                                                && (lazyRowState.firstVisibleItemIndex == 0
                                                    && lazyRowState.firstVisibleItemScrollOffset > 0
                                                    && lazyRowState.isScrollInProgress))
                                }
                            }
                            val uploaded = remember {
                                derivedStateOf {
                                    countItems > 1 && (lazyRowState.firstVisibleItemIndex > 0
                                            || lazyRowState.firstVisibleItemScrollOffset > 0)
                                }
                            }
                            if (needDownloadImages.value || uploaded.value) {
                                    product.linkimages?.let { items ->
                                        for (i in 1 until countItems) {
                                            if (listImages[i].first == IMAGE_STATE.NONE) {
                                                val itemImageLink = getLinkImage(i, items)
                                                listImages[i] = IMAGE_STATE.PROCESS to EMPTY_IMAGE
                                                ImageLinkDownloader.downloadCardImage(
                                                    "$SERVER_URL/images/$itemImageLink",
                                                    object : Callback {
                                                        override fun onComplete(image: Bitmap) {
                                                            log ("product ${product.id}, loaded image $i")
                                                            listImages[i] =
                                                                IMAGE_STATE.COMPLETED to image.asImageBitmap()
                                                        }

                                                        override fun onFailure() {
                                                            listImages[i] =
                                                                IMAGE_STATE.FAILURE to EMPTY_IMAGE
                                                        }
                                                    }
                                                )
                                            }
                                        }
                                    }
                            }
                            val flingBehavior = rememberSnapFlingBehavior(lazyListState = lazyRowState)
                            LazyRow(state = lazyRowState, modifier = Modifier.fillMaxSize(),
                                horizontalArrangement = Arrangement.Center,
                                flingBehavior = flingBehavior
                            ) {
                                listImages.forEach {item ->
                                    item {
                                        Image(
                                            modifier = Modifier
                                                .fillParentMaxSize()
                                                .padding(all = 8.dp),
                                            bitmap = item.second,//bitmap.value,
                                            contentDescription = null
                                        )
                                    }
                                }
                            }
                    }
                    DiscountPanel(modifier = Modifier.align(Alignment.BottomStart), percent = product.discount)
                    ButtonMore(modifier = Modifier.align(Alignment.BottomEnd)
                    ) {
                        log("click")
                    }
                    ButtonFavorite(modifier = Modifier.align(Alignment.TopEnd)
                    ) {
                        //log("setProductFavorite")
                        viewModel.setProductFavorite(product.id, it)
                    }
                }
            }
            // < * Text Price
                Card(
                    modifier = Modifier.padding(top = 4.dp),
                    backgroundColor = BgTextPrice,
                    shape = RoundedCornerShape(6.dp)
                ){
                    Text(modifier = Modifier.padding(horizontal = 4.dp),
                        fontSize = 15.sp,
                        text = getSalePrice(product.price, product.discount),
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrice)
                }
                Text(modifier = Modifier.padding(top = 4.dp),
                    fontSize = 14.sp,
                    text = getFormattedPrice(product.price),
                    fontWeight = FontWeight.SemiBold,
                    style = TextStyle(textDecoration = TextDecoration.LineThrough),
                    color = TextPriceDiscount)
            val promostr: String =
                if (product.star > 4)
                    getStringResource(R.string.text_bestseller)
                else if (product.discount > 0)
                    getStringResource(R.string.text_action)
                else
                    ""
            if (promostr.isNotEmpty())
                Text(promostr,
                    fontSize = 14.sp,
                    color = TextPromotion)

            if (brand.isNotEmpty())
            Text(brand,
                color = TextBrand)
            // * >
            // < * Text Description
            Text(product.name,
                fontFamily = labelFont,
                color = TextDescription,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            // * >
            StarPanel(product.star)
        }
    }

}