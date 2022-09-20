package com.training.shoplocal.buttonpanel

import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import com.training.shoplocal.AppShopLocal.Companion.appContext

class UserFingerPrint {
    fun canAuthenticate() =
        BiometricManager.from(appContext())
        .canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL) == BiometricManager.BIOMETRIC_SUCCESS
}