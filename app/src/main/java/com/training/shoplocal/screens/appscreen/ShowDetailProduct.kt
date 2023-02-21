package com.training.shoplocal.screens.appscreen

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.training.shoplocal.R
import com.training.shoplocal.classes.Product
import com.training.shoplocal.classes.Review
import com.training.shoplocal.getRate
import com.training.shoplocal.getStringResource
import com.training.shoplocal.log
import com.training.shoplocal.screens.mainscreen.StarPanel
import com.training.shoplocal.ui.theme.*
import com.training.shoplocal.viewmodel.RepositoryViewModel

@Composable
fun ShowDetailProduct(value: Product){
    data class Integer(var value: Int = 0)
    val countReviews = remember {
        Integer()
    }
    val product = remember {
        value
    }
    val viewModel: RepositoryViewModel = viewModel()
    val reviews = viewModel.reviews.collectAsState()
    val indexImage = remember {
        mutableStateOf(0)
    }
    DisposableEffect(Unit) {
        viewModel.getReviewProduct(product.id, limit = 2)
        onDispose {
            viewModel.clearReviews()
        }
    }
    LaunchedEffect(reviews.value.size) {
       // log(reviews.value.size.toString())
        if (reviews.value.isNotEmpty()) {
            val username = reviews.value[0].username
            val pos = username.indexOf(' ')
            reviews.value[0].username = username.substring(pos+1)
            countReviews.value = username.substring(0, pos).toInt()
        }
    }
    val interaction = remember { MutableInteractionSource() }
    val scrollState = rememberScrollState()
    BoxWithConstraints(modifier = Modifier
        .fillMaxSize()
        .clickable(
            interactionSource = interaction,
            indication = null
        ) {}
        .background(BgScreenDark)
    ){
        val size = this.maxWidth/*with(LocalDensity) {
           32.dp.roundToPx().toFloat()
        }*/
            Column(
                modifier = Modifier
                    .verticalScroll(scrollState)
                    .fillMaxSize()
                    .padding(all = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
            Box(
                Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .size(size)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                ShowProductImages(
                    modifier = Modifier
                        .width(size - 32.dp)
                        .height(size - 32.dp)
                        .padding(all = 8.dp), reduce = false, product = product
                ) {
                    indexImage.value = it
                }
            }

            product.linkimages?.let{links ->
                val count = links.size
                if (count > 1 )
                    ShowImagesIndicator(modifier = Modifier, index = indexImage, count = count)
                else
                    DividerVertical(size = 16.dp)
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                TextPanel(
                    text = "-${product.discount}%",
                    textColor = Color.White,
                    backgroundColor = BgDiscount
                )
                val textSale = product.getTypeSale()
                if (textSale.isNotBlank()) {
                    DividerHorizontal(size = 4.dp)
                    TextPanel(
                        text = textSale,
                        textColor       = TextOrange,
                        backgroundColor = PrimaryDark
                    )
                }
            }
            Row(modifier = Modifier
                .fillMaxWidth()
                .height(intrinsicSize = IntrinsicSize.Min)
                .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ){
                val questions = 4
                val rate = countReviews.value
                val textRate = if (rate > 0) "$rate ${getRate(rate)}" else getStringResource(R.string.text_norate)
                CompositeButton(
                    modifier = Modifier.weight(1f),
                    top = {
                        StarPanel(count = product.star)
                    },
                    bottom = {
                        Text(modifier = Modifier.padding(top = 4.dp), text = textRate, color = SelectedItemBottomNavi, fontSize = 13.sp)
                    })
                CompositeButton(
                    modifier = Modifier.weight(1f),
                    top = {
                        Row(modifier = Modifier.padding(top = 8.dp), verticalAlignment = Alignment.CenterVertically){
                            Image(
                                imageVector = ImageVector.vectorResource(R.drawable.ic_review),
                                colorFilter = ColorFilter.tint(TextFieldFont),
                                contentDescription = null
                            )
                            Text(modifier = Modifier.padding(start = 4.dp), text = "$rate", color = TextFieldFont, fontSize = 13.sp)
                        }
                    },
                    bottom = {
                        Text(modifier = Modifier.padding(bottom = 8.dp), text = stringResource(id = R.string.text_review), color = SelectedItemBottomNavi, fontSize = 13.sp)
                    }) {
                }
                CompositeButton(
                    modifier = Modifier.weight(1f),
                    top = {
                        Row(modifier = Modifier.padding(top = 8.dp), verticalAlignment = Alignment.CenterVertically){
                            Image(
                                imageVector = ImageVector.vectorResource(R.drawable.ic_question),
                                colorFilter = ColorFilter.tint(TextFieldFont),
                                contentDescription = null
                            )
                            Text(modifier = Modifier.padding(start = 4.dp), text = "$questions", color = TextFieldFont, fontSize = 13.sp)
                        }
                    },
                    bottom = {
                        Text(modifier = Modifier.padding(bottom = 8.dp), text = stringResource(id = R.string.text_question), color = SelectedItemBottomNavi, fontSize = 13.sp)
                    }) {
                }
            }

            Box(modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .fillMaxWidth()
                .background(PrimaryDark)
            ) {
                 Row(modifier = Modifier
                     .padding(all = 12.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .fillMaxSize()
                    .background(TextFieldBg.copy(alpha = 0.3f))) {
                     Column(){
                         CompositeButton(
                             color = BgTextPrice,
                             modifier = Modifier,//.weight(1f),
                             top = {
                                 Text(modifier = Modifier.padding(start = 4.dp), text = "10 990Р", color = TextFieldFont, fontSize = 13.sp)
                             },
                             bottom = {
                                 Text(modifier = Modifier.padding(bottom = 8.dp), text = stringResource(id = R.string.text_question), color = SelectedItemBottomNavi, fontSize = 13.sp)
                             })

                     }
                }
            }

        }
    }
}