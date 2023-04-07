package com.training.shoplocal.dagger

import com.training.shoplocal.classes.downloader.*
import dagger.Module

@Module
class ImageCacheModule {
    fun provideJournalCache(cacheDirectory: String): Journal     = Journal(cacheDirectory)
    fun provideDiskCache(cacheDirectory: String): ImageDiskCache = DiskCache(cacheDirectory)
    fun provideMemoryCache(cacheSize: Int): ImageMemoryCache     = MemoryCache(cacheSize)
    fun provideImageDownloader(diskCache: ImageDiskCache, memoryCache: ImageMemoryCache) = ImageLinkDownloader(diskCache, memoryCache)
}