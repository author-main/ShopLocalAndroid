package com.training.shoplocal

import android.app.Application
import com.training.shoplocal.repository.Repository

class AppShopLocal: Application() {
    init {
        instance = this
    }
    companion object {
        private lateinit var repository: Repository
        private lateinit var instance: AppShopLocal
        fun appRepository(): Repository {
            if (!this::repository.isInitialized)
                repository = Repository()
            return repository
        }
        fun appContext() =
            instance.applicationContext
    }
}