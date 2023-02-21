package com.training.shoplocal

import android.content.Context
import android.content.res.Resources
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Base64
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.ImageBitmap
import com.training.shoplocal.AppShopLocal.Companion.appContext
import com.training.shoplocal.classes.EMPTY_STRING
import java.math.RoundingMode
import java.nio.charset.Charset
import java.text.DecimalFormat
import kotlin.math.roundToInt

//const val DEFAULT_STRRESOURCE_VALUE  = ""
fun getStringResource(@StringRes id: Int): String =
    try {
        AppShopLocal.appContext().getString(id)
    }
    catch (e: Exception){
        EMPTY_STRING
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
    return dec.format(result) + getStringResource(R.string.currency)
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
fun getRate(rate: Int): String{
    val aRate = AppShopLocal.appContext().resources.getStringArray(R.array.rate)
     return when (rate.toString().last().toString().toInt()) {
        1     -> aRate[0]
        2,3,4 -> aRate[1]
        else  -> aRate[2]
    }
/*    1  оценка
    2, 3, 4         оценки
    0, 5, 6, 7, 8, 9    оценок*/
}

fun getSalePrice(price: Float, percent: Int): String{
    val result = price - (price * percent/100f)
    return getFormattedPrice(result)
}

