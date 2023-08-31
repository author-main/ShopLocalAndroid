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
    //const val SERVER_URL = "http://192.168.1.10"
    const val DIR_IMAGES = "images_ls"
    const val SERVER_URL = "http://faceshot.ru"
//---------------------------------------------------------------------------------------------
const val KEY_PASSWORD = "password"
const val USERMESSAGE_READ   = 0
const val USERMESSAGE_DELETE = 1

const val PERCENT_BLACKFRIDAY = 10
const val LIMIT_BLACKFRIDAY   = 7000f
const val TAB_CHAR   = "    "

const val WORD_REVIEW         = 0
const val WORD_RATE           = 1

enum class Container {
    LOGIN,
    MAIN,
    CATALOG,
    CART,
    PROFILE,
    SEARCH_EDIT,
    SEARCH,
    FILTER,
    DETAIL
}
const val CATEGORY_ITEM     = -1
const val BREND_ITEM        = -2
const val NO_OPEN_ITEM      = -1000
const val EMPTY_STRING = ""
const val ANY_VALUE    = -1
//const val SIZE_DRIVECACHE =  50 // значение в Мб
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

/**
 *  Константы LoginViewState
 */
const val PASSWORD_LENGTH = 5
const val emptyChar   = '○'
const val fillChar    = '●'
val arrayEmptyChar: CharArray = CharArray(PASSWORD_LENGTH).apply {
    for (index in 0 until PASSWORD_LENGTH)
        this[index] = emptyChar
}
const val ID_CATEGORY = 1L
const val ID_BRAND    = 2L