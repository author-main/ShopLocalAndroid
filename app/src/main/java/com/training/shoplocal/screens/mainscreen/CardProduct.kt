package com.training.shoplocal.screens.mainscreen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.training.shoplocal.AppShopLocal.Companion.appContext
import com.training.shoplocal.log
import java.io.IOException

@Composable
fun CardProduct(){
    val context = LocalContext.current
    Box(modifier = Modifier
        .width(150.dp)
        .height(260.dp)){
        Column{
            Card(modifier = Modifier
                .requiredSize(150.dp),
                backgroundColor = Color.White
            ) {
                Box(modifier = Modifier.fillMaxSize()
                    .padding(8.dp),
                    contentAlignment = Alignment.Center
                    ) {
                    val bitmap =
                        try {
                            with(appContext().assets.open("images/cpu_rayzen.jpg")) {
                                BitmapFactory.decodeStream(this)
                            }
                        } catch (e: IOException) {
                            null
                        }
                    val imageBitmap: ImageBitmap? =
                        bitmap?.asImageBitmap()

                    imageBitmap?.let {
                        Image(
                            //alignment = Alignment.BottomEnd,
                            bitmap = it, contentDescription = null
                        )
                    }
                    Text("asfgsdfgsf", modifier = Modifier.align(Alignment.BottomEnd))

                    /*if (imageBitmap != null)
                    log("image loaded")*/
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CardProducttPreview() {
    CardProduct()
}