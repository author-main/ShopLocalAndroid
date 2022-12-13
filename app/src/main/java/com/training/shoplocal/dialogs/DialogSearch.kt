package com.training.shoplocal.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.training.shoplocal.DialogRouter

@Composable
fun DialogSearch(textSearch: String){
    /*Column(){
        Spacer(modifier = Modifier.height(50.dp))
        Box(Modifier.background(Color.Red)
            .fillMaxSize()
        ){
            Text(text = textSearch)
        }
    }*/
    Box(Modifier.fillMaxSize()
        .background(Color.Red)
    ){
         Text(modifier = Modifier.clickable {
            DialogRouter.reset()
         }
             ,
             text = textSearch
         )
    }
}

/*@Composable
fun CustomText(y: Dp){
    Layout(content = { Text(text = "Lorem Ipsum") }){measurables, constraints ->
        val text = measurables[0].measure(constraints)
        layout(constraints.maxWidth, constraints.maxHeight){ //Change these per your needs
            text.placeRelative(IntOffset(0, y.value.roundToInt() - text.height))
        }
    }
}*/