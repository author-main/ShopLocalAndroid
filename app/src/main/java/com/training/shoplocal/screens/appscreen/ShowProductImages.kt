package com.training.shoplocal.screens.appscreen

import android.text.Layout.Alignment
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.training.shoplocal.classes.Product
import com.training.shoplocal.log
import com.training.shoplocal.md5
import com.training.shoplocal.screens.mainscreen.ImageLink

@Composable
fun ShowProductImages(modifier: Modifier, product: Product){
    val product = remember {
        product
    }
    /*val md5link = remember {
        var entries = emptyArray<String>()
        product.linkimages?.forEach {
            entries += md5(it)
        }
        entries
    }*/

        LazyColumn(modifier = modifier
            .background(Color.White)) {

        }

   //log("Product ${product.name}")
}