package com.training.shoplocal

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.training.shoplocal.AppShopLocal.Companion.appContext
import com.training.shoplocal.ui.theme.SelectedItem
import com.training.shoplocal.ui.theme.TextFieldBg
import java.text.DecimalFormat

enum class Field_Filter {
    SORT_TYPE,
    SORT_PROPERTY,
    BREND,
    CATEGORY,
    FAVORITE,
    PRICE_RANGE
}

val TEXT_BOTTOMNAVIGATION: Array<String> = AppShopLocal.appContext().resources.getStringArray(R.array.bottom_navigation_items)
const val DEFAULT_STRRESOURCE_VALUE  = ""
const val FILE_PREFERENCES      = "settings"
const val SERVER_URL            = "http://192.168.0.10"

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

fun log(value: String) {
    Log.v("shoplocal", value)
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

fun getPrice(value: Float): String{
    val dec = DecimalFormat("#,###.##")
    return dec.format(value) + "P"
}