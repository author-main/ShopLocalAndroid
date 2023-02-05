package com.training.shoplocal.screens.mainscreen

import androidx.compose.ui.platform.LocalDensity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.DiscretePathEffect
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
import androidx.compose.ui.Alignment.Companion.CenterVertically
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
import androidx.compose.ui.res.stringResource
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
import com.training.shoplocal.classes.*
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
    if (percent > 0) {
        Card(
            modifier = modifier, backgroundColor = BgDiscount,
            shape = RoundedCornerShape(6.dp)
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                text = "-$percent%",
                fontSize = 11.sp,
                color = TextDiscount
            )
        }
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
    fun isCardModeView() =
        mode_View.value == VIEW_MODE.CARD
    val fontsize = if (isCardModeView()) 14.sp else 13.sp
    val fontSizeDescription = 15.sp//if (isCardModeView()) 16.sp else 15.sp
    val CARD_SIZE = if (isCardModeView()) 150 else 110
    val linkImages: List<ImageLink> = remember {
        val list = mutableListOf<ImageLink>()
        cardproduct.linkimages?.forEach {
            list.add(ImageLink(it, md5(it)))
        }
       // log("links = ${list.toString()}")
        list.toList()
    }
    val viewModel: RepositoryViewModel = viewModel()
    val brand: String = product.brand?.let { viewModel.getBrand(it) } ?: ""
    val scope = rememberCoroutineScope()

    @Composable
    fun ButtonMore(modifier: Modifier){//}, action: ()-> Unit){
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
        val observeSelectedProduct = viewModel.selectedProduct.collectAsState()
        val isfavorite = remember {
            derivedStateOf {
                if (cardproduct.id == observeSelectedProduct.value.id) {
                    cardproduct.favorite = observeSelectedProduct.value.favorite
                    observeSelectedProduct.value.favorite > 0
                }
                else
                    cardproduct.favorite > 0
            }
        }
        Box(modifier = modifier) {
            if (isCardModeView()){
                Image(
                painter = painterResource(R.drawable.ic_favorite),
                contentDescription = null,
                contentScale = ContentScale.None,
                colorFilter = ColorFilter.tint(Color.White),
                modifier = modifier
                    .size(24.dp)
            )}
            Image(
                painter = if (isfavorite.value) painterResource(R.drawable.ic_favorite)
                else painterResource(R.drawable.ic_favorite_border),
                contentDescription = null,
                contentScale = ContentScale.None,
                colorFilter = if (isfavorite.value) ColorFilter.tint(ImageFavoriteOn)
                else ColorFilter.tint(ImageFavoriteOff),
                modifier = modifier
                    .clip(CircleShape)
                    .size(24.dp)
                    .clickable/*(interactionSource = remember { MutableInteractionSource() },
                           indication = rememberRipple(radius = 16.dp)) */{
                        cardproduct.favorite = if (cardproduct.favorite > 0) 0 else 1
                        viewModel.setProductFavorite(cardproduct)
                    }
            )
        }
    }


    @Composable
    fun ProductImages(){
        fun getLinkImage(index: Int): ImageLink?{
            return linkImages.let{
                if (index < it.size)
                    it[index]
                else
                    null
            }
        }
//**************************************************************************************************
        val countItems = remember {cardproduct.linkimages?.size ?: 1} // у продукта должно быть хотя бы одно изображение
        val listImages = remember{
            Array<Pair<IMAGE_STATE, ImageBitmap>>(countItems) {
                IMAGE_STATE.NONE to EMPTY_IMAGE
            }}
        //val context = LocalContext.current

        val visible = remember{MutableTransitionState(false)}
        val animateSize = remember{mutableStateOf(Size.Zero)}
        val imageLink = getLinkImage(0)//, product.linkimages)


      /*  val downloadedImage = remember {
            mutableStateOf(false)
        }
*/
       /* val downloadedImage = remember {
            mutableStateOf(false)
           /* mutableStateOf(
                (listImages[0].first == IMAGE_STATE.COMPLETED && !listImages[0].second.isEmpty()) || listImages[0].first == IMAGE_STATE.FAILURE
            )*/
        }*/

        /*val isDownload = remember {
            derivedStateOf {
                (listImages[0].first == IMAGE_STATE.COMPLETED && !listImages[0].second.isEmpty())
                        || listImages[0].first == IMAGE_STATE.FAILURE
            }
        }*/




        /*val downloadedImage = remember{ mutableStateOf(
            (listImages[0].first == IMAGE_STATE.COMPLETED && !listImages[0].second.isEmpty()) || listImages[0].first == IMAGE_STATE.FAILURE
        ) }*/


        // Вызывается при старте композиции один раз, выполняется блок,
        // в случае рекомпозиции: при измененнии key или уничтожения композиции блок не выполняется,
        // а выполняется OnDispose()
        DisposableEffect(Unit) {
            onDispose {
                for (i in listImages.indices)
                    listImages[i] = IMAGE_STATE.NONE to EMPTY_IMAGE
            }
        }


        val isDownloadImage = remember {
            derivedStateOf {
              //  val loaded =
                listImages[0].first == IMAGE_STATE.COMPLETED && !listImages[0].second.isEmpty()// || listImages[0].first == IMAGE_STATE.FAILURE
              //  log("is download image = $loaded")
              //  loaded
            }
        }


//        if (!downloadedImage.value) {
        if (!isDownloadImage.value) {

            // Запуск в области compose, если compose завершится. Блок внутри будет завершен без
            // утечки памяти и процессов.
            LaunchedEffect(Unit) {
                // Если не нужно уменьшать изображение,
                // используйте ImageLinkDownload.downloadImage вместо
                // ImageLinkDownload.downloadCardImage
                //    if (!isDownload.value) {
                //log ("not download")
                ImageLinkDownloader.downloadCardImage(
                    imageLink?.let { "$SERVER_URL/images/${it.link}" },
                    object : Callback {
                        override fun onComplete(image: ExtBitmap) {
                            listImages[0] =
                                IMAGE_STATE.COMPLETED to image.bitmap!!.asImageBitmap()
                           // downloadedImage.value = true
                           /* log("card product ${cardproduct.id}")
                            log("${imageLink?.link} load image true")*/
                            visible.targetState = true
                        }

                        override fun onFailure() {
                            // здесь можно установить картинку по умолчанию,
                            // в случае если картинка не загрузилась
                            //listImages[0] = ваше изображение
                            listImages[0] = IMAGE_STATE.FAILURE to EMPTY_IMAGE
                            //downloadedImage.value = false//true
                          //  log("load image false")
                        }
                    })

            }

        } /*else
            visible.targetState = true*/

        Card(modifier = Modifier
            .requiredSize(CARD_SIZE.dp),
           // elevation = 3.dp,
            backgroundColor = BgCard,
           // border = BorderStroke(3.dp, PrimaryDark),
            shape = RoundedCornerShape(16.dp)
        ) {
            Box(modifier = Modifier
                .padding(8.dp)
                .onGloballyPositioned { coordinates ->
                    animateSize.value = coordinates.size.toSize()
                },
                contentAlignment = Alignment.Center
            ) {
                val showDownloadProcess = remember {
                    derivedStateOf {
                        //!downloadedImage.value && !viewModel.existImageCache(imageLink?.md5)
                        !isDownloadImage.value && !viewModel.existImageCache(imageLink?.md5)
                    }
                }
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

                    LaunchedEffect(Unit){
                        cardproduct.linkimages?.let { items ->
                            for (i in 1 until countItems) {
                                if (listImages[i].first == IMAGE_STATE.NONE) {
                                    val itemImageLink = getLinkImage(i)//, items)
                                    listImages[i] = IMAGE_STATE.PROCESS to EMPTY_IMAGE
                                    ImageLinkDownloader.downloadCardImage(
                                        "$SERVER_URL/images/${itemImageLink?.link}",
                                        object : Callback {
                                            override fun onComplete(image: ExtBitmap) {
//                                                log ("product ${product.id}, loaded image $i")
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
                //if (isCardModeView())
                DiscountPanel(modifier = Modifier.align(if (isCardModeView()) Alignment.BottomStart else Alignment.TopStart), percent = cardproduct.discount)
                if (show_MoreButton.value) {
                    ButtonMore(
                        modifier = Modifier.align(Alignment.BottomEnd)
                    )
                }
                if (isCardModeView())
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






//**************************************************************************************************
    }

    @Composable
    fun DiscountText(){
        Card(
            modifier = Modifier.padding(top = 4.dp),
            backgroundColor = BgTextPrice,
            shape = RoundedCornerShape(6.dp)
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 4.dp),
                fontSize = 15.sp,
                text = getSalePrice(cardproduct.price, cardproduct.discount),
                fontWeight = FontWeight.SemiBold,
                color = TextPrice
            )
        }
    }

    @Composable
    fun PriceText(){
        fun isDiscount() = cardproduct.discount > 0
            Text(
                modifier = Modifier.padding(top = 4.dp),
                fontSize = fontsize,
                text = if (isDiscount()) getFormattedPrice(cardproduct.price) else stringResource(
                    id = R.string.text_nodiscounts
                ),

              //  fontWeight = FontWeight.SemiBold,
                style = TextStyle(textDecoration = if (isDiscount()) TextDecoration.LineThrough else TextDecoration.None),
                color = TextPriceDiscount
            )
    }

    @Composable
    fun CartButton(modifier: Modifier){
        Box(
            modifier//Modifier
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
                    .size(18.dp),
                imageVector = ImageVector.vectorResource(R.drawable.ic_addcart),
                colorFilter = ColorFilter.tint(SelectedItemBottomNavi),
                contentDescription = null
            )
        }
    }




    @Composable
    fun ActionText(){
        val promostr: String =
            if (cardproduct.star >= 4)
                getStringResource(R.string.text_bestseller)
            else if (cardproduct.discount > 0)
                getStringResource(R.string.text_action)
            else
                ""
        if (promostr.isNotEmpty())
            Text(promostr,
                fontSize = fontsize,
                color = TextPromotion)

    }
    @Composable
    fun BrendText(){
        if (brand.isNotEmpty())
            Text(brand,
                fontSize = fontsize,
                color = TextBrand)
    }

    @Composable
    fun DescriptionText(){
        Text(//modifier = Modifier.background(Color.Red),
            text = cardproduct.name,
            fontFamily = FontFamily(Font(R.font.robotocondensed_light)),
            fontSize = fontSizeDescription,
            color = TextDescription,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }




  /*  Box(
        modifier = Modifier
            .width(CARD_SIZE.dp)
            /* .onGloballyPositioned { coordinates ->
            heightCard = coordinates.size.height
          //  log ("height = ${heightCard.Dp}")
                //animateSize.value = coordinates.size.toSize()
        }*/
            .padding(vertical = 10.dp))
        {*/
        if (isCardModeView()) {
            Column(
                modifier = Modifier
                    .width(CARD_SIZE.dp)
                    .padding(vertical = 10.dp)
            ) {
                ProductImages()
                Spacer(modifier = Modifier
                    .height(8.dp)
                    .fillMaxWidth())
                Row(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        DiscountText()
                        PriceText()
                    }
                    CartButton(Modifier.align(Alignment.Bottom))
                }
                ActionText()
                BrendText()
                DescriptionText()
                StarPanel(cardproduct.star)
            }
        } else { // VIEW_MODE.ROW
            //if (mode_View.value == VIEW_MODE.ROW) {
            Column() {
                Row(
                    Modifier
                        //.background(Color.Red)
                        .fillMaxWidth()
                        //    .height(CARD_SIZE.dp)
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                ) {
                    ProductImages()

                    Column(
                        Modifier
                            .weight(1f)
                            .padding(start = 8.dp)
                    ) {
                        DescriptionText()
                        Row() {
                            Column(Modifier.weight(1f)) {
                                ActionText()
                                BrendText()
                            }
                            /*DiscountPanel(modifier = Modifier.align(CenterVertically),
                                percent = cardproduct.discount)*/
                            ButtonFavorite(modifier = Modifier.align(CenterVertically))
                            Spacer(modifier = Modifier.width(4.dp))
                        }

                        Row() {
                            Column(Modifier.weight(1f)) {
                                StarPanel(cardproduct.star)
                                Row() {
                                    DiscountText()
                                    Spacer(modifier = Modifier.width(4.dp))
                                    PriceText()
                                }
                            }
                            CartButton(Modifier.align(Alignment.Bottom))
                        }

                    }
                }
                 Spacer(modifier = Modifier
                     .height(1.dp)
                     .fillMaxWidth()
                     .background(PrimaryDark))
            }
            }
//    }
 }