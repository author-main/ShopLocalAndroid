package com.training.shoplocal.dagger

import com.training.shoplocal.classes.downloader.*
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module//(includes = [BindsImageCache::class])
class CacheModule {
  /** Пересобрано с @Inject на конструкторе
    @Provides
    fun provideJournalCache(cacheDirectory: String): Journal        = Journal(cacheDirectory)

    @Provides
    fun provideDiskCache(cacheDirectory: String): ImageDiskCache    = DiskCache(cacheDirectory)

    @Provides
    fun provideMemoryCache(cacheSize: Int): ImageMemoryCache        = MemoryCache(cacheSize)*/
    /** Реализован @Inject на конструкторе класс ImageLinkDownloader
    @Provides
    fun provideImageDownloader  (diskCache: ImageDiskCache,
                                 memoryCache: ImageMemoryCache)          = ImageLinkDownloader(diskCache, memoryCache)*/
}

/*interface ProviderCacheParam {
    val cacheDir:   String
    val sizeDir:    Int
}*/