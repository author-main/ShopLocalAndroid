package com.training.shoplocal.userfingerprint

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.training.shoplocal.R
import com.training.shoplocal.dagger.ActivityMainScope
import com.training.shoplocal.getStringResource
import javax.crypto.Cipher
import javax.inject.Inject

@ActivityMainScope
class UserFingerPrint @Inject constructor(private val context: ComponentActivity): UserFingerPrintInterface {
    @Inject
    override lateinit var userPasswordStorage: UserPasswordStorageInterface// = UserPasswordStorage()
    override var userFingerPrintListener: UserFingerPrintListener? = null
    override fun authenticate(): Boolean {
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

    fun putPassword(value: String) {
        userPasswordStorage.putPassword(value)
    }

    fun getPassword(cipher: Cipher) =
        userPasswordStorage.getPassword(cipher)

    fun removePassword() {
        userPasswordStorage.removePassword()
    }
}
