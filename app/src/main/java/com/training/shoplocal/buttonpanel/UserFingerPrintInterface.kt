package com.training.shoplocal.buttonpanel

import javax.crypto.Cipher

interface UserFingerPrintInterface {
    var userFingerPrintListener: UserFingerPrintListener?
    fun authenticate(cryptoObject: Cipher?): Boolean
}