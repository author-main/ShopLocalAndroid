package com.training.shoplocal.passwordview

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class PasswordViewState(private var password: String = ""): Parcelable {
    companion object {
        fun getPasswordState(value: String) = PasswordViewState().apply { this.password = value }
    }
}