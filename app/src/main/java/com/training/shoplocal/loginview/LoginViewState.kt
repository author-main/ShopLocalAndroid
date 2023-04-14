package com.training.shoplocal.loginview

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.training.shoplocal.classes.User
import com.training.shoplocal.classes.arrayEmptyChar
import com.training.shoplocal.classes.fillChar
import com.training.shoplocal.dagger.ActivityMainScope
import com.training.shoplocal.log
import com.training.shoplocal.userfingerprint.UserFingerPrint
import com.training.shoplocal.validateMail
import javax.inject.Inject

@ActivityMainScope
class LoginViewState @Inject constructor() {
    /*private var onAccessUser: AccessUserInterface? = null
    fun addOnAccessUser(value: AccessUserInterface) {
        onAccessUser = value
    }*/

    private var progress by mutableStateOf(false)

    private val userEmail = User.getUserData()?.email ?: ""
    private var email by mutableStateOf(userEmail)
    private var enableFingerButton: MutableState<Boolean> = mutableStateOf(canAuthenticate())

    private var pressedButtons = false
    private var focused = false
    private var password by mutableStateOf("")
    private var errorEmail by mutableStateOf(true)
    private var animated = false

/*    private var force by mutableStateOf(false)

    fun forceRecomposition(){
        force = !force
        log(force.toString())
    }

    fun getForceAction() = force*/

//    var onLogin: ((password:String) -> Boolean)? = null

    fun reset(){
        progress = false
        errorEmail = false
        password = ""
    }

    @JvmName("getErrorEmail1")
    fun getErrorEmail() = errorEmail

    @JvmName("setErrorEmail1")
    fun setErrorEmail(value: Boolean) {
        errorEmail = value
    }

    fun isProgress() = progress

    fun showProgress(){
        fillPassword()
        progress = true
    }

    fun hideProgress(){
        if (progress)
            progress = false
    }

    @JvmName("getPassword1")
    fun getPassword() = password


    private fun canAuthenticate() =
        UserFingerPrint.canAuthenticate() && UserFingerPrint.existPasswordStore()

    fun checkFingerButtonState(){
        enableFingerButton.value = canAuthenticate()
        //log(enableFingerButton.toString())
    }

    fun isEnabledFingerButton() = enableFingerButton


    fun setPressedButtons(value: Boolean) {
        pressedButtons = value
    }

    fun clearPassword(){
        animated = false
        password = "xxxxx"
        password = ""
    }

    private fun fillPassword(){
        animated = false
        if (password.isBlank())
            password = "xxxxx"
    }

    fun setFocus(value: Boolean) {
        focused = value
    }

    fun isFocused() =
        focused


    @JvmName("setEmail1")
    fun setEmail(value: String, check: Boolean = false) {
        email = value
        if (check)
            errorEmail = !validateMail(value)
    }

    @JvmName("getEmail1")
    fun getEmail() = email

    fun isPressedButtons() =
        pressedButtons

    fun changeChar(value: Char){

        /*if (password.length == 5) {
            log ("key down")
            return
        }*/
        pressedButtons = true
         animated = false
        if (value == ' ')
          return
        if (value == '<') {
            if (password.isNotEmpty()){
                password = password.dropLast(1)
            }
        } else
            if (password.length < 5) {
                animated = true
                password += value
                if (password.length == 5)
                    showProgress()
            }
    }

    fun changePassword(value: String) {
        password = value
    }

    fun isAnimated() = animated

    fun stopAnimate() {
         if (animated)
             animated = false
    }

    fun getPasswordChar(): CharArray {
        val array = arrayEmptyChar.clone()
        //if (password!="xxxxx" && password != "")
            password.forEachIndexed { index, _ ->
                array[index] = fillChar
            }
        return array
    }

   /* companion object {
        const val PASSWORD_LENGTH = 5
        const val emptyChar   = '○'
        const val fillChar    = '●'
        private val arrayEmptyChar: CharArray = CharArray(PASSWORD_LENGTH).apply {
            for (index in 0 until PASSWORD_LENGTH)
                this[index] = emptyChar
        }

        fun getLoginState(value: String = "") = LoginViewState().apply { this.password = value }

     /*   @Suppress("UNCHECKED_CAST")
        val Saver: Saver<PasswordViewState,Any> = listSaver(
            save = { listOf(it.password) },
            restore = {
                getPasswordState( value = it[0] as String )
            }
        )*/
    }*/

}