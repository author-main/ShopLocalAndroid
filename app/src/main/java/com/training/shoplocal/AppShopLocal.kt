package com.training.shoplocal

import android.app.Application
import android.content.Context
import com.training.shoplocal.dagger.AppComponent
import com.training.shoplocal.dagger.DaggerAppComponent
import com.training.shoplocal.repository.Repository

class AppShopLocal: Application() {
    private lateinit var appComponent: AppComponent
    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .cacheDir(applicationInfo.dataDir + "/cache/")
            .cacheSize(8)
            .build()
   }

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