package com.training.shoplocal.screens.mainscreen.composable

import androidx.compose.animation.*
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.core.view.inputmethod.EditorInfoCompat
import com.training.shoplocal.DialogRouter
import com.training.shoplocal.R
import com.training.shoplocal.classes.Container
import com.training.shoplocal.classes.EMPTY_IMAGE
import com.training.shoplocal.classes.Product
import com.training.shoplocal.isEmpty
import com.training.shoplocal.log
import com.training.shoplocal.screens.mainscreen.ImageLink
import com.training.shoplocal.ui.theme.PrimaryDark
import com.training.shoplocal.ui.theme.SelectedItemBottomNavi
import com.training.shoplocal.ui.theme.TextFieldBg
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialApi::class)
@Composable
fun ShowBigProductImages(open: MutableState<Boolean>, product: Product, index: Int) {
    val close = remember {
        mutableStateOf(false)
    }
    val selectedIndex = remember{mutableStateOf(index)}

    val startIndex    = remember{mutableStateOf(index)}

    val countImages = remember {product.linkimages?.size ?: 1}
    val images = remember {
        MutableList<MutableState<ImageBitmap>>(countImages){ mutableStateOf(EMPTY_IMAGE) }
    }
/*    val openPopup = remember{ mutableStateOf(false) }
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        launch {
            //delay(500)
            openPopup.value = true
        }
    }
    fun onDissmiss(){
        coroutineScope.launch {
            openPopup.value = false
            delay(300)
            open.value = false
        }
    }*/


    /*    Popup(
            alignment = Alignment.Center,
            onDismissRequest = {
        /*        coroutineScope.launch {
                    openPopup.value = false
                    delay(500)
                    open.value = false
                }*/
                               onDissmiss()
                },
            properties = PopupProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = false,
                focusable = true
            )
        ) {
                AnimatedVisibility(
                    visible = openPopup.value,
                    enter = slideInHorizontally (
                        animationSpec = tween(500)
                    ){ fullWidth ->
                        fullWidth * 2
                    } /* + fadeIn(
                            animationSpec = tween(durationMillis = 800)
                            )*/
                    ,
                    /*scaleIn(
                        animationSpec = tween(500)
                    ),*/
                    exit = slideOutHorizontally (
                        animationSpec = tween(300){ fullWidth ->
                            - fullWidth * 2
                        }
                    )
                    /*scaleOut(
                        animationSpec = tween(500)
                    )*/
                ) {*/
    AnimatedScreen(open, close) {
        Column(modifier = Modifier
            .fillMaxSize()
            .background(PrimaryDark)) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(Color.White)
            ) {
                //val img = ImageBitmap.imageResource(R.drawable.ic_close)
                //    log("start index = ${startIndex.value}")
                ShowProductImages(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    product = product,
                    reduce = false,
                    startIndex = startIndex,
                    isZoom = true,
                    onLoadImage = {  index, image ->
                        images[index].value = image
                        //changedImage = !changedImage
                        //      log("image[$index] $image")
                    },
                    onChangeImage = {
                        //indexBigImage.value = it
                        // startIndex.value = it
                        selectedIndex.value = it
                    }) {

                }
                Box(
                    modifier = Modifier
                        .padding(all = 8.dp)
                        .align(Alignment.TopEnd)
                        .requiredSize(24.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .clickable {
                            close.value = true
                        },
                ) {
                    Image(
                        ImageVector.vectorResource(R.drawable.ic_close),
                        colorFilter = ColorFilter.tint(TextFieldBg),
                        contentDescription = null
                    )
                }
            }

            if (countImages > 1)
            //if (countImages == 0)
                Box(modifier = Modifier
                    .padding(top = 4.dp)
                    .fillMaxWidth()) {
                    val imageViewSize = DpSize(45.dp, 50.dp)
                    LazyRow(modifier = Modifier.align(Alignment.Center)) {
                        itemsIndexed(images) { index, item ->
                            //log("$item")
                            //  log("image height = ${item.value.height}, width = ${item.value.width}")
                            Surface(
                                modifier = Modifier
                                    .padding(all = 4.dp)
                                    .clickable {
                                        // log("selected index = $index")
                                        //indexBigImage.value = index
                                        selectedIndex.value = index
                                        startIndex.value = index
                                    },
                                border = BorderStroke(2.dp, if (index == selectedIndex.value) SelectedItemBottomNavi else Color.Transparent),
                                color = Color.White,
                                shape = RoundedCornerShape(6.dp)
                            ) {

                                if (!item.value.isEmpty())
                                    Image(
                                        modifier = Modifier
                                            .size(imageViewSize)
                                            .padding(all = 4.dp)
                                            /*.height(100.dp)
                                            .width(75.dp)*/
                                            /*  .clip(RoundedCornerShape(6.dp))
                                            .border(
                                                BorderStroke(
                                                    2.dp,
                                                    if (index == indexBigImage.value) SelectedItemBottomNavi else Color.Transparent
                                                )
                                            )*/
                                            .background(Color.White),
                                        //contentScale = ContentScale.Inside,
                                        bitmap = item.value,
                                        //imageVector = ImageVector.vectorResource(id = R.drawable.ic_close),
                                        contentDescription = "index = $index"
                                    )
                            }
                        }
                    }
                }
        }

    }

}