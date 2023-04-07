package com.training.shoplocal.dagger

import dagger.BindsInstance
import dagger.Component

@Component(modules = [ImageCacheModule::class])
interface AppComponent {
    @Component.Builder
    interface Builder{
        @BindsInstance
        fun cacheDir(dir: String): Builder
        @BindsInstance
        fun cacheSize(size: Int): Builder
        fun build(): AppComponent
    }
}
