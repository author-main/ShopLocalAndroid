package com.training.shoplocal.buttonpanel.userfingerprint

import javax.crypto.Cipher

interface UserFingerPrintInterface {
    var userFingerPrintListener: UserFingerPrintListener?
    fun authenticate(): Boolean
}