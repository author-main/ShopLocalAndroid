package com.training.shoplocal.loginview

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.training.shoplocal.ScreenItem
import com.training.shoplocal.ScreenRouter
import com.training.shoplocal.log
import com.training.shoplocal.validateMail
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoginViewState {
    private var onAccessUser: AccessUserInterface? = null
    fun addOnAccessUser(value: AccessUserInterface) {
        onAccessUser = value
    }

    private var email = ""
    private var pressedButtons = false
    private var focused = false//by mutableStateOf(false)
    private var password by mutableStateOf("")
    private var animated = false

/*    private var force by mutableStateOf(false)

    fun forceRecomposition(){
        force = !force
        log(force.toString())
    }

    fun getForceAction() = force*/

//    var onLogin: ((password:String) -> Boolean)? = null

    @JvmName("getPassword1")
    fun getPassword() = password


    fun setPressedButtons(value: Boolean) {
        pressedButtons = value
    }

    fun setFocus(value: Boolean) {
        focused = value
    }

    fun isFocused() =
        focused


    fun setEmail(value: String) {
        email = value
    }

    fun getEmail() = email

    fun isPressedButtons() =
        pressedButtons

    fun changeChar(value: Char){
        pressedButtons = true
      /*  if (focused)
            focused = false*/
        animated = false
        if (value == ' ') {
           /* if (!validateMail(email))
                return*/
            onAccessUser?.onFingerPrint(email)
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
                    if (onAccessUser?.onLogin(password) == true)
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

    fun stopAnimate() {
         if (animated)
             animated = false
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