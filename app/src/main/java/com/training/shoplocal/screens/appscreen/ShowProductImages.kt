package com.training.shoplocal.screens.appscreen

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.training.shoplocal.classes.EMPTY_IMAGE
import com.training.shoplocal.classes.Product
import com.training.shoplocal.classes.SERVER_URL
import com.training.shoplocal.classes.downloader.Callback
import com.training.shoplocal.classes.downloader.ExtBitmap
import com.training.shoplocal.classes.downloader.ImageLinkDownloader
import com.training.shoplocal.log
import com.training.shoplocal.ui.theme.TextFieldBg
import com.training.shoplocal.ui.theme.TextFieldFont
import kotlin.math.pow


@Composable
fun ShowImagesIndicator(modifier: Modifier, index: State<Int>, count: Int){
    /*data class IndexImage(var value: Int = -1) // если захочется анимировать
    val curIndex= remember { // хранит предыдущее значение индекса
        IndexImage()
    }*/

    val symIndicator = "●"
    Row(modifier = modifier,
        verticalAlignment = Alignment.CenterVertically){
        for (i in 0 until count) {
           /* if (index.value == i)
                curIndex.value = i*/
            Box(modifier = Modifier.padding(horizontal = 1.dp)){
                Text(text = symIndicator,
                    color = if (index.value == i) TextFieldFont.copy(alpha = 0.7f) else TextFieldBg
                )
            }
        }
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
fun ShowProductImages(modifier: Modifier, product: Product, onChangeImage: (index: Int) -> Unit = {}){
    @Composable
    fun ProgressDownloadImage(size: Size){
        if (size.width > 0) {
            val dpSize = LocalDensity.current.run {
                size.toDpSize()
            }
            val padding = LocalDensity.current.run {
                16.dp.toPx()
            }
            val heightGradient = kotlin.math.sqrt(size.width.pow(2) +
            size.height.pow(2)) + padding
            val widthGradient  = 120f
            val delta = ( heightGradient - size.height) / 2
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
                                endX   = widthGradient
                            ),
                            size = Size(widthGradient, heightGradient)
                        )
                    }
                }
            }
        }
    }
    data class ImageStatus (var link: String, var image: ImageBitmap = EMPTY_IMAGE, var status: Status = Status.NONE)
   /* val currentProduct = remember {
        product
    }*/
    val linkImages = remember {
        val entries = mutableListOf<ImageStatus>()
        product.linkimages?.forEach {
            entries.add(ImageStatus(it))
        }
        entries
    }
    var downloadedMainImage by remember {
        mutableStateOf(false)
    }

    DisposableEffect(Unit){
        onDispose {
            linkImages.clear()
        }
    }

    @Composable
    fun downloadImage(index: Int): MutableState<ImageBitmap> {
        val isMainImage = remember{
            index == 0
        }
        fun checkMainImage(){
            if (isMainImage)
                downloadedMainImage = true
        }
        val downloadedImage = remember { mutableStateOf(
            ImageBitmap(1,1, hasAlpha = true, config = ImageBitmapConfig.Argb8888)
        ) }

        val linkImage = remember{linkImages[index]}
        LaunchedEffect(Unit) {
            linkImage.status = Status.LOADING
            ImageLinkDownloader.downloadImage("$SERVER_URL/images/${linkImage.link}", callback = object: Callback{
                override fun onComplete(image: ExtBitmap) {
                    image.bitmap?.let{
                        linkImage.status = Status.COMPLETE
                        downloadedImage.value = it.asImageBitmap()
                        linkImage.image = it.asImageBitmap()
                        /*if (isMainImage)
                            downloadedMainImage = true*/
                        checkMainImage()
                    }
                }
                override fun onFailure() {
                    linkImage.status = Status.FAIL
                    downloadedImage.value = EMPTY_IMAGE
                    /*if (isMainImage)
                        downloadedMainImage = true*/
                    checkMainImage()
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
    Box(
        modifier = modifier
            .background(Color.White)
            .onGloballyPositioned { coordinates ->
                size = coordinates.size.toSize()
            }
    ) {
      //  log ("download = $downloadedMainImage")
        LazyRow(
          //  modifier = Modifier.padding(all = 8.dp),
            state = lazyRowState,
            horizontalArrangement = Arrangement.Center,
            flingBehavior = flingBehavior
        ) {
            itemsIndexed(linkImages) { index, item ->
                Image(
                    modifier = Modifier
                        .fillParentMaxSize()
                        .padding(all = 8.dp),
                    bitmap = run {
//                        if (linkImages[index].status == Status.NONE)
                        if (item.status == Status.NONE)
                            downloadImage(index).value
                        else
                            item.image
                    },
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

        val indexImage = remember {
            derivedStateOf {
                if (lazyRowState.layoutInfo.visibleItemsInfo.isNotEmpty())
                    lazyRowState.firstVisibleItemIndex
                else
                    -1
            }
        }
        LaunchedEffect(indexImage.value) {
            onChangeImage(indexImage.value)
        }
    }


   //log("Product ${product.name}")
}