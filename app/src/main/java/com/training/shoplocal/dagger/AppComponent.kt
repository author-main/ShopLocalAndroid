package com.training.shoplocal.dagger

import androidx.lifecycle.ViewModel
import com.training.shoplocal.classes.downloader.ImageLinkDownloader
import com.training.shoplocal.viewmodel.RepositoryViewModel
import dagger.BindsInstance
import dagger.Component
import javax.inject.Qualifier
import javax.inject.Singleton

@Singleton
@Component(modules = [CacheModule::class])//, dependencies = [ProviderCacheParam::class])
interface AppComponent {
    fun injectRepositoryViewModel(viewModel: RepositoryViewModel)
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