package com.training.shoplocal.screens.appscreen

import android.text.Layout.Alignment
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageBitmapConfig
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import com.training.shoplocal.classes.EMPTY_IMAGE
import com.training.shoplocal.classes.Product
import com.training.shoplocal.classes.SERVER_URL
import com.training.shoplocal.classes.downloader.Callback
import com.training.shoplocal.classes.downloader.ExtBitmap
import com.training.shoplocal.classes.downloader.ImageLinkDownloader
import com.training.shoplocal.log
import com.training.shoplocal.loginview.LoginViewState
import com.training.shoplocal.md5
import com.training.shoplocal.screens.mainscreen.ImageLink


private enum class Status {
    NONE,
    LOADING,
    COMPLETE,
    FAIL
}
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ShowProductImages(modifier: Modifier, product: Product, onChangeImage: (index: Int) -> Unit = {}){
    data class ImageStatus (var link: String, var image: ImageBitmap = EMPTY_IMAGE, var status: Status = Status.NONE)
    val product = remember {
        product
    }
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
        fun checkMainImage(){
            if (index == 0)
                downloadedMainImage = true
        }
        val downloadedImage = remember { mutableStateOf(
            ImageBitmap(1,1, hasAlpha = true, config = ImageBitmapConfig.Argb8888)
        ) }
        val linkImage = remember{linkImages[index].apply { status = Status.LOADING }}
        LaunchedEffect(index) {
            ImageLinkDownloader.downloadImage("$SERVER_URL/images/${linkImage.link}", callback = object: Callback{
                override fun onComplete(image: ExtBitmap) {
                    image.bitmap?.let{
                        //log("complete ${linkImage.link}")
                        linkImage.image = it.asImageBitmap()
                        linkImage.status = Status.COMPLETE
                        downloadedImage.value = it.asImageBitmap()
                        checkMainImage()
                    }
                }
                override fun onFailure() {
                    linkImage.status = Status.FAIL
                    downloadedImage.value = EMPTY_IMAGE
                    checkMainImage()
                }
            }
            )
        }
        return downloadedImage
    }

    if (linkImages.size > 0) {
        if (!downloadedMainImage) {
            if (linkImages[0].status == Status.NONE)
                downloadImage(0)
        } else {
            for (index in 1 until linkImages.size) {
                if (linkImages[index].status == Status.NONE)
                    downloadImage(index)
            }
        }
    }

    val lazyRowState = rememberLazyListState()
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = lazyRowState)
    LazyRow(
        state = lazyRowState, modifier = modifier.background(Color.White),
        horizontalArrangement = Arrangement.Center,
        flingBehavior = flingBehavior
    ) {
        items(linkImages, {linkImage -> linkImage.link}){item ->
            Image(
                modifier = Modifier
                    .fillParentMaxSize()
                    .padding(all = 8.dp),
                bitmap = item.image,
                contentDescription = null
            )
        }
        /*linkImages.forEach{ item ->
            item {
                Image(
                    modifier = Modifier
                        .fillParentMaxSize()
                        .padding(all = 8.dp),
                    bitmap = item.image,/*run {
                        if (item.first != IMAGE_STATE.COMPLETED)
                            getCardImage(index).value
                        else
                            item.second
                    },*/
                    contentDescription = null
                )
            }
        }*/
    }

    val indexImage = remember {
        derivedStateOf {
            if (lazyRowState.layoutInfo.visibleItemsInfo.isNotEmpty())
                 lazyRowState.layoutInfo.visibleItemsInfo.first().index
            else
                -1
        }
    }
    LaunchedEffect(indexImage.value) {
        onChangeImage(indexImage.value)
    }


   //log("Product ${product.name}")
}