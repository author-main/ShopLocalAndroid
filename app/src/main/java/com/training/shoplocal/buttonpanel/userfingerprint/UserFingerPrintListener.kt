package com.training.shoplocal.buttonpanel.userfingerprint

import javax.crypto.Cipher

interface UserFingerPrintListener {
    fun onComplete(cryptoObject: Cipher?)
}