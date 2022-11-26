package com.training.shoplocal.screens.mainscreen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.training.shoplocal.*
import com.training.shoplocal.AppShopLocal.Companion.appContext
import com.training.shoplocal.R
import com.training.shoplocal.classes.Product
import com.training.shoplocal.classes.downloader.Callback
import com.training.shoplocal.classes.downloader.ImageLinkDownloader
import com.training.shoplocal.ui.theme.*
import com.training.shoplocal.viewmodel.RepositoryViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.IOException
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

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
    val floatPart = if (partNumber.size == 2)
        partNumber[1].toInt() else 0
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
                val center     = bm.width / 2f
                val widthStar  = if (floatPart <= 5)
                    center - (5 - floatPart) * part
                else center + (floatPart - 5) * part
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CardProduct(product: Product, state: ModalBottomSheetState){//}, scope: CoroutineScope){
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
                        state.show()
                    }
                }
        )
    }
    @Composable
    fun ButtonFavorite(modifier: Modifier, action: ()-> Unit){
        val favorite: Byte = product?.favorite ?: 0
        val checked = remember{mutableStateOf(favorite > 0)}
        Image(
            painter = painterResource(R.drawable.ic_favorite),
            contentDescription = null,
            contentScale = ContentScale.None,
            colorFilter = if (checked.value) ColorFilter.tint(ImageFavoriteOn)
            else ColorFilter.tint(ImageFavoriteOff),
            modifier = modifier
                .clip(CircleShape)
                .size(24.dp)
                .clickable/*(interactionSource = remember { MutableInteractionSource() },
                           indication = rememberRipple(radius = 16.dp)) */{
                    checked.value = !checked.value
                    action()
                }
        )
        Image(
            painter = painterResource(R.drawable.ic_favorite_border),
            contentDescription = null,
            contentScale = ContentScale.None,
            colorFilter = if (!checked.value) ColorFilter.tint(BorderButton)
            else ColorFilter.tint(BgCard),
            modifier = modifier
                .size(24.dp)
        )

    }

    val context = LocalContext.current
    val labelFont = FontFamily(Font(R.font.robotocondensed_light))


    val imageLink: String = product.linkimages?.let{
        if (it.isNotEmpty())
            it[0]
        else
            ""
    } ?: ""
    //log("$SERVER_URL/images/$imageLink")
    var bitmap: Bitmap? = null
    ImageLinkDownloader.downloadImage("$SERVER_URL/images/$imageLink", object: Callback{
        override fun onComplete(image: Bitmap) {
            bitmap = image
        }
        override fun onFailure() {
            log("error download image")
        }
    })



    Box(modifier = Modifier
        .width(150.dp) )
        {
        Column(
        )//verticalArrangement = Arrangement.spacedBy(4.dp))
        {
            Card(modifier = Modifier
                .requiredSize(150.dp)
                .padding(bottom = 8.dp),
                backgroundColor = BgCard,
                shape = RoundedCornerShape(8.dp)
            ) {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                    contentAlignment = Alignment.Center
                    ) {
                    /*val bitmap =
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
                    }*/

                   /* val imageLink: String = product.linkimages?.let{
                        if (it.isNotEmpty())
                            it[0]
                        else
                            ""
                    } ?: ""
                    log(imageLink)
                   Image(
                        ImageLinkDownloader.downloadImage(imageLink, object: Callback(){
                            override fun onComplete(image: Bitmap) {
                                bitmap = image
                            }

                            override fun onFailure() {
                                null
                            }
                        }),
                        contentDescription = null
                    )*/

                    DiscountPanel(modifier = Modifier.align(Alignment.BottomStart), percent = product.discount)
                    ButtonMore(modifier = Modifier.align(Alignment.BottomEnd)
                    ) {
                        log("click")
                    }

                    ButtonFavorite(modifier = Modifier.align(Alignment.TopEnd)
                    ) {

                    }

                    //Text("asfgsdfgsf", modifier = Modifier.align(Alignment.BottomEnd))

                    /*if (imageBitmap != null)
                    log("image loaded")*/
                }
            }
            // < * Text Price
            //Row(verticalAlignment = Alignment.CenterVertically) {
                Card(
                    modifier = Modifier.padding(top = 4.dp),
                    backgroundColor = BgTextPrice,
                    shape = RoundedCornerShape(6.dp)
                ){
                    Text(modifier = Modifier.padding(horizontal = 4.dp),
                        fontSize = 17.sp,
                        text = getSalePrice(product.price, product.discount),
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrice)
                }

               /* Text(modifier = Modifier.padding(end = 8.dp),
                    fontSize = 18.sp,
                    text = getPrice(11100f),
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrice
                )*/
                Text(modifier = Modifier.padding(top = 4.dp),
                    fontSize = 14.sp,
                    text = getFormattedPrice(product.price),
                    fontWeight = FontWeight.SemiBold,
                    style = TextStyle(textDecoration = TextDecoration.LineThrough),
                    color = TextPriceDiscount)

        //    }
            // * >
            // < * Text Promotion

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

/*@Preview(showBackground = true)
@Composable
fun CardProducttPreview() {
    BottomSheet()
    //CardProduct()
}*/