package com.training.shoplocal.loginview

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.training.shoplocal.ScreenItem
import com.training.shoplocal.ScreenRouter
import com.training.shoplocal.buttonpanel.UserFingerPrint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoginViewState {
    private var password by mutableStateOf("4")
    private var animated = false
    var onLogin: ((password:String) -> Boolean)? = null
    @JvmName("getPassword1")
    fun getPassword() = password

    fun changeChar(value: Char){
        animated = false
        if (value == ' ') {
            return
        }
        if (value == '<') {
            if (password.isNotEmpty()){
                password = password.dropLast(1)
            }
        } else
            if (password.length < 5) {
                animated = true
                password += value
                if (password.length == 5) {
                    if (onLogin?.invoke(password) == true)
                        ScreenRouter.navigateTo(ScreenItem.MainScreen)
                    else {
                        CoroutineScope(Dispatchers.Main).launch {
                            delay(500)
                            password = ""
                        }
                    }
                    //onLogin?.invoke(password)
                }
            }
    }

    fun changePassword(value: String) {
       // animated =  value.length > password.length
        password = value
    }

    fun isAnimated() = animated

    /* fun stopAnimate() {
         if (animated) {
           //  Log.v("shoplocal", "stop animate")
             animated = false
         }
     }*/

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

        fun getPasswordState(value: String = "") = LoginViewState().apply { this.password = value }

     /*   @Suppress("UNCHECKED_CAST")
        val Saver: Saver<PasswordViewState,Any> = listSaver(
            save = { listOf(it.password) },
            restore = {
                getPasswordState( value = it[0] as String )
            }
        )*/
    }
}