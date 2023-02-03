package com.training.shoplocal.screens.mainscreen

import androidx.compose.ui.platform.LocalDensity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.lifecycle.viewmodel.compose.viewModel
import com.training.shoplocal.*
import com.training.shoplocal.AppShopLocal.Companion.appContext
import com.training.shoplocal.R
import com.training.shoplocal.classes.DECIMAL_SEPARATOR
import com.training.shoplocal.classes.EMPTY_IMAGE
import com.training.shoplocal.classes.Product
import com.training.shoplocal.classes.SERVER_URL
import com.training.shoplocal.classes.downloader.Callback
import com.training.shoplocal.classes.downloader.ExtBitmap
import com.training.shoplocal.classes.downloader.ImageLinkDownloader
import com.training.shoplocal.classes.fodisplay.VIEW_MODE
import com.training.shoplocal.ui.theme.*
import com.training.shoplocal.viewmodel.RepositoryViewModel
import kotlinx.coroutines.launch
import java.math.RoundingMode
import java.text.DecimalFormat

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
    val partNumber = df.format(count).split(DECIMAL_SEPARATOR)
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

data class ImageLink(val link: String, val md5: String)

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun CardProduct(product: Product, showMoreButton: Boolean = true, state: ModalBottomSheetState, modeview: VIEW_MODE = VIEW_MODE.CARD) {
    val mode_View       = rememberUpdatedState(newValue = modeview)
    val show_MoreButton = rememberUpdatedState(newValue = showMoreButton)
    val cardproduct = remember {
        product
    }
    val CARD_SIZE = if (mode_View.value == VIEW_MODE.CARD) 150 else 100
    val linkImages: List<ImageLink> = remember {
        val list = mutableListOf<ImageLink>()
        product.linkimages?.forEach {
            list.add(ImageLink(it, md5(it)))
        }
       // log("links = ${list.toString()}")
        list.toList()
    }
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
                        viewModel.setSelectedProduct(cardproduct)
                        //log("card favorite ${cardproduct.favorite}")
                        state.show()
                    }
                }
        )
    }
    @Composable
    fun ButtonFavorite(modifier: Modifier){

      /*  var checked by remember {
            mutableStateOf(cardproduct.favorite > 0)
        }*/
        val observeSelectedProduct = viewModel.selectedProduct.collectAsState()
        val isfavorite = remember {
            /*derivedStateOf {
                if (observeSelectedProduct.value.id != -1)
                    product.favorite > 0
                else {
                    if (product.id == observeSelectedProduct.value.id)
                        observeSelectedProduct.value.favorite > 0
                    else
                        product.favorite > 0
                }
            }*/
            derivedStateOf {
                if (cardproduct.id == observeSelectedProduct.value.id) {
                    cardproduct.favorite = observeSelectedProduct.value.favorite
                    observeSelectedProduct.value.favorite > 0
                }
                else
                    cardproduct.favorite > 0
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
            //painter = if (isFavorite.value) painterResource(R.drawable.ic_favorite)
            painter = if (isfavorite.value) painterResource(R.drawable.ic_favorite)
                else painterResource(R.drawable.ic_favorite_border),
            contentDescription = null,
            contentScale = ContentScale.None,
            colorFilter = if (isfavorite.value) ColorFilter.tint(ImageFavoriteOn)
            //if (isFavorite.value) ColorFilter.tint(ImageFavoriteOn)
            else ColorFilter.tint(ImageFavoriteOff),
            modifier = modifier
                .clip(CircleShape)
                .size(24.dp)
                .clickable/*(interactionSource = remember { MutableInteractionSource() },
                           indication = rememberRipple(radius = 16.dp)) */{
                    //val value: Byte = if (cardproduct.favorite > 0) 0 else 1
                    cardproduct.favorite = if (cardproduct.favorite > 0) 0 else 1
                    //product.favorite = value//if (isFavorited.value) 1 else 0
                    //val checked = cardproduct.favorite > 0
                    //log("checked = $checked")
                    //viewModel.setSelectedProduct(cardproduct)
                    viewModel.setProductFavorite(cardproduct)
                    // action(checked)
                }
        )
    }

/*    fun getLinkImage(index: Int, images: List<String>?): String?{
        return images?.let{
            if (index < it.size)
                it[index]
            else
                null
        }
    }*/

    fun getLinkImage(index: Int): ImageLink?{
        return linkImages.let{
            if (index < it.size)
                it[index]
            else
                null
        }
    }

    val countItems = product.linkimages?.size ?: 1 // у продукта должно быть хотя бы одно изображение
    val listImages = remember{
        Array<Pair<IMAGE_STATE, ImageBitmap>>(countItems) {
        IMAGE_STATE.NONE to EMPTY_IMAGE
    }}
    //val context = LocalContext.current
    val labelFont = FontFamily(Font(R.font.robotocondensed_light))
    val visible = remember{MutableTransitionState(false)}
    val animateSize = remember{mutableStateOf(Size.Zero)}
    val imageLink = getLinkImage(0)//, product.linkimages)

    val downloadedImage = remember {
        mutableStateOf(
            (listImages[0].first == IMAGE_STATE.COMPLETED && !listImages[0].second.isEmpty()) || listImages[0].first == IMAGE_STATE.FAILURE
        )
    }


    /*val downloadedImage = remember{ mutableStateOf(
        (listImages[0].first == IMAGE_STATE.COMPLETED && !listImages[0].second.isEmpty()) || listImages[0].first == IMAGE_STATE.FAILURE
    ) }*/


    // Вызывается при старте композиции один раз, выполняется блок,
    // в случае рекомпозиции: при измененнии key или уничтожения композиции блок не выполняется,
    // а выполняется OnDispose()
    DisposableEffect(Unit) {
        //log("effect product id = ${product.id}")
        onDispose {
          //  log("product id = ${product.id} dispose")
            for (i in listImages.indices)
              listImages[i] = IMAGE_STATE.NONE to EMPTY_IMAGE
        }
    }

        //log("downloadedImage = ${downloadedImage.value}")
    //log("product id = ${product.id} -> status ${listImages[0].first}")
    if (!downloadedImage.value) {
        // Запуск в области compose, если compose завершится. Блок внутри будет завершен без
        // утечки памяти и процессов.
        LaunchedEffect(Unit) {
            // Если не нужно уменьшать изображение,
            // используйте ImageLinkDownload.downloadImage вместо
            // ImageLinkDownload.downloadCardImage

           /* imageLink?.let{
                log(it)
            }*/

             ImageLinkDownloader.downloadCardImage(
                 imageLink?.let { "$SERVER_URL/images/${it.link}" },
                 object : Callback {
                    override fun onComplete(image: ExtBitmap) {
                        //log("загружено из ${image.source.name}")
                        listImages[0] = IMAGE_STATE.COMPLETED to image.bitmap!!.asImageBitmap()
                        downloadedImage.value = true
                    }

                    override fun onFailure() {
                       // log("product id = ${product.id} failure")
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
    Box(
        modifier = Modifier
            .width(CARD_SIZE.dp)
            /* .onGloballyPositioned { coordinates ->
            heightCard = coordinates.size.height
          //  log ("height = ${heightCard.Dp}")
                //animateSize.value = coordinates.size.toSize()
        }*/
            .padding(vertical = 10.dp))
        {


        Column(){
            Card(modifier = Modifier
                .size(CARD_SIZE.dp)
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
                    //val existCache = viewModel.existImageCache(imageLink)
//                    if (!downloadedImage.value && !existCache) {
                    val showDownloadProcess = remember {
                        derivedStateOf {
                            !downloadedImage.value && !viewModel.existImageCache(imageLink?.md5)
                        }
                    }
                    //if (!downloadedImage.value) {
                    if (showDownloadProcess.value)
                        AnimateLinkDownload(animateSize.value)


                        androidx.compose.animation.AnimatedVisibility(
                            visibleState = visible,
                            enter = fadeIn(
                                animationSpec = tween(
                                    durationMillis = if (!showDownloadProcess.value) 0 else 100,
                                    easing = LinearEasing
                                )
                            )
                        ) {
                            val lazyRowState = rememberLazyListState()
                          /*  val needDownloadImages = remember {
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
                            //if (needDownloadImages.value || uploaded.value) {*/
                            LaunchedEffect(Unit){
                            //if (needDownloadImages.value) {
                               // val str = product.linkimages?.get(0) ?: ""
                               // log("need download $str}")
                                    product.linkimages?.let { items ->
                                        for (i in 1 until countItems) {
                                            if (listImages[i].first == IMAGE_STATE.NONE) {
                                                val itemImageLink = getLinkImage(i)//, items)
                                                listImages[i] = IMAGE_STATE.PROCESS to EMPTY_IMAGE
                                                ImageLinkDownloader.downloadCardImage(
                                                    "$SERVER_URL/images/${itemImageLink?.link}",
                                                    object : Callback {
                                                        override fun onComplete(image: ExtBitmap) {
                                                          //  log ("product ${product.id}, loaded image $i")
                                                            listImages[i] =
                                                                IMAGE_STATE.COMPLETED to image.bitmap!!.asImageBitmap()
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
                    if (show_MoreButton.value) {
                        ButtonMore(
                            modifier = Modifier.align(Alignment.BottomEnd)
                        ) {
                            log("click")
                        }
                    }

                    ButtonFavorite(modifier = Modifier.align(Alignment.TopEnd)
                    )/* {
                        //log("setProductFavorite")
                        viewModel.setProductFavorite(product.id, it)
                    }*/
                }
            }


            /*Text(modifier = Modifier.padding(top = 4.dp),
                text = product.id.toString(),
                fontSize = 23.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPriceDiscount)*/

            // < * Text Price
            Row(modifier = Modifier
                .fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Card(
                        modifier = Modifier.padding(top = 4.dp),
                        backgroundColor = BgTextPrice,
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            modifier = Modifier.padding(horizontal = 4.dp),
                            fontSize = 15.sp,
                            text = getSalePrice(product.price, product.discount),
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrice
                        )
                    }
                    Text(
                        modifier = Modifier.padding(top = 4.dp),
                        fontSize = 14.sp,
                        text = getFormattedPrice(product.price),
                        fontWeight = FontWeight.SemiBold,
                        style = TextStyle(textDecoration = TextDecoration.LineThrough),
                        color = TextPriceDiscount
                    )
                    /*val promostr: String =
                        if (product.star >= 4)
                            getStringResource(R.string.text_bestseller)
                        else if (product.discount > 0)
                            getStringResource(R.string.text_action)
                        else
                            ""
                    if (promostr.isNotEmpty())
                        Text(promostr,
                            fontSize = 14.sp,
                            color = TextPromotion)*/

                }

                Box(
                    Modifier
                        //.background(Color.Red)
                        //   .border(1.dp, TextFieldBg, CircleShape)
                        .background(PrimaryDark, CircleShape)
                        .clip(CircleShape)
                        .size(32.dp)
                        .clickable { },
                ) {
                    Image(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(18.dp)
                            //.padding(all = 4.dp)
                            //.size(44.dp)
                            /*.clickable(
                                /* interactionSource = interactionSource,
                            indication = null*/
                            ) {

                            }*/,
                        imageVector = ImageVector.vectorResource(R.drawable.ic_addcart),
                        colorFilter = ColorFilter.tint(SelectedItemBottomNavi),
                        contentDescription = null
                    )
                }
            }

            val promostr: String =
                if (product.star >= 4)
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

          /*  val boxScope = this
            LaunchedEffect(Unit) {
                heightConstraints = boxScope.maxHeight
                log("height card = $heightConstraints")
            }*/

            //log ("height = $cardHeight")*/
    }
 }