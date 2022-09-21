package com.training.shoplocal.buttonpanel

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.training.shoplocal.AppShopLocal
import com.training.shoplocal.AppShopLocal.Companion.appContext
import com.training.shoplocal.R
import com.training.shoplocal.getStringResource
import javax.crypto.Cipher

class UserFingerPrint(private val context: Context): UserFingerPrintInterface {
    override var userFingerPrintListener: UserFingerPrintListener? = null

    override fun authenticate(cryptoObject: Cipher?): Boolean {
        if (!canAuthenticate() || cryptoObject == null)
            return false
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(getStringResource(R.string.biometric_title))
            .setConfirmationRequired(false)
            .setNegativeButtonText(getStringResource(R.string.button_cancel))
            .build()


        val biometricPrompt = createBiometricPrompt()
        biometricPrompt.authenticate(
            promptInfo,
            BiometricPrompt.CryptoObject(cryptoObject)
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
        fun canAuthenticate() =
            BiometricManager.from(appContext())
                .canAuthenticate(BIOMETRIC_STRONG) == BiometricManager.BIOMETRIC_SUCCESS
    }
}