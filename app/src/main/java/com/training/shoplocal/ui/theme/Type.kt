package com.training.shoplocal.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.training.shoplocal.R

// Set of Material typography styles to start with
val RobotoLight = FontFamily(
    Font(R.font.roboto_light)
)

val Typography = Typography(
    h4 = TextStyle(
        fontFamily = RobotoLight,
        fontSize = 14.sp
    ),

    body1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    )
    /* Other default text styles to override
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
    */
)

/*
            weight = FontWeight.W900,
            style = FontStyle.Normal
        ),
        Font(
            resId = R.font.roboto_black_italic,
            weight = FontWeight.W900,
            style = FontStyle.Italic
        ),
        Font(
            resId = R.font.roboto_bold,
            weight = FontWeight.W700,
            style = FontStyle.Normal
        ),
        ...
    )
)

private val defaultTypography = Typography()
val appTypography = Typography(
    h1 = defaultTypography.h1.copy(fontFamily = appFontFamily),
    h2 = defaultTypography.h2.copy(fontFamily = appFontFamily),
    h3 = defaultTypography.h3.copy(fontFamily = appFontFamily),
    h4 = defaultTypography.h4.copy(fontFamily = appFontFamily),
    h5 = defaultTypography.h5.copy(fontFamily = appFontFamily),
    h6 = defaultTypography.h6.copy(fontFamily = appFontFamily),
    subtitle1 = defaultTypography.subtitle1.copy(fontFamily = appFontFamily),
    subtitle2 = defaultTypography.subtitle2.copy(fontFamily = appFontFamily),
    body1 = defaultTypography.body1.copy(fontFamily = appFontFamily),
    body2 = defaultTypography.body2.copy(fontFamily = appFontFamily),
    button = defaultTypography.button.copy(fontFamily = appFontFamily),
    caption = defaultTypography.caption.copy(fontFamily = appFontFamily),
    overline = defaultTypography.overline.copy(fontFamily = appFontFamily)
)
The combination of weight and style allows us to cover all cases for the application. You can find information about the weight of each Roboto font here.

In addition to this, we should configure a Typography object with the following parameters:

h1 is the largest headline, reserved for short and important text.
h2 is the second-largest headline, reserved for short and important text.
h3 is the third-largest headline, reserved for short and important text.
h4 is the fourth-largest headline, reserved for short and important text.
h5 is the fifth-largest headline, reserved for short and important text.
h6 is the sixth-largest headline, reserved for short and important text.
subtitle1 is the largest subtitle and is typically reserved for medium-emphasis text that is shorter in length.
subtitle2 is the smallest subtitle and is typically reserved for medium-emphasis text that is shorter in length.
body1 is the largest body and is typically reserved for a long-form text that is shorter in length.
body2 is the smallest body and is typically reserved for a long-form text that is shorter in length.
button is reserved for a button text.
caption is one of the smallest font sizes it reserved for annotating imagery or introduce a headline.
overline is one of the smallest font sizes.
I recommend checking the "Typography: Type scale" section of the material design website.

The last step is adding the Typography object to MaterialTheme function as a parameter:

MaterialTheme(typography = appTypography) {
    ...
}
Note: I recommend configuring all @Preview functions in the same way to have a correct preview.

So, let's rework the ingredients text for coffee list item.

@Composable
private fun CoffeeDrinkIngredient(ingredients: String) {
    Text(
        text = ingredients,
        modifier = Modifier.padding(end = 8.dp) +
                Modifier.drawOpacity(0.54f),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        style = MaterialTheme.typography.body1,
        color = MaterialTheme.colors.onSurface
    )
}
 */