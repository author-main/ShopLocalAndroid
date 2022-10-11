package com.training.shoplocal

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.annotation.StringRes
import com.training.shoplocal.AppShopLocal.Companion.appContext

const val DEFAULT_RESOURCE_STR = ""
const val FILE_PREFERENCES = "settings"
const val SERVER_URL = "http://192.168.0.10"

fun getStringResource(@StringRes id: Int): String =
    try {
        AppShopLocal.appContext().getString(id)
    }
    catch (e: Exception){
        DEFAULT_RESOURCE_STR
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
  /*  if (!connected && showToast)
        showToast(R.string.error_connected_internet)*/
    return connected
}

    fun mToast(value: String){
        Toast.makeText(appContext(), value, Toast.LENGTH_LONG).show()
    }