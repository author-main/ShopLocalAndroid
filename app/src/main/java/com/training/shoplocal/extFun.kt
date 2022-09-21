package com.training.shoplocal

import androidx.annotation.StringRes

const val defaultStringResource = ""
fun getStringResource(@StringRes id: Int): String =
    try {

        AppShopLocal.appContext().getString(id)
    }
    catch (e: Exception){
        defaultStringResource
    }