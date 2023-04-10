package com.training.shoplocal.dagger

import com.training.shoplocal.classes.downloader.*
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module(includes = [BindsImageCache::class])
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

@Module
interface BindsImageCache {
    @Binds
    fun bindDiskCache_to_ImageDiskCache(diskCache: DiskCache): ImageDiskCache

    @Binds
    fun bindMemoryCache_to_ImageMemoryCache(memoryCache: MemoryCache): ImageMemoryCache
}

/*interface ProviderCacheParam {
    val cacheDir:   String
    val sizeDir:    Int
}*/