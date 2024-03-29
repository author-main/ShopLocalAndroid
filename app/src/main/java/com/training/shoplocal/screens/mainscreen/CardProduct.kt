package com.training.shoplocal.screens.mainscreen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.training.shoplocal.*
import com.training.shoplocal.AppShopLocal.Companion.appContext
import com.training.shoplocal.R
import com.training.shoplocal.classes.*
import com.training.shoplocal.classes.fodisplay.VIEW_MODE
import com.training.shoplocal.screens.mainscreen.composable.DividerHorizontal
import com.training.shoplocal.screens.mainscreen.composable.ShowProductImages
import com.training.shoplocal.ui.theme.*
import com.training.shoplocal.viewmodel.RepositoryViewModel
import kotlinx.coroutines.launch

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
fun StarPanel(count: Float, starSize: Dp = 12.dp, starHorzInterval: Dp = 0.dp){
    val MAX_STAR_COUNT = 5
    val partNumber  = remember { getFormattedStar(count).split(DECIMAL_SEPARATOR) }
    val intPart     = remember { partNumber[0].toInt()}
    val floatPart   = remember { if (partNumber.size == 2) partNumber[1].toInt() else 0}
    val bm          = remember { BitmapFactory.decodeResource(appContext().resources, R.drawable.ic_star)}
    val widthStar   = remember { floatPart * (bm.width / 10f)}
    Row(modifier = Modifier.padding(top = 2.dp),
    ) {
        for (i in 0 until MAX_STAR_COUNT) {
            val color = if (i <= intPart - 1)
                ImageStarOn
            else
                ImageStarOff
            Box{
            Image( modifier = Modifier.requiredSize(starSize),
                bitmap = bm.asImageBitmap(),
                colorFilter = ColorFilter.tint(color),
                contentDescription = null
            )
            if (i == intPart && floatPart > 0) {
                val bmPart: Bitmap = Bitmap.createBitmap(bm, 0, 0, widthStar.toInt(), bm.height)
                Image(modifier = Modifier.height(starSize),
                    bitmap = bmPart.asImageBitmap(),
                    colorFilter = ColorFilter.tint(ImageStarOn),
                    contentDescription = null
                )
            }
            }
            if (starHorzInterval > 0.dp && i < MAX_STAR_COUNT - 1)
                DividerHorizontal(size = starHorzInterval)
        }
    }
}

data class ImageLink(val link: String, val md5: String)

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun CardProduct(product: Product, showMoreButton: Boolean = true, state: ModalBottomSheetState, modeview: VIEW_MODE = VIEW_MODE.CARD, onClick:(product: Product) -> Unit) {
    fun isCardModeView() =
        modeview == VIEW_MODE.CARD
    val fontsize = if (isCardModeView()) 14.sp else 13.sp
    val fontSizeDescription = 15.sp//if (isCardModeView()) 16.sp else 15.sp
    val CARD_SIZE = if (isCardModeView()) 150 else 110
    val viewModel: RepositoryViewModel = viewModel()
    val brand: String = remember {product.brand?.let { viewModel.getBrand(it) } ?: ""}
    val scope = rememberCoroutineScope()

    @Composable
    fun ButtonMore(modifier: Modifier){
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
                        viewModel.setSelectedProduct(product)
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
                if (product.id == observeSelectedProduct.value.id) {
                    product.favorite = observeSelectedProduct.value.favorite
                    observeSelectedProduct.value.favorite > 0
                }
                else
                    product.favorite > 0
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
                    .clickable{
                        product.favorite = if (product.favorite > 0) 0 else 1
                        viewModel.setProductFavorite(product)
                    }
            )
        }
    }

    @Composable
    fun ProductImages(){
        Card(modifier = Modifier
            .requiredSize(CARD_SIZE.dp),
            backgroundColor = BgCard,
            shape = RoundedCornerShape(16.dp)
        ) {
            Box(modifier = Modifier
                .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                    ShowProductImages(modifier = Modifier.fillMaxSize(), reduce = true, product = product){
                       onClick(product)
                    }
                    DiscountPanel(
                        modifier = Modifier.align(if (isCardModeView()) Alignment.BottomStart else Alignment.TopStart),
                        percent = product.discount
                    )
                    if (showMoreButton) {
                        ButtonMore(
                            modifier = Modifier.align(Alignment.BottomEnd)
                        )
                    }
                    if (isCardModeView())
                        ButtonFavorite(
                            modifier = Modifier.align(Alignment.TopEnd)
                        )
            }
        }
    }

    @Composable
    fun DiscountText(){
        val textDiscount = remember {
            getSalePrice(product.price, product.discount)
        }
        Card(
            modifier = Modifier.padding(top = 4.dp),
            backgroundColor = BgTextPrice,
            shape = RoundedCornerShape(6.dp)
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 4.dp),
                fontSize = 15.sp,
                text = textDiscount,//getSalePrice(product.price, product.discount),
                fontWeight = FontWeight.SemiBold,
                color = TextPrice
            )
        }
    }

    @Composable
    fun PriceText(){
        fun isDiscount() = product.discount > 0
        val text = remember {
            if (isDiscount()) getFormattedPrice(product.price) else getStringResource(
                id = R.string.text_nodiscounts)
        }
        val style =
            TextStyle(textDecoration = if (isDiscount()) TextDecoration.LineThrough else TextDecoration.None)

            Text(
                modifier = Modifier.padding(top = 4.dp),
                fontSize = fontsize,
                text = text,
                style = style,
                color = TextPriceDiscount
            )
    }

    @Composable
    fun CartButton(modifier: Modifier){
        Box(
            modifier
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
        val promostr: String = product.getTypeSale()
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
        Text(
            text = product.name,
            fontFamily = FontFamily(Font(R.font.robotocondensed_light)),
            fontSize = fontSizeDescription,
            color = TextDescription,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
        if (isCardModeView()) {
            Column(
                modifier = Modifier
                    .width(CARD_SIZE.dp)
                    .padding(vertical = 10.dp)
                    .clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = null
                    ) {
                        onClick(product)
                    }
            ) {
                ProductImages()
                Spacer(modifier = Modifier
                    .height(8.dp)
                    .fillMaxWidth())
                Row(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalAlignment = CenterVertically
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
                StarPanel(product.star)
            }
        } else {
            Column(Modifier
                .clickable(interactionSource = MutableInteractionSource(),
                    indication = null) {
                    onClick(product)
                }
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = CenterVertically
                ) {
                    ProductImages()
                    Column(
                        Modifier
                            .weight(1f)
                            .padding(start = 8.dp)
                    ) {
                        DescriptionText()
                        Row {
                            Column(Modifier.weight(1f)) {
                                ActionText()
                                BrendText()
                            }
                            ButtonFavorite(modifier = Modifier.align(CenterVertically))
                            Spacer(modifier = Modifier.width(4.dp))
                        }
                        Row {
                            Column(Modifier.weight(1f)) {
                                StarPanel(product.star)
                                Row {
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
 }