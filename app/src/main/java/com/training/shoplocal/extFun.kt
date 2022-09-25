package com.training.shoplocal

import android.util.Log
import androidx.annotation.StringRes

const val defaultStringResource = ""
fun getStringResource(@StringRes id: Int): String =
    try {
        AppShopLocal.appContext().getString(id)
    }
    catch (e: Exception){
        defaultStringResource
    }

fun log(value: String) {
    Log.v("shoplocal", value)
}