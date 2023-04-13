package com.training.shoplocal.dagger

import android.content.Context
import com.training.shoplocal.MainActivity
import com.training.shoplocal.loginview.LoginViewState
import com.training.shoplocal.repository.AccessUser
import com.training.shoplocal.repository.AccessUserInterface
import com.training.shoplocal.repository.DatabaseCRUD
import dagger.BindsInstance
import dagger.Module
import dagger.Provides
import dagger.Subcomponent


@Subcomponent(modules = [MainActivityModule::class])
interface MainActivitySubcomponent {//: AndroidInjector<MainActivity> {
    @Subcomponent.Factory
    interface Factory {//: AndroidInjector.Factory<MainActivity>
        fun create(@BindsInstance context: Context) : MainActivitySubcomponent
    }
    fun inject(mainActivity: MainActivity)
}

@Module
interface MainActivityModule{
    @Provides
    fun provideAccessUser(context: Context, loginState: LoginViewState): AccessUserInterface        = AccessUser(context, loginState)
}
