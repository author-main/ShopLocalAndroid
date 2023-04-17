package com.training.shoplocal.userfingerprint

interface UserFingerPrintInterface {
    val userPasswordStorage: UserPasswordStorageInterface
    var userFingerPrintListener: UserFingerPrintListener?
    fun authenticate(): Boolean
}