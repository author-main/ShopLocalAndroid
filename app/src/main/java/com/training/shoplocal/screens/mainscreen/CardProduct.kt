package com.training.shoplocal.screens.mainscreen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.training.shoplocal.AppShopLocal.Companion.appContext
import com.training.shoplocal.log
import com.training.shoplocal.ui.theme.TextBrand
import com.training.shoplocal.ui.theme.TextDescription
import com.training.shoplocal.ui.theme.TextPrice
import com.training.shoplocal.ui.theme.TextPromotion
import java.io.IOException

@Composable
fun CardProduct(){
    val context = LocalContext.current
    Box(modifier = Modifier
        .width(150.dp) )
        {
        Column(
        )//verticalArrangement = Arrangement.spacedBy(4.dp))
        {
            Card(modifier = Modifier
                .requiredSize(150.dp)
                .padding(bottom = 8.dp),
                backgroundColor = Color.White,
                shape = RoundedCornerShape(8.dp)
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
                    //Text("asfgsdfgsf", modifier = Modifier.align(Alignment.BottomEnd))

                    /*if (imageBitmap != null)
                    log("image loaded")*/
                }
            }
            // < Text Price
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(modifier = Modifier.padding(end = 8.dp),
                    fontSize = 17.sp,
                    text = "11 100P",
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrice
                )
                Text(modifier = Modifier.padding(end = 8.dp),
                    fontSize = 14.sp,
                    text = "11 260P",
                    fontWeight = FontWeight.SemiBold,
                    style = TextStyle(textDecoration = TextDecoration.LineThrough),
                    color = TextPrice
                )
            }
            // < Text Promotion
            Text("Бестселлер",
                fontSize = 14.sp,
                color = TextPromotion)
            // >
            // >
            // < Text Brend
            Text("AMD",
                color = TextBrand)
            // >
            // < Text Description
            Text("Процессор Intel Core i3 10105, LGA 1200, OEM",
                color = TextDescription,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            // >
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CardProducttPreview() {
    CardProduct()
}