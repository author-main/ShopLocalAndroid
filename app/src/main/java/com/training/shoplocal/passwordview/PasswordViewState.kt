package com.training.shoplocal.passwordview

import android.os.Parcelable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.setValue
import kotlinx.parcelize.Parcelize

class PasswordViewState {
    private val emptyChar   = '○'
    private val fillChar    = '●'
    private var password by mutableStateOf("")
    fun changePassword(value: String) {
        password = value
    }

    fun getPasswordChar(): CharArray {
        val array = CharArray(PASSWORD_LENGTH)
        for (index in 0..4)
            array[index] = emptyChar

        password.forEachIndexed { index, _ ->
            array[index] = fillChar
        }
        return array
    }



    companion object {
        const val PASSWORD_LENGTH = 5
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