package com.training.shoplocal.dagger

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.training.shoplocal.MainActivity
import com.training.shoplocal.classes.downloader.DiskCache
import com.training.shoplocal.classes.downloader.ImageDiskCache
import com.training.shoplocal.classes.downloader.ImageMemoryCache
import com.training.shoplocal.classes.downloader.MemoryCache
import com.training.shoplocal.loginview.LoginViewState
import com.training.shoplocal.repository.*
import com.training.shoplocal.repository.retrofit.DatabaseApi
import com.training.shoplocal.repository.retrofit.DatabaseApiInterface
import com.training.shoplocal.userfingerprint.UserFingerPrint
import com.training.shoplocal.userfingerprint.UserFingerPrintInterface
import com.training.shoplocal.userfingerprint.UserPasswordStorage
import com.training.shoplocal.userfingerprint.UserPasswordStorageInterface
import com.training.shoplocal.viewmodel.FactoryViewModel
import com.training.shoplocal.viewmodel.RepositoryViewModel
import dagger.*
import javax.inject.Scope
import javax.inject.Singleton

@ActivityMainScope
@Subcomponent(modules = [MainActivityModule::class, BindsMainActivityModule::class])
interface MainActivitySubcomponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance activity: ComponentActivity) : MainActivitySubcomponent
    }
    fun inject(mainActivity: MainActivity)
}

@Module
class MainActivityModule{
    @[Provides ActivityMainScope]
    fun provideRepositoryViewModel(activity: ComponentActivity, repository: Repository):
            RepositoryViewModel {
        return ViewModelProvider(
            activity.viewModelStore,
            FactoryViewModel(activity, repository, activity.intent.extras)
        )[(RepositoryViewModel::class.java)]
    }

    @[Provides ActivityMainScope]
    fun provideAccessUser(loginState: LoginViewState, databaseApi: DatabaseCRUDInterface): AccessUserInterface
            = AccessUser(loginState, databaseApi)
}

@Module
interface BindsMainActivityModule {
    @Binds
    fun bindUserPasswordStorage_to_UserPasswordStorageInterface(passwordStorage: UserPasswordStorage): UserPasswordStorageInterface
}

@Scope
annotation class ActivityMainScope