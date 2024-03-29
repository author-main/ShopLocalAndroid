package com.training.shoplocal.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorPalette = darkColors(
    primary = PrimaryDark,//Purple200,
    primaryVariant = Purple700,
    secondary = Teal200
)

private val LightColorPalette = lightColors(
    primary = Purple500,
    primaryVariant = Purple700,
    secondary = Teal200

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun ShopLocalTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }


    val systemUiController = rememberSystemUiController()
    if(darkTheme){
        systemUiController.setSystemBarsColor(
            color = PrimaryDark
        )
    }else{

        /* systemUiController.setSystemBarsColor(
             color = LightColorPalette.primary
         )*/
        /*systemUiController.setStatusBarColor(
            color = LightColorPalette.primary
        )
        systemUiController.setNavigationBarColor(
            color = LightColorPalette.primary
        )*/
        systemUiController.setSystemBarsColor(
            color = LightColorPalette.primary,
            /* transformColorForLightContent = {
                 Color.White
             },*/
            //  darkIcons = false
            //isNavigationBarContrastEnforced = true
        )


    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}