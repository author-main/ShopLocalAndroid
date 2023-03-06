package com.training.shoplocal.screens.mainscreen.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.training.shoplocal.R
import com.training.shoplocal.ui.theme.ImageFavoriteOff
import com.training.shoplocal.ui.theme.ImageFavoriteOn
import com.training.shoplocal.ui.theme.SelectedItemBottomNavi

@Composable
fun ButtonFavorite(modifier: Modifier, background: Boolean = false, checked: Boolean = false, onChecked: (value: Boolean) -> Unit){
    var isChecked by remember {
        mutableStateOf(checked)
    }

    Box(modifier =
        modifier
            .toggleable(value = isChecked, role = Role.Checkbox){
                isChecked = it
                onChecked(it)
            }
         /*   .clip(CircleShape)
            .size(24.dp),*/
    ){
        if (background)
            Image(
           /*     modifier = Modifier
                    .align(Alignment.Center)
                    .size(24.dp),*/
                imageVector = ImageVector.vectorResource(R.drawable.ic_favorite),
                colorFilter = ColorFilter.tint(Color.White),
                contentDescription = null
            )
        Image(
            imageVector = if (isChecked) ImageVector.vectorResource(R.drawable.ic_favorite)
            else ImageVector.vectorResource(R.drawable.ic_favorite_border),
            contentDescription = null,
            colorFilter = if (isChecked) ColorFilter.tint(ImageFavoriteOn)
            else ColorFilter.tint(ImageFavoriteOff),
            /*modifier = Modifier
            .size(24.dp)*/
        )
    }
}