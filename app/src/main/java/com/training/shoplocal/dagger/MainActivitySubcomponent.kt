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
interface MainActivitySubcomponent {//: AndroidInjector<MainActivity> {

    /*@Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun componentActivity(activity: ComponentActivity): Builder
        fun build(): MainActivitySubcomponent
    }*/

    @Subcomponent.Factory
    interface Factory {//: AndroidInjector.Factory<MainActivity>
        //fun create(@BindsInstance context: Context,
        fun create(@BindsInstance activity: ComponentActivity) : MainActivitySubcomponent
        //fun create(@BindsInstance activity: ComponentActivity) : MainActivitySubcomponent
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
        )[RepositoryViewModel::class.java]
    }


    /*@[Provides ActivityMainScope]
    fun provideAccessUser(context: Context, loginState: LoginViewState, databaseApi: DatabaseCRUDInterface): AccessUserInterface
        = AccessUser(context, loginState, databaseApi)*/


    @[Provides ActivityMainScope]
    fun provideAccessUser(loginState: LoginViewState, databaseApi: DatabaseCRUDInterface): AccessUserInterface
            = AccessUser(loginState, databaseApi)


   /* @[Provides ActivityMainScope]
    fun providePasswordStorage(): UserPasswordStorageInterface = UserPasswordStorage()*/

    /*@[Provides ActivityMainScope]
    fun provideFingerPrint(context: Context): UserFingerPrint = UserFingerPrint(context)*/
}

@Module
interface BindsMainActivityModule {
    @Binds
    fun bindUserPasswordStorage_to_UserPasswordStorageInterface(passwordStorage: UserPasswordStorage): UserPasswordStorageInterface

    /*@Binds
    fun bindComponentActivity_to_Context(activity: ComponentActivity): Context*/
}

@Scope
annotation class ActivityMainScope