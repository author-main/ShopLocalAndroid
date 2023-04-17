package com.training.shoplocal.dagger

import android.content.Context
import androidx.fragment.app.FragmentActivity
import com.training.shoplocal.MainActivity
import com.training.shoplocal.loginview.LoginViewState
import com.training.shoplocal.repository.AccessUser
import com.training.shoplocal.repository.AccessUserInterface
import com.training.shoplocal.repository.DatabaseCRUD
import com.training.shoplocal.userfingerprint.UserFingerPrint
import com.training.shoplocal.userfingerprint.UserFingerPrintInterface
import dagger.BindsInstance
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@ActivityMainScope
@Subcomponent(modules = [MainActivityModule::class])
interface MainActivitySubcomponent {//: AndroidInjector<MainActivity> {
    @Subcomponent.Factory
    interface Factory {//: AndroidInjector.Factory<MainActivity>
        fun create(@BindsInstance context: Context) : MainActivitySubcomponent
    }
    fun inject(mainActivity: MainActivity)
}

@Module
class MainActivityModule{
    @[Provides ActivityMainScope]
    fun provideAccessUser(context: Context, loginState: LoginViewState): AccessUserInterface = AccessUser(context, loginState)

    /*@[Provides ActivityMainScope]
    fun provideFingerPrint(context: Context): UserFingerPrint = UserFingerPrint(context as FragmentActivity)*/
}
