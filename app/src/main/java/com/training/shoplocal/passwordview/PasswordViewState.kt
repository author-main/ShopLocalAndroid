package com.training.shoplocal.passwordview

import android.os.Parcelable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.setValue
import kotlinx.parcelize.Parcelize

class PasswordViewState {
    private val emptyChar   = "○"
    private val fillChar    = "●"
    private var password by mutableStateOf("")
    companion object {
        fun getPasswordState(value: String) = PasswordViewState().apply { this.password = value }

        @Suppress("UNCHECKED_CAST")
        val Saver: Saver<PasswordViewState,Any> = listSaver(
            save = { listOf(it.password) },
            restore = {
                getPasswordState( value = it[0] as String )
            }
        )
    }
}