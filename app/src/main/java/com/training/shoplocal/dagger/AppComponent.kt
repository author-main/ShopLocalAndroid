package com.training.shoplocal.dagger

import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import com.google.gson.GsonBuilder
import com.training.shoplocal.AppShopLocal
import com.training.shoplocal.classes.SERVER_URL
import com.training.shoplocal.classes.downloader.*
import com.training.shoplocal.repository.*
import com.training.shoplocal.repository.retrofit.DatabaseApiInterface
import com.training.shoplocal.userfingerprint.UserFingerPrint
import com.training.shoplocal.userfingerprint.UserFingerPrintInterface
import com.training.shoplocal.userfingerprint.UserPasswordStorage
import com.training.shoplocal.userfingerprint.UserPasswordStorageInterface
import com.training.shoplocal.viewmodel.FactoryViewModel
import com.training.shoplocal.viewmodel.RepositoryViewModel
import dagger.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Scope
import javax.inject.Singleton


@Singleton
@Component(modules = [CacheModule::class, RepositoryModule::class, BindsAppModule::class, SubcomponentsModule::class])
interface AppComponent {
    fun injectApplication(application: AppShopLocal)
    @Component.Factory
    interface Factory{
        fun create(@BindsInstance dir: String,
                   @BindsInstance size: Int
        ): AppComponent
    }
    fun mainActivitySubcomponent(): MainActivitySubcomponent.Factory
}

@Module(subcomponents = [MainActivitySubcomponent::class])
interface SubcomponentsModule

@Module
class CacheModule

@Module
interface BindsAppModule {
    @Binds
    fun bindDiskCache_to_ImageDiskCache(diskCache: DiskCache): ImageDiskCache

    @Binds
    fun bindMemoryCache_to_ImageMemoryCache(memoryCache: MemoryCache): ImageMemoryCache

    @Binds
    fun bindDatabaseCRUD_to_DatabaseCRUDInterface(databaseCRUD: DatabaseCRUD): DatabaseCRUDInterface
}

@Module
class RepositoryModule {
    @Singleton
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