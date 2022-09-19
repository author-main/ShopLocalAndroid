package com.training.shoplocal.repository

import com.training.shoplocal.passwordview.PasswordViewState

class Repository: CrudInterface {
    val passwordState = PasswordViewState.getPasswordState()
}