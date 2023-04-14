package com.training.shoplocal.screens.mainscreen.composable

import androidx.compose.foundation.MutatePriority
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.training.shoplocal.ImageDownloader
import com.training.shoplocal.classes.*
import com.training.shoplocal.classes.downloader.Callback
import com.training.shoplocal.classes.downloader.ExtBitmap
import com.training.shoplocal.classes.downloader.ImageLinkDownloader
import com.training.shoplocal.isEmpty
import com.training.shoplocal.log
import com.training.shoplocal.ui.theme.PrimaryDark
import com.training.shoplocal.ui.theme.TextFieldBg
import com.training.shoplocal.ui.theme.TextFieldFont
import com.training.shoplocal.viewmodel.RepositoryViewModel
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.math.absoluteValue
import kotlin.math.pow


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ZoomImage(modifier: Modifier, source: ImageBitmap, scrollState: MutableState<Boolean>, isZoom: Boolean = false, onClick: () -> Unit){

  //  val coroutineScope = rememberCoroutineScope()
    fun enableScrolling(value: Boolean) {
        /*  scrollState?.run {
              coroutineScope.launch {
                  setScrolling(value)
              }
          }*/
        scrollState.value = value
    }


    val minScale: Float = 1f
    val maxScale: Float = 3f
    var scale by remember {
        mutableStateOf(1f)
    }

    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    val animScale by animateFloatAsState(
        targetValue = scale,
        animationSpec = tween(durationMillis =100, easing = LinearEasing)/*,
        finishedListener = {
            animate = false
        }*/
    )
    val animOffsetX by animateFloatAsState(
        targetValue = offsetX,//offsetX,
        animationSpec = tween(durationMillis =100, easing = LinearEasing)
    )

    val animOffsetY by animateFloatAsState(
        targetValue = offsetY,//offsetY,
        animationSpec = tween(durationMillis =100, easing = LinearEasing)
    )

    //log("offsetX $offsetX, offsetY $offsetY")

  /*  fun resetZoomData() {
     //   log ("reset Zoom...")
       /* offsetX = 0f
        offsetY = 0f*/
        offset.copy(0f, 0f)
        scale = minScale
        enableScroll(true)
    }*/

/*    val coroutineScope = rememberCoroutineScope()
    fun enableScroll(enabled: Boolean) {
      scrollState?.run {
        coroutineScope.launch {
            value = enabled
        }
      }
    }*/




    Box(modifier = modifier

        .clip(RectangleShape)
        .padding(8.dp)
        .combinedClickable(
            enabled = true,
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = {
                onClick()
            },
            onDoubleClick = {
                //  log("double click...")
                //  var enabled = false
                if (isZoom) {
                    val delta = (maxScale - minScale) / 2f
                    scale = if (scale >= minScale + delta) {
                        //         enabled = true
                        offsetX = 0f
                        offsetY = 0f
                        minScale
                    } else {
                        maxScale
                    }
                    //       enableScrolling(enabled)
                }
            },
        )
        /*  .pointerInput(Unit) {
            detectTapGestures(
                onDoubleTap = {
                    if (isZoom) {
                        val delta = (maxScale - minScale) / 2f
                        scale = if (scale >= minScale + delta) {
                            offsetX = 0f
                            offsetY = 0f
                            minScale
                        } else {
                            maxScale
                        }
                    }
                },
                onTap = {
                    onClick()
                }
            )
        }*/

        .pointerInput(Unit) {
            if (isZoom) {
                awaitEachGesture {
                    awaitFirstDown()
                    do {
                        val event = awaitPointerEvent()
                        scale =//*= event.calculateZoom()
                            minOf(maxOf(minScale, scale * event.calculateZoom()), maxScale)
                        if (scale > minScale) {
                            val offset = event.calculatePan()
                            offsetX += offset.x
                            offsetY += offset.y
                        } else {
                            scale = minScale
                            offsetX = 0f
                            offsetY = 0f
                        }
                        //enableScrolling(scale == minScale)


                        /*                       //var enabled = scrollState.value
                                              val event = awaitPointerEvent()
                                              //  log(event.type)
                                                  val scaleValue =
                                                      minOf(maxOf(minScale, scale * event.calculateZoom()), maxScale)
                                                  if (scaleValue > minScale) {
                                                    //  enableScrolling(false)
                                                      scale =
                                                          scaleValue//minOf(maxOf(minScale, scale * scaleValue), maxScale)
                                                      val eventOffset = event.calculatePan()
                                                      offsetX += eventOffset.x
                                                      offsetY += eventOffset.y
                                                  } else {
                                                      offsetX = 0f
                                                      offsetY = 0f

                                                    //  enabled = true
                                              }

                                              enableScrolling(scale == minScale)*/
                    } while (event.changes.any {
                            it.pressed
                        })


                }
            }
        }

       /* .pointerInput(Unit) {
            detectTransformGestures { _, _, zoom, _ ->
                scale = when {
                    scale < minScale -> minScale
                    scale > maxScale -> maxScale
                    else -> scale * zoom
                }
                enableScroll(scale == 1f)
            }
        }*/


    ){
        //log("pointerInput scale = $scale")
        Image(source,
            //contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxSize()
                //    .transformable(state = transformState)
                .graphicsLayer {
                    if (isZoom) {
                        scaleX = animScale
                        scaleY = animScale

                        translationX = animOffsetX
                        translationY = animOffsetY
                        //log("scale...")

                    }
                }, contentDescription = null)

    }
    SideEffect {
        //log("scale = $scale")
        enableScrolling(scale == minScale)
    }
}

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
        val selectedColor = TextFieldFont.copy(alpha = 0.7f)
        val symIndicator = "●"
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
fun ShowProductImages(modifier: Modifier, product: Product, reduce: Boolean, startIndex: MutableState<Int> = mutableStateOf(0), isZoom: Boolean = false, onLoadImage:((index: Int, image: ImageBitmap) -> Unit)? = null,
                      onChangeImage: ((index: Int) -> Unit)? = null, onClick: (product: Product) -> Unit = {}) {
    //val viewModel: RepositoryViewModel = viewModel()

    val imageDownloader = ImageDownloader.current

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
                modifier = Modifier
                    .size(dpSize)
                    .clip(RectangleShape)
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
    /*val product = remember {
        product
    }*/
    /*var scale by remember {
        mutableStateOf(1f)
    }
    val offsetX = remember { mutableStateOf(0f) }
    val offsetY = remember { mutableStateOf(0f) }
    val coroutineScope = rememberCoroutineScope()*/


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
/*        val imageDownloader = remember {

        }*/
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
            //ImageLinkDownloader.downloadImage(
            //viewModel.imageDownloader.downloadImage(
            imageDownloader.downloadImage(
            //viewModel.imageDownloader.downloadImage(
                "$SERVER_URL/$DIR_IMAGES/${linkImage.link}",
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
    val scrollState = remember {
        mutableStateOf(true)
    }
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = lazyRowState)
    var size by remember {
        mutableStateOf(Size.Zero)
    }

    //data class Integer(var value: Int = 0)


  /*  val updateSelectedItem = remember {
        derivedStateOf {
            selectedImage.value != startIndex.value
        }
    }*/
   // log ("selected ${startIndex.value}")

    /*val selectedIndex = remember {
        startIndex
    }*/

    //LaunchedEffect(startIndex.value) {
    data class Boolean(var value: kotlin.Boolean)
    val init = remember{ Boolean(true) }

    LaunchedEffect(startIndex.value) {
        MainScope().launch {
            if (!(startIndex.value == 0 && init.value))
                lazyRowState.scrollToItem(startIndex.value)
            init.value = false
        }
    }


    DisposableEffect(Unit){
        onDispose {
            //ImageLinkDownloader.cancel()
            //viewModel.imageDownloader.cancelAll()
            imageDownloader.cancelAll()
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
            userScrollEnabled = scrollState.value,
            horizontalArrangement = Arrangement.Center,
            flingBehavior = flingBehavior
        ) {


            items(linkImages, {linkimage -> linkimage.id}) { item ->
                if (!item.image.value.isEmpty()) {
                    ZoomImage(modifier = Modifier.fillParentMaxSize(), item.image.value, scrollState = scrollState, isZoom = isZoom) {
                        onClick(product)
                    }
                }
            //items(linkImages) { item ->
                //if (item.status == Status.COMPLETE)
                //val scrollState = rememberScrollState()
                /*if (!item.image.value.isEmpty()) {
                  /*  Box(modifier = Modifier
                        .verticalScroll(scrollState)
                      //  .horizontalScroll(scrollState)
                        .fillParentMaxSize()
                        .padding(all = 8.dp)
                    ) {*/
                        Image(
                            modifier = Modifier
                                .pointerInput(Unit) {
                                    awaitEachGesture {
                                        awaitFirstDown()
                                        do {
                                            val event = awaitPointerEvent()
                                            scale *= event.calculateZoom()
                                            if (scale > 1) {
                                                log("scale = $scale")
                                                lazyRowState.run {
                                                    coroutineScope.launch {
                                                        //setScrolling(false)
                                                    }
                                                }
                                            }

                                       /*     val offset = event.calculatePan()
                                            log(offset.x)*/
                                        } while (event.changes.any { it.pressed })
                                    }
                                }


                              //  .verticalScroll(scrollState)
                                .fillParentMaxSize(),
                          /*      .pointerInput(Unit) {
                            detectTransformGestures(true, onGesture =  { centroid, pan, zoom, rotate ->
                                scale = when {
                                    scale < 0.5f -> 0.5f
                                    scale > 3f -> 3f
                                    else -> scale * zoom
                                }
                            })
                        }
                                .graphicsLayer(
                                    scaleX = scale,
                                    scaleY = scale
                                ),*/
                            bitmap = item.image.value,
                            //contentScale = ContentScale.None,
                            contentDescription = null
                        )
                    }
              //  }*/
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
                        if (offset > half.width)
                            index += 1
                    index
                }
            }
            LaunchedEffect(indexImage.value) {
              if (!init.value)
                startIndex.value = indexImage.value
              changeImage(indexImage.value)
            }
        }
    }
}

/*suspend fun ScrollableState.setScrolling(value: Boolean) {
    scroll(scrollPriority = MutatePriority.PreventUserInput) {
        when (value) {
            true -> Unit
            else -> awaitCancellation()
        }
    }
}*/