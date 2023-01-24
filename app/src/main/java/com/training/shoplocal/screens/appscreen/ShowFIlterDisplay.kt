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
import androidx.compose.ui.platform.LocalFocusManager
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
import com.training.shoplocal.classes.ANY_VALUE
import com.training.shoplocal.getFormattedPrice
import com.training.shoplocal.log
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
    val editFilter = remember {
        OrderDisplay.FilterData(
            brend       = filter.getBrend(),
            favorite    = filter.getFavorite(),
            priceRange  = filter.getPriceRange(),
            category    = filter.getCategory(),
            discount    = filter.getDiscount()
        )
    }
    val focusManager = LocalFocusManager.current
    fun checkNumberValue(value: String, len: Int): String? {
        //log("len = $len")
        if (!value.contains(".") && !value.contains(",") && !value.contains("-") && value.length <= len) {
            val number = try {
                value.toInt()
            } catch (_: java.lang.Exception) {
                0
            }
            return number.toString()
        }
        return null
    }
    @Composable
    fun NumberTextField(modifier: Modifier, value: String, label: String, len: Int, onChange: (text: String) ->Unit){
        var number by remember{
            mutableStateOf(value)
        }
        TextField(
            modifier = modifier,
            value = number,//value.value,
            onValueChange = {
                val newValue = checkNumberValue(it, len)
                if (newValue != null) {
                    //value.value = newValue
                    number = newValue
                    onChange(newValue)
                }

            },
            leadingIcon = { Text(text = label,//stringResource(id = R.string.text_to),
                color = TextFieldFont.copy(alpha = 0.5f)
            ) },
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
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done, keyboardType = KeyboardType.Number),
            keyboardActions = KeyboardActions (
                onDone = {
                    focusManager.clearFocus(true)
                }
            )
        )
    }


    @Composable
    fun showRangePrice(){//valueFrom: Float, valueTo: Float){

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
                NumberTextField(modifier = Modifier.weight(0.5f), value = editFilter.priceRange.first.toInt().toString(),
                    label = stringResource(id = R.string.text_from), len = 6) {
                    val second = editFilter.priceRange.second
                    editFilter.priceRange = it.toFloat() to second
                }

                Spacer(modifier = Modifier.width(8.dp))
                NumberTextField(modifier = Modifier.weight(0.5f), value = editFilter.priceRange.second.toInt().toString(),
                    stringResource(id = R.string.text_to), len = 6) {
                    val first = editFilter.priceRange.first
                    editFilter.priceRange = first to it.toFloat()
                }

            }
        }
    }

    @Composable
    fun showDiscount(value: Int){

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.padding(end = 8.dp),
                text = stringResource(id = R.string.text_discount),
                color = TextFieldFont
            )
            NumberTextField(modifier = Modifier.weight(0.5f), value = editFilter.discount.toString(),
                label = stringResource(id = R.string.text_from),len = 2) {
                editFilter.discount = it.toInt()
            }

            Text(
                modifier = Modifier.padding(start = 4.dp),
                text = "%",
                color = TextFieldFont
            )

            CustomCheckBox(
                modifier = Modifier.padding(start = 24.dp),
                text = stringResource(id = R.string.text_favorite),
                checked = editFilter.favorite == 1
            ) { checked ->
                editFilter.favorite = if (checked) 1 else ANY_VALUE
                focusManager.clearFocus(true)
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
                showRangePrice()//valueFrom = 500f, valueTo = 2000f)
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