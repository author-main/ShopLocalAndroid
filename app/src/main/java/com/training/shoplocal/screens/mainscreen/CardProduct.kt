package com.training.shoplocal.screens.mainscreen

import android.graphics.Bitmap
import com.training.shoplocal.R
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
import com.training.shoplocal.AppShopLocal.Companion.appContext
import com.training.shoplocal.getPrice
import com.training.shoplocal.log
import com.training.shoplocal.ui.theme.*
import java.io.IOException
import java.math.RoundingMode
import java.text.DecimalFormat


@Composable
private fun BottomSheetItem(@DrawableRes id: Int, text: String, action: ()->Unit){
    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable(onClick = { action() })
        .height(54.dp)
        .background(color = PrimaryDark)
        .padding(start = 15.dp), verticalAlignment = Alignment.CenterVertically){
        Icon(painterResource(id = id), contentDescription = null, tint = TextFieldFont)
        Text(modifier = Modifier.padding(start = 16.dp), text = text, color = TextFieldFont)
    }
}

@Preview(showBackground = true)
@Composable
fun BotomSheetItemPreview(){
    BottomSheetItem(id = R.drawable.ic_product_bs, text = "checkit") {

    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheet(){
    val modalBottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()
    ModalBottomSheetLayout(
        sheetContent = {
            BottomSheetContent()
        },
        sheetState = modalBottomSheetState,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetBackgroundColor = PrimaryDark
    ) {
        /* Add code later */
    }
}

@Composable
private fun BottomSheetContent(){
    val textItems = stringArrayResource(id = R.array.bottomsheet_product_items)
    BottomSheetItem(R.drawable.ic_brend_bs, textItems[0]){

    }
    BottomSheetItem(R.drawable.ic_favorite_bs, textItems[1]){

    }
    BottomSheetItem(R.drawable.ic_product_bs, textItems[2]){

    }
    BottomSheetItem(R.drawable.ic_cancel_bs, textItems[3]){

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
    val partNumber = df.format(count).split(",")
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

@Composable
fun CardProduct(){
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
                    action()
                }
        )
    }
    @Composable
    fun ButtonFavorite(modifier: Modifier, action: ()-> Unit){
        val checked = remember{mutableStateOf(false)}
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

                    DiscountPanel(modifier = Modifier.align(Alignment.BottomStart), percent = 15)
                    ButtonMore(modifier = Modifier.align(Alignment.BottomEnd)
                    ) {
                        log("click")
                    }

                    ButtonFavorite(modifier = Modifier.align(Alignment.TopEnd)
                    ) {
                        log("favorite")
                    }

                    //Text("asfgsdfgsf", modifier = Modifier.align(Alignment.BottomEnd))

                    /*if (imageBitmap != null)
                    log("image loaded")*/
                }
            }
            // < * Text Price
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(modifier = Modifier.padding(end = 8.dp),
                    fontSize = 18.sp,
                    text = getPrice(11100f),
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrice
                )
                Text(modifier = Modifier.padding(end = 8.dp),
                    fontSize = 14.sp,
                    text = getPrice(11260f),
                    fontWeight = FontWeight.SemiBold,
                    style = TextStyle(textDecoration = TextDecoration.LineThrough),
                    color = TextPrice
                )
            }
            // * >
            // < * Text Promotion
            Text("Бестселлер",
                fontSize = 14.sp,
                color = TextPromotion)
            // * >
            // < * Text Brend
            Text("AMD",
                color = TextBrand)
            // * >
            // < * Text Description
            Text("Процессор Intel Core i3 10105, LGA 1200, OEM",
                fontFamily = labelFont,
                color = TextDescription,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            // * >
            StarPanel(2.6f)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CardProducttPreview() {
    CardProduct()
}