package com.training.shoplocal.dagger

import com.google.gson.GsonBuilder
import com.training.shoplocal.classes.SERVER_URL
import com.training.shoplocal.classes.downloader.DiskCache
import com.training.shoplocal.classes.downloader.ImageDiskCache
import com.training.shoplocal.classes.downloader.ImageMemoryCache
import com.training.shoplocal.classes.downloader.MemoryCache
import com.training.shoplocal.repository.AccessUser
import com.training.shoplocal.repository.AccessUserInterface
import com.training.shoplocal.repository.DatabaseCRUD
import com.training.shoplocal.repository.DatabaseCRUDInterface
import com.training.shoplocal.repository.retrofit.DatabaseApiInterface
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

@Module
class RepositoryModule {

    @Provides
    fun provideDatabaseApi(): DatabaseApiInterface {
        val gson = GsonBuilder()
            .setLenient()
            .create()
        val retrofit = Retrofit.Builder()
            .baseUrl(SERVER_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        return retrofit.create()
    }
}
