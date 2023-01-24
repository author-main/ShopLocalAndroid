package com.training.shoplocal.screens.appscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.node.modifierElementOf
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.training.shoplocal.classes.fodisplay.OrderDisplay
import com.training.shoplocal.classes.fodisplay.ProviderDataDisplay
import com.training.shoplocal.ui.theme.BgScreenDark
import com.training.shoplocal.ui.theme.PrimaryDark
import com.training.shoplocal.ui.theme.TextFieldBg
import com.training.shoplocal.R
import com.training.shoplocal.getFormattedPrice
import com.training.shoplocal.ui.theme.TextFieldFont

const val CATEGORY_ITEM = -1
const val BRAND_ITEM    = -2

data class ItemFilter(
    val id: Int,
    val name: String,
    val linkImage: String,
    var selected: Boolean = false
) {
    fun isHeader() =
        id < 0
    fun isCategory() =
        id == CATEGORY_ITEM
    fun isBrand() =
        id == BRAND_ITEM
}

@Composable
fun ShowFilterDisplay(filter: ProviderDataDisplay){
    @Composable
    fun showRangePrice(valueFrom: Float, valueTo: Float){
        var startValue by remember {
            mutableStateOf(getFormattedPrice(valueFrom, false))
        }
        var endValue by remember {
            mutableStateOf(getFormattedPrice(valueTo, false))
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
        ) {
            Text(
                modifier = Modifier.padding(bottom = 8.dp),
                text = "${stringResource(id = R.string.text_price)}, ${stringResource(id = R.string.text_currency)}",
                color = TextFieldFont
            )
            Row(
               // modifier = Modifier
               //     .padding(all = 8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    modifier = Modifier.weight(0.5f),
                    value = startValue,
                    onValueChange = {
                        startValue = it
                    },
                    //modifier = Modifier.width(120.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = TextFieldFont,
                        backgroundColor = TextFieldBg,
                        cursorColor = TextFieldFont,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true,
                    textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.End),
                )
                Text(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    text = "-",
                    color = TextFieldFont
                )
                TextField(
                    modifier = Modifier.weight(0.5f),
                    value = endValue,
                    onValueChange = {
                        endValue = it
                    },
                    //modifier = Modifier.width(120.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = TextFieldFont,
                        backgroundColor = TextFieldBg,
                        cursorColor = TextFieldFont,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true,
                    textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.End),
                )
            }
        }
    }

    @Composable
    fun showDiscount(value: Int){
        var discount by remember {
            mutableStateOf("2")
        }
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(all = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.padding(end = 8.dp),
                text = "${stringResource(id = R.string.text_discount)} ${stringResource(id = R.string.text_from)}",
                color = TextFieldFont
            )

            TextField(value = discount, onValueChange = {
                discount = it
            },
                modifier = Modifier.width(80.dp),
                colors = TextFieldDefaults.textFieldColors(
                    textColor = TextFieldFont,
                    backgroundColor = TextFieldBg,
                    cursorColor = TextFieldFont,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(16.dp),
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.End),
            )
            Text(
                modifier = Modifier.padding(start = 4.dp),
                text = "%",
                color = TextFieldFont
            )

            CustomCheckBox(
                modifier = Modifier.padding(start = 24.dp),
                text = stringResource(id = R.string.text_favorite),
                checked = false
            ) { checked ->

            }
        }
    }

    @Composable
    fun showFilterItem(item: ItemFilter, onClick: () -> Unit){

    }

    Box(modifier = Modifier
        //.fillMaxSize()
        .background(BgScreenDark)
    ){
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(all = 16.dp)) {
            Card(
                /*modifier = Modifier
                    .fillMaxWidth(),*/
                //elevation = 4.dp,
                //shape = RoundedCornerShape(20.dp),

                backgroundColor = PrimaryDark

            ) {

                    showDiscount(value = 20)

            }
            Spacer(modifier = Modifier.height(8.dp))
            Card(
               /* modifier = Modifier
                    .fillMaxWidth(),*/
                backgroundColor = PrimaryDark
            ) {
                showRangePrice(valueFrom = 500f, valueTo = 2000f)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                ,

                backgroundColor = PrimaryDark
            ) {
            }
        }
    }
}