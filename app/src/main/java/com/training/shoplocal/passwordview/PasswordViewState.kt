package com.training.shoplocal.passwordview

import android.os.Parcelable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.setValue

class PasswordViewState {
    private var password by mutableStateOf("")

    @JvmName("getPassword1")
    fun getPassword() = password

    fun changePassword(value: String) {
        password = value
    }

    fun getPasswordChar(): CharArray {
        val array = arrayEmptyChar.clone()
        password.forEachIndexed { index, _ ->
            array[index] = fillChar
        }
        return array
    }

    companion object {
        private const val PASSWORD_LENGTH = 5
        private const val emptyChar   = '○'
        private const val fillChar    = '●'
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