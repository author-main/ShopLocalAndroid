package com.training.shoplocal.dagger

import com.training.shoplocal.classes.downloader.DiskCache
import com.training.shoplocal.classes.downloader.ImageDiskCache
import com.training.shoplocal.classes.downloader.ImageMemoryCache
import com.training.shoplocal.classes.downloader.MemoryCache
import com.training.shoplocal.repository.AccessUser
import com.training.shoplocal.repository.AccessUserInterface
import com.training.shoplocal.repository.DatabaseCRUD
import com.training.shoplocal.repository.DatabaseCRUDInterface
import dagger.Binds
import dagger.Module

@Module
interface BindsModule {
    @Binds
    fun bindDiskCache_to_ImageDiskCache(diskCache: DiskCache): ImageDiskCache

    @Binds
    fun bindMemoryCache_to_ImageMemoryCache(memoryCache: MemoryCache): ImageMemoryCache

    @Binds
    fun bindAccessUserImpl_to_AccessUserInterface(accessUser: AccessUser): AccessUserInterface

    @Binds
    fun bindDatabaseCRUDImpl_to_DatabaseCRUD(databaseCRUD: DatabaseCRUD): DatabaseCRUDInterface
}