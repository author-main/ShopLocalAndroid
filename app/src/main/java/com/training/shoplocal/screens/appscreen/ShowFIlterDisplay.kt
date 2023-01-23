package com.training.shoplocal.screens.appscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.training.shoplocal.classes.fodisplay.OrderDisplay
import com.training.shoplocal.classes.fodisplay.ProviderDataDisplay
import com.training.shoplocal.ui.theme.BgScreenDark
import com.training.shoplocal.ui.theme.PrimaryDark
import com.training.shoplocal.ui.theme.TextFieldBg

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

    }

    @Composable
    fun showDiscount(value: Int){
        CustomCheckBox(){checked ->

        }
    }

    @Composable
    fun showFilterItem(item: ItemFilter, onClick: () -> Unit){

    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(BgScreenDark)
    ){
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(all = 16.dp)) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.1f)
                ,
                //elevation = 4.dp,
                //shape = RoundedCornerShape(20.dp),
                backgroundColor = PrimaryDark,
            ) {
            }
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.1f),
                backgroundColor = PrimaryDark
            ) {
            }
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.8f),
                backgroundColor = PrimaryDark
            ) {
            }
        }
    }
}