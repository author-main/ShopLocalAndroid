package com.training.shoplocal

import android.app.Application
import android.content.Context
import com.training.shoplocal.dagger.AppComponent
import com.training.shoplocal.dagger.DaggerAppComponent
import com.training.shoplocal.loginview.LoginViewState
import com.training.shoplocal.repository.AccessUser
import com.training.shoplocal.repository.DatabaseCRUD
//import com.training.shoplocal.dagger.ProviderCacheParam
import com.training.shoplocal.repository.Repository
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector

import javax.inject.Inject


class AppShopLocal: Application() {//, HasAndroidInjector {
    @Inject
    lateinit var repository: Repository

    private lateinit var appComponent: AppComponent
    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent
            .factory().create(
            applicationInfo.dataDir + "/cache/",
            8
        )
        appComponent.injectApplication(this)
   }

    init {
        instance = this
    }

    companion object {
        val appComponent: AppComponent
            get () = instance.appComponent
        private lateinit var instance: AppShopLocal
        fun appRepository() = instance.repository
        fun appContext(): Context =
            instance.applicationContext

    }
}
