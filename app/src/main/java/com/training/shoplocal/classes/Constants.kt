package com.training.shoplocal.classes

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageBitmapConfig
import com.training.shoplocal.AppShopLocal
import com.training.shoplocal.R
import com.training.shoplocal.ui.theme.SelectedItem
import com.training.shoplocal.ui.theme.TextFieldBg
import java.text.DecimalFormatSymbols

enum class ComposeView {
    LOGIN,
    MAIN,
    CATALOG,
    CART,
    PROFILE,
    SEARCH_EDITVALUE,
    SEARCH,
    FILTER
}

const val SIZE_PORTION =  6     //количество записей в порции при подгрузке сиаска продуктов из БД
const val SERVER_URL            = "http://192.168.1.10"
val DECIMAL_CEPARATOR           = DecimalFormatSymbols().decimalSeparator
val EMPTY_IMAGE = ImageBitmap(1,1, hasAlpha = true, config = ImageBitmapConfig.Argb8888)
val TEXT_BOTTOMNAVIGATION: Array<String> = AppShopLocal.appContext().resources.getStringArray(R.array.bottom_navigation_items)
const val DEFAULT_STRRESOURCE_VALUE  = ""
const val FILE_PREFERENCES      = "settings"
enum class MESSAGE(@DrawableRes val icon: Int, val color: Color) {
    ERROR   (R.drawable.ic_error,           SelectedItem),
    INFO    (R.drawable.ic_notifications,   TextFieldBg),
    WARNING (R.drawable.ic_warning,         TextFieldBg)
}