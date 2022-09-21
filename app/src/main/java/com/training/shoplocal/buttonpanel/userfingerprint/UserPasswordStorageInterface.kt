package com.training.shoplocal.buttonpanel.userfingerprint

import javax.crypto.Cipher

interface UserPasswordStorageInterface {
    fun putPassword(password: String)
    fun getPassword(cipher: Cipher): String?
    fun existPasswordStore(): Boolean
    fun getDecryptCipher(): Cipher?
}