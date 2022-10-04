package com.training.shoplocal

import android.util.Log
import android.util.Patterns
import androidx.annotation.StringRes

const val DEFAULT_RESOURCE_STR = ""

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