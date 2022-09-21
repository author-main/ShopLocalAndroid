package com.training.shoplocal.buttonpanel

import javax.crypto.Cipher

interface UserFingerPrintListener {
    fun onComplete(cryptoObject: Cipher?)
}