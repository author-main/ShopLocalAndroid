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
    /*@Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>*/
    private lateinit var appComponent: AppComponent
    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent
            .factory().create(
            applicationInfo.dataDir + "/cache/",
            8
        )
     //   appComponent.injectApplication(this)
    /*  appComponent = DaggerAppComponent.builder()
            .cacheDir(applicationInfo.dataDir + "/cache/")
            .cacheSize(8)
            .build()*/
   }

    init {
        instance = this
    }


    companion object {
        val appComponent: AppComponent
            get () = instance.appComponent
        private lateinit var instance: AppShopLocal
        private lateinit var repository: Repository
        fun appRepository(): Repository {
            if (!this::repository.isInitialized)
                repository = Repository(/*AccessUser(
                    LoginViewState()
                ),*/
                DatabaseCRUD())
            return repository
        }
        fun appContext(): Context =
            instance.applicationContext

    }

   /* override fun androidInjector(): AndroidInjector<Any> {
        return dispatchingAndroidInjector
    }*/
}

/** Для получения appComponent создаем свойство-расширение Context.appModule
 * если context является application возвращаем appComponent,
 * в противном случае выполняем рекурсию, извлекаем из текущего context applicationContext
 * и берем из него appComponent
 */
/*val Context.appComponent: AppComponent
    get ()  =
    when (this) {
        is AppShopLocal -> appComponent
        else -> this.applicationContext.appComponent
    }*/
