package com.training.shoplocal.screens.mainscreen.composable

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.*
import com.training.shoplocal.classes.*
import com.training.shoplocal.classes.downloader.Callback
import com.training.shoplocal.classes.downloader.ExtBitmap
import com.training.shoplocal.classes.downloader.ImageLinkDownloader
import com.training.shoplocal.isEmpty
import com.training.shoplocal.log
import com.training.shoplocal.screens.mainscreen.MainScreen
import com.training.shoplocal.ui.theme.PrimaryDark
import com.training.shoplocal.ui.theme.TextFieldBg
import com.training.shoplocal.ui.theme.TextFieldFont
import kotlinx.coroutines.*
import okhttp3.Dispatcher
import kotlin.math.absoluteValue
import kotlin.math.pow


/*@Composable
fun ExtImage(modifier: Modifier, bitmap: State<ImageBitmap>){
   /* val source = remember {
        bitmap
    }*/
    log("recomposition image...")
    Image(
        modifier = modifier,
        bitmap = bitmap.value,
        contentDescription = null
    )
}*/


@Composable
fun CompositeButton(modifier: Modifier = Modifier, color: Color = PrimaryDark, top: @Composable () -> Unit, bottom: @Composable () -> Unit, onClick: (() -> Unit)? = null){
    Box(modifier = modifier
        .clip(RoundedCornerShape(6.dp))
        .background(color)
        .clickable(
            interactionSource = MutableInteractionSource(),
            indication = if (onClick == null) null else LocalIndication.current
        ) {
            onClick?.invoke()
        },
        contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
                Spacer(modifier = Modifier.weight(1f))
                top()
                bottom()
                Spacer(modifier = Modifier.weight(1f))
            }
    }
}


@Composable
fun DividerHorizontal(size: Dp){
    Spacer(modifier = Modifier.width(size))
}

@Composable
fun DividerVertical(size: Dp){
    Spacer(modifier = Modifier.height(size))
}


@Composable
fun TextPanel(text: String, textColor: Color, backgroundColor: Color, fontSize: TextUnit = 13.sp ) {
    Box(modifier = Modifier
        .clip(RoundedCornerShape(6.dp))
        .background(backgroundColor),
        contentAlignment = Alignment.Center
    ){
        Text(modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp), text = text,
            color = textColor,
            fontSize = fontSize
        )
    }
}


@Composable
fun ShowImagesIndicator(modifier: Modifier, index: State<Int>, count: Int){
   // val STATUS_NONE = -1
   /* val STATUS_SELECTED = 1
    val STATUS_DESELECTED = 0*/

   /* data class IndexImage(var value: Int = -1) // если захочется анимировать
    val curIndex= remember { // хранит предыдущее значение индекса
        IndexImage()
    }*/
    @Composable
    fun AnimateSymbol(selected: Boolean = false) {
        val selectedColor = remember {TextFieldFont.copy(alpha = 0.7f)}
        val symIndicator = remember {"●"}
        val animateColor by animateColorAsState(
            if (selected) selectedColor else TextFieldBg,
            animationSpec = tween(
                durationMillis = 350,
                easing = LinearEasing
            )
        )
        Box(modifier = Modifier.padding(horizontal = 1.dp)){
            Text(text = symIndicator,
                color = animateColor,
                fontSize = 13.sp
            )
       }
    }
    Row(modifier = modifier,
        verticalAlignment = Alignment.CenterVertically){
        for (i in 0 until count)
            AnimateSymbol(index.value == i)
    }

}

private enum class Status {
    NONE,
    LOADING,
    COMPLETE,
    FAIL
}
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ShowProductImages(modifier: Modifier, product: Product, reduce: Boolean, startIndex: State<Int> = mutableStateOf(0), onLoadImage:((index: Int, image: ImageBitmap) -> Unit)? = null, onChangeImage: ((index: Int) -> Unit)? = null) {
    @Composable
    fun ProgressDownloadImage(size: Size) {
        if (size.width > 0) {
            val dpSize = LocalDensity.current.run {
                size.toDpSize()
            }
            val padding = LocalDensity.current.run {
                16.dp.toPx()
            }
            val heightGradient = kotlin.math.sqrt(
                size.width.pow(2) +
                        size.height.pow(2)
            ) + padding
            val widthGradient = 120f
            val delta = (heightGradient - size.height) / 2
            val infiniteTransition = rememberInfiniteTransition()
            val animatedPos by infiniteTransition.animateFloat(
                initialValue = -widthGradient - delta,
                targetValue = size.width + delta,
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
                    translate(animatedPos, -delta) {
                        drawRect(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color.White,
                                    Color(0xFFDDDDDD),
                                    Color.White
                                ),
                                startX = 0f,
                                endX = widthGradient
                            ),
                            size = Size(widthGradient, heightGradient)
                        )
                    }
                }
            }
        }
    }

    data class ImageStatus(
        val id: Int,
        var link: String,
        var image: State<ImageBitmap> = mutableStateOf(EMPTY_IMAGE),
        var status: Status = Status.NONE
    )
    /* val currentProduct = remember {
        product
    }*/
    val linkImages = remember {
        val entries = mutableListOf<ImageStatus>()
        product.linkimages?.forEachIndexed { index, it ->
            entries.add(ImageStatus(id = index, link = it))
        }
        entries
    }
    var downloadedMainImage by remember {
        mutableStateOf(false)
    }

    /* DisposableEffect(Unit){
        onDispose {
            linkImages.clear()
        }
    }*/

    @Composable
    fun downloadImage(indexLink: Int): State<ImageBitmap> {
        /*val isMainImage = remember{
            index == 0
        }*/
        val index = remember {
            indexLink
        }
        /*  fun checkMainImage(){
            if (index == 0 ) {
             //   log("download main image ${linkImages[index].link}")
                downloadedMainImage = true
            }
        }*/


        val downloadedImage = remember {
            mutableStateOf(ImageBitmap(1, 1, hasAlpha = true, config = ImageBitmapConfig.Argb8888))
        }

        val linkImage = remember {
            linkImages[index]
        }

        LaunchedEffect(index) {
            fun checkMainImage() {
                if (index == 0)
                    downloadedMainImage = true
            }
            linkImage.status = Status.LOADING
            ImageLinkDownloader.downloadImage(
                "$SERVER_URL/images/${linkImage.link}",
                reduce,
                callback = object : Callback {
                    override fun onComplete(image: ExtBitmap) {
                        image.bitmap?.let {
                            linkImage.status = Status.COMPLETE
                            val imageBitmap = it.asImageBitmap()
                            downloadedImage.value = imageBitmap//it.asImageBitmap()
                            onLoadImage?.invoke(index, imageBitmap)

                            // linkImage.image = downloadedImage
                            //      log("complete ${linkImage.link}")
                            checkMainImage()
                        }
                    }

                    override fun onFailure() {
                        linkImage.status = Status.FAIL
                        downloadedImage.value = EMPTY_IMAGE
                        checkMainImage()
                        // log("fail ${linkImage.link}")
                    }
                }
            )
        }

        return downloadedImage
    }

    val lazyRowState = rememberLazyListState()
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = lazyRowState)
    var size by remember {
        mutableStateOf(Size.Zero)
    }


    LaunchedEffect(startIndex.value) {
        MainScope().launch {
            lazyRowState.scrollToItem(startIndex.value)
        }
    }


    DisposableEffect(Unit){
        onDispose {
            linkImages.clear()
        }
    }



    linkImages.forEachIndexed { index, item ->
        if (item.status == Status.NONE || item.status == Status.FAIL)
            item.image = downloadImage(index)
    }

    Box(
        modifier = modifier
            .background(Color.White)
            .onGloballyPositioned { coordinates ->
                size = coordinates.size.toSize()
            }
    ) {
        LazyRow(
            state = lazyRowState,
            horizontalArrangement = Arrangement.Center,
            flingBehavior = flingBehavior
        ) {


            items(linkImages, {linkimage -> linkimage.id}) { item ->
            //items(linkImages) { item ->
                //if (item.status == Status.COMPLETE)
                if (!item.image.value.isEmpty())
                Image(
                    modifier = Modifier
                        .fillParentMaxSize()
                        .padding(all = 8.dp),
                    bitmap = item.image.value,
                    contentDescription = null
                )
            }
        }

        if (!downloadedMainImage)
             Box(modifier = Modifier
                 //.clipToBounds()
                 .fillMaxSize()
                 .background(Color.White)) {
                ProgressDownloadImage(size)
             }
        onChangeImage?.let {changeImage ->
            val indexImage = remember {
                derivedStateOf {
                    var index = lazyRowState.firstVisibleItemIndex
                    val half = lazyRowState.layoutInfo.viewportSize / 2
                    val offset = (lazyRowState.layoutInfo.visibleItemsInfo.firstOrNull()?.offset
                        ?: 0).absoluteValue
                    if (lazyRowState.layoutInfo.visibleItemsInfo.isNotEmpty())
                        if (offset > half.width) {
                            index += 1
                        }
                    index
                }
            }
            LaunchedEffect(indexImage.value) {
                changeImage(indexImage.value)
            }
        }
    }
}