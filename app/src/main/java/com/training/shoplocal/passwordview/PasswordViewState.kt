package com.training.shoplocal.passwordview

import android.os.Parcelable
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.setValue

class PasswordViewState {
    private var password by mutableStateOf("")
    private var animated = false
    private var prev = ""

    @JvmName("getPassword1")
    fun getPassword() = password

    fun changePassword(value: String) {
        animated =  value.length > password.length
        prev = password
        password = value
    }

    fun isAnimated() = animated

    fun stopAnimate() {
        if (animated) {
            Log.v("shoplocal", "stop animate")
            animated = false
        }
    }

    fun getPasswordChar(): CharArray {
        val array = arrayEmptyChar.clone()
        password.forEachIndexed { index, _ ->
            array[index] = fillChar
        }
        return array
    }

    companion object {
        const val PASSWORD_LENGTH = 5
        const val emptyChar   = '○'
        const val fillChar    = '●'
        private val arrayEmptyChar: CharArray = CharArray(PASSWORD_LENGTH).apply {
            for (index in 0 until PASSWORD_LENGTH)
                this[index] = emptyChar
        }

        fun getPasswordState(value: String = "") = PasswordViewState().apply { this.password = value }

        @Suppress("UNCHECKED_CAST")
        val Saver: Saver<PasswordViewState,Any> = listSaver(
            save = { listOf(it.password) },
            restore = {
                getPasswordState( value = it[0] as String )
            }
        )
    }
}