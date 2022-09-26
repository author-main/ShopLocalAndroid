package com.training.shoplocal.userfingerprint

import javax.crypto.Cipher

interface UserFingerPrintListener {
    fun onComplete(cipher: Cipher?)
}