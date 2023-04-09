package com.training.shoplocal.dagger

import dagger.BindsInstance
import dagger.Component
import javax.inject.Qualifier
import javax.inject.Singleton

@Singleton
@Component(modules = [CacheModule::class])//, dependencies = [ProviderCacheParam::class])
interface AppComponent {
    //fun injectImageDownloader(imageDownloader: ImageLinkDownloader)
    //fun injectViewModel(viewModel: ViewModel)
    @Component.Builder
    interface Builder{
        @BindsInstance
        fun cacheDir(@CacheDir dir: String): Builder
        @BindsInstance
        fun cacheSize(@CacheSize size: Int): Builder
        //fun providerCacheParam(cacheParam: ProviderCacheParam): Builder
        fun build(): AppComponent
    }
}
@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class CacheDir

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class CacheSize
