package com.training.shoplocal.dagger

import com.training.shoplocal.classes.downloader.ImageLinkDownloader
import dagger.BindsInstance
import dagger.Component
import javax.inject.Qualifier

@Component(modules = [ImageCacheModule::class])//, dependencies = [ProviderCacheParam::class])
interface AppComponent {
    fun injectImageDownloader(imageDownloader: ImageLinkDownloader)
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
