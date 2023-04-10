package com.training.shoplocal.dagger

import androidx.lifecycle.ViewModel
import com.training.shoplocal.MainActivity
import com.training.shoplocal.classes.downloader.*
import com.training.shoplocal.viewmodel.RepositoryViewModel
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import java.lang.annotation.RetentionPolicy
import javax.inject.Qualifier
import javax.inject.Scope
import javax.inject.Singleton

@ActivityMainScope
@Component(modules = [CacheModule::class, BindsModule::class])//, dependencies = [ProviderCacheParam::class])
interface AppComponent {
    //fun injectRepositoryViewModel(viewModel: RepositoryViewModel)
    fun injectMainActivity(mainActivity: MainActivity)
    @Component.Builder
    interface Builder{
        @BindsInstance
        //fun cacheDir(@CacheDir dir: String): Builder
        fun cacheDir(dir: String): Builder
        @BindsInstance
        //fun cacheSize(@CacheSize size: Int): Builder
        fun cacheSize(size: Int): Builder
        //fun providerCacheParam(cacheParam: ProviderCacheParam): Builder
        fun build(): AppComponent
    }
}


/*@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class CacheDir

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class CacheSize*/

@Scope
annotation class ActivityMainScope