package com.training.shoplocal

import android.app.Application
import android.content.Context
import com.training.shoplocal.repository.Repository

class AppShopLocal: Application() {
    init {
        instance = this
    }
    companion object {
        private lateinit var instance: AppShopLocal
        private lateinit var repository: Repository
        fun appRepository(): Repository {
            if (!this::repository.isInitialized)
                repository = Repository()
            return repository
        }
        fun appContext(): Context =
            instance.applicationContext
    }
}