package com.training.shoplocal.userfingerprint

import javax.crypto.Cipher

interface UserPasswordStorageInterface {
    fun removePassword()
    fun putPassword(password: String)
    fun getPassword(cipher: Cipher): String?
    fun existPasswordStore(): Boolean
    fun getDecryptCipher(): Cipher?
}