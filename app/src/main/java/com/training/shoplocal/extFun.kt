package com.training.shoplocal

import android.content.Context
import android.content.res.Resources
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Base64
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageBitmapConfig
import com.training.shoplocal.AppShopLocal.Companion.appContext
import com.training.shoplocal.ui.theme.SelectedItem
import com.training.shoplocal.ui.theme.TextFieldBg
import java.math.RoundingMode
import java.nio.charset.Charset
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Base64.getEncoder
import kotlin.math.roundToInt
const val SIZE_PORTION =  6     //количество записей в порции при подгрузке сиаска продуктов из БД
const val SERVER_URL            = "http://192.168.1.10"
val DECIMAL_CEPARATOR           = DecimalFormatSymbols().decimalSeparator

/*enum class Error {
    NO_CONNECTION,      // нет соединения
    IMAGE_NOTFOUND,     // файл изображения не найден
    IMAGE_NOTLOADED     // ошибка в процессе загрузки
}*/

val EMPTY_IMAGE = ImageBitmap(1,1, hasAlpha = true, config = ImageBitmapConfig.Argb8888)
val TEXT_BOTTOMNAVIGATION: Array<String> = AppShopLocal.appContext().resources.getStringArray(R.array.bottom_navigation_items)
const val DEFAULT_STRRESOURCE_VALUE  = ""
const val FILE_PREFERENCES      = "settings"
enum class MESSAGE(@DrawableRes val icon: Int, val color: Color) {
    ERROR   (R.drawable.ic_error,           SelectedItem),
    INFO    (R.drawable.ic_notifications,   TextFieldBg),
    WARNING (R.drawable.ic_warning,         TextFieldBg)
}

fun getStringResource(@StringRes id: Int): String =
    try {
        AppShopLocal.appContext().getString(id)
    }
    catch (e: Exception){
        DEFAULT_STRRESOURCE_VALUE
    }

fun<T> log(value: T?) {
    if (value != null) {
        if (value is String)
            Log.v("shoplocal", value)
        else
            Log.v("shoplocal", value.toString())
    }
}

fun validateMail(email: String): Boolean {
    return !(email.isBlank() || !Patterns.EMAIL_ADDRESS.matcher(email).matches())
}


fun isConnectedNet(): Boolean{
    var connected = false
    val connectivityManager = appContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val  n = connectivityManager.activeNetwork
    n?.let{
        val capabilities = connectivityManager.getNetworkCapabilities(n)
        if (capabilities != null)
            connected = capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || capabilities.hasTransport(
                NetworkCapabilities.TRANSPORT_WIFI
            );
    }
    return connected
}

fun mToast(value: String){
    Toast.makeText(appContext(), value, Toast.LENGTH_LONG).show()
}

fun getFormattedPrice(value: Float): String{
    val result = value.roundToInt()
    val dec = DecimalFormat("#,###.00")
    dec.roundingMode = RoundingMode.HALF_EVEN
    return dec.format(result) + "P"
}

fun getSalePrice(price: Float, discount: Int): String{
    val result = price - (price * discount/100f)
    return getFormattedPrice(result)
}

val Int.Dp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()

val Int.Px: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

fun ImageBitmap.isEmpty(): Boolean =
    this.width == 1 || this.height == 1

fun encodeBase64(value: String): String //{

    //val result
    = Base64.encodeToString(value.toByteArray(charset = Charset.defaultCharset()), Base64.NO_WRAP)
    //log("encode $result")
    /*val decode = Base64.decode(result, Base64.NO_WRAP).decodeToString()
    log("decode $decode")*/
   // return result
//}

