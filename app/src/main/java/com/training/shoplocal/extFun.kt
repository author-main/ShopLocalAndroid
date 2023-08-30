package com.training.shoplocal

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Base64
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.annotation.ArrayRes
import androidx.annotation.StringRes
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.compose.ui.graphics.ImageBitmap
import com.training.shoplocal.AppShopLocal.Companion.appContext
import com.training.shoplocal.classes.*
import java.math.RoundingMode
import java.nio.charset.Charset
import java.text.DecimalFormat
import kotlin.math.roundToInt

fun getStringArrayResource(@ArrayRes id: Int): Array<String> =
    appContext().resources.getStringArray(id)

fun getStringResource(@StringRes id: Int): String =
    try {
        appContext().getString(id)
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
            )
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

fun encodeBase64(value: String): String
    = Base64.encodeToString(value.toByteArray(charset = Charset.defaultCharset()), Base64.NO_WRAP)

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

fun getDiscountPrice(price: Float, percent: Int) =
    price - (price * percent/100f)

fun getSalePrice(price: Float, percent: Int) =
    getFormattedPrice(getDiscountPrice(price, percent))

fun getFormattedStar(value: Float): String{
    val df = DecimalFormat("#.#")
    df.roundingMode = RoundingMode.HALF_EVEN
    return df.format(value)
}

fun vibrate(duration: Long) {
    val vibe =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager =
                appContext()
                    .getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            appContext()
                .getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
    val effect: VibrationEffect = VibrationEffect.createOneShot(
        duration,
        VibrationEffect.DEFAULT_AMPLITUDE
    )
    vibe.vibrate(effect)
}

fun fingerPrintCanAuthenticate() =
    BiometricManager.from(appContext())
        .canAuthenticate(BIOMETRIC_STRONG) == BiometricManager.BIOMETRIC_SUCCESS

fun existPasswordStore(): Boolean {
    val sharedPrefs: SharedPreferences =
        appContext().getSharedPreferences(FILE_PREFERENCES, Context.MODE_PRIVATE)
    return !sharedPrefs.getString(KEY_PASSWORD, null).isNullOrBlank()
}