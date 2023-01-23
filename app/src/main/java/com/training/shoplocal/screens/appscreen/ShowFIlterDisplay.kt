package com.training.shoplocal.screens.appscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.training.shoplocal.ui.theme.BgScreenDark
import com.training.shoplocal.ui.theme.PrimaryDark
import com.training.shoplocal.ui.theme.TextFieldBg

data class ItemFilter(
    val id: Int,
    val name: String,
    val header: Boolean = false,
    val linkImage: String,
    var selected: Boolean = false
)

@Composable
fun ShowFilterDisplay(){
    @Composable
    fun showRangePrice(valueFrom: Float, valueTo: Float){

    }

    @Composable
    fun showDiscount(value: Int){

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