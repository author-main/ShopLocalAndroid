package com.training.shoplocal.dialogs

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext

class CustomToast(context: Context) : Toast(context) {
    @Composable
    fun MakeText(
        message: String,
        duration: Int = LENGTH_LONG,
        //type: SweetToastProperty,
        padding: PaddingValues,
        contentAlignment: Alignment
    ) {
        val context = LocalContext.current
        val views = ComposeView(context)
    }

}