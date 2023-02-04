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
//---------------------------------------------------------------------------------------------
    const val SERVER_URL = "http://192.168.0.10"
//---------------------------------------------------------------------------------------------
enum class ComposeView {
    LOGIN,
    MAIN,
    CATALOG,
    CART,
    PROFILE,
    SEARCH_EDIT,
    SEARCH,
    FILTER
}
const val CATEGORY_ITEM     = -1
const val BREND_ITEM        = -2
const val NO_OPEN_ITEM      = -1000
const val EMPTY_STRING = ""
const val ANY_VALUE    = -1
const val SIZE_MEMORYCACHE =  8 // значение в Мб
const val SIZE_DRIVECACHE =  50 // значение в Мб
const val SIZE_PORTION =  8     //количество записей в порции при подгрузке списка продуктов из БД
val DECIMAL_SEPARATOR           = DecimalFormatSymbols().decimalSeparator
val EMPTY_IMAGE = ImageBitmap(1,1, hasAlpha = true, config = ImageBitmapConfig.Argb8888)
val TEXT_BOTTOMNAVIGATION: Array<String> = AppShopLocal.appContext().resources.getStringArray(R.array.bottom_navigation_items)
const val FILE_PREFERENCES      = "settings"
enum class MESSAGE(@DrawableRes val icon: Int, val color: Color) {
    ERROR   (R.drawable.ic_error,           SelectedItem),
    INFO    (R.drawable.ic_notifications,   TextFieldBg),
    WARNING (R.drawable.ic_warning,         TextFieldBg)
}