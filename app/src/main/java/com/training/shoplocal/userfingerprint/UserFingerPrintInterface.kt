package com.training.shoplocal.userfingerprint

interface UserFingerPrintInterface {
    var userFingerPrintListener: UserFingerPrintListener?
    fun authenticate(): Boolean
}