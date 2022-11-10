package com.training.shoplocal.repository

import com.training.shoplocal.loginview.AccessUserInterface

class AccessUser: AccessUserInterface {
    override fun onLogin(
        action: ((result: Int) -> Unit)?,
        email: String,
        password: String,
        finger: Boolean
    ) {
        TODO("Not yet implemented")
    }

    override fun onRegisterUser(action: ((result: Boolean) -> Unit)?, vararg userdata: String) {
        TODO("Not yet implemented")
    }

    override fun onRestoreUser(
        action: ((result: Boolean) -> Unit)?,
        email: String,
        password: String
    ) {
        TODO("Not yet implemented")
    }

    override fun onFingerPrint(email: String) {
        TODO("Not yet implemented")
    }
}