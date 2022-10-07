package com.training.shoplocal.userfingerprint

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.training.shoplocal.AppShopLocal.Companion.appContext
import com.training.shoplocal.R
import com.training.shoplocal.getStringResource

class UserFingerPrint(private val context: Context): UserFingerPrintInterface {
    //private  var userPasswordStorage: UserPasswordStorageInterface = UserPasswordStorage()//null
    override var userFingerPrintListener: UserFingerPrintListener? = null
   /* fun addPasswordStorage(storage: UserPasswordStorageInterface){
        userPasswordStorage = storage
    }*/

    //override fun authenticate(cryptoObject: Cipher?): Boolean {
    override fun authenticate(): Boolean {
        if (!canAuthenticate())
            return false
        val cipher = userPasswordStorage.getDecryptCipher() ?: return false
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(getStringResource(R.string.biometric_title))
            .setConfirmationRequired(false)
            .setNegativeButtonText(getStringResource(R.string.button_cancel))
            .build()
        val biometricPrompt = createBiometricPrompt()
        biometricPrompt.authenticate(
            promptInfo,
            BiometricPrompt.CryptoObject(cipher)
        )
        return true
    }

    private fun createBiometricPrompt(): BiometricPrompt{
        val executor = ContextCompat.getMainExecutor(context)
        val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                userFingerPrintListener?.onComplete(null)
            }
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                // здесь можно извлечь cryptoObject
                userFingerPrintListener?.onComplete(result.cryptoObject?.cipher)
            }
        }
        return BiometricPrompt(context as FragmentActivity, executor, callback)
    }

    companion object {
        private val userPasswordStorage: UserPasswordStorageInterface = UserPasswordStorage()//null
        fun canAuthenticate() =
            BiometricManager.from(appContext())
                .canAuthenticate(BIOMETRIC_STRONG) == BiometricManager.BIOMETRIC_SUCCESS

        fun existPasswordStore() =
            userPasswordStorage.existPasswordStore()
    }

    fun putPassword(value: String) {
        userPasswordStorage.putPassword(value)
    }

    /*fun existPasswordStore() =
        userPasswordStorage.existPasswordStore()*/

    /*fun getPassword(value: String): String {
        userPasswordStorage?.getPassword()
    }*/

}