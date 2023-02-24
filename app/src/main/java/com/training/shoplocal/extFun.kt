package com.training.shoplocal

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Base64
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.annotation.ArrayRes
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.ImageBitmap
import com.training.shoplocal.AppShopLocal.Companion.appContext
import com.training.shoplocal.classes.DECIMAL_SEPARATOR
import com.training.shoplocal.classes.EMPTY_STRING
import com.training.shoplocal.classes.WORD_RATE
import com.training.shoplocal.classes.WORD_REVIEW
import java.math.RoundingMode
import java.nio.charset.Charset
import java.text.DecimalFormat
import kotlin.math.roundToInt

//const val DEFAULT_STRRESOURCE_VALUE  = ""

fun getStringArrayResource(@ArrayRes id: Int): Array<String> =
    AppShopLocal.appContext().resources.getStringArray(id)

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

fun getAfterWord(count: Int, what: Int): String {
    val idResource = when (what) {
        WORD_RATE   -> R.array.rate
        WORD_REVIEW -> R.array.review
        else        -> -1
    }
    if (idResource == -1)
        return EMPTY_STRING
    val aWord = getStringArrayResource(idResource)
    var sCount = count.toString()
    val length = sCount.length
    if (length >=2 )
        sCount = sCount.substring(length - 2)
    val iCount = sCount.toInt()
    if (iCount in 11..14)
        return aWord[2]
    return when (iCount.toString().last().toString().toInt()) {
        1     -> aWord[0]
        2,3,4 -> aWord[1]
        else  -> aWord[2]
    }
}



/*fun getReview(count: Int) : String {
/*  1                    отзыв
    2, 3, 4             отзыва
    0, 5, 6, 7, 8, 9    отзывов */
    val aReview = getStringArrayResource(R.array.review)
    var sReview = count.toString()
    if (sReview.length >=2 )
        sReview = sReview.substring(sReview.length - 2)

    val iReview = sReview.toInt()
    if (iReview in 11..14)
        return aReview[2]

    return when (iReview.toString().last().toString().toInt()) {
        1     -> aReview[0]
        2,3,4 -> aReview[1]
        else  -> aReview[2]
    }
}

fun getRate(rate: Int): String{
    val aRate = getStringArrayResource(R.array.rate)
    var sRate = rate.toString()
    if (sRate.length >=2 )
       sRate = sRate.substring(sRate.length - 2)

    val iRate = sRate.toInt()
    if (iRate in 11..14)
        return aRate[2]

     return when (iRate.toString().last().toString().toInt()) {
        1     -> aRate[0]
        2,3,4 -> aRate[1]
        else  -> aRate[2]
    }
/*          1                   оценка
            2, 3, 4             оценки
            0, 5, 6, 7, 8, 9    оценок*/
}
*/

fun getDiscountPrice(price: Float, percent: Int) =
    price - (price * percent/100f)

fun getSalePrice(price: Float, percent: Int) =
    getFormattedPrice(getDiscountPrice(price, percent))

fun getFormattedStar(value: Float): String{
    val df = DecimalFormat("#.#")
    df.roundingMode = RoundingMode.HALF_EVEN
    return df.format(value)
}




