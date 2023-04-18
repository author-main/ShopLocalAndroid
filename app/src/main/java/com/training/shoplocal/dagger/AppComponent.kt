package com.training.shoplocal.dagger

import com.google.gson.GsonBuilder
import com.training.shoplocal.AppShopLocal
import com.training.shoplocal.classes.SERVER_URL
import com.training.shoplocal.classes.downloader.*
import com.training.shoplocal.repository.AccessUser
import com.training.shoplocal.repository.AccessUserInterface
import com.training.shoplocal.repository.DatabaseCRUD
import com.training.shoplocal.repository.DatabaseCRUDInterface
import com.training.shoplocal.repository.retrofit.DatabaseApiInterface
import com.training.shoplocal.userfingerprint.UserFingerPrint
import com.training.shoplocal.userfingerprint.UserFingerPrintInterface
import com.training.shoplocal.userfingerprint.UserPasswordStorage
import com.training.shoplocal.userfingerprint.UserPasswordStorageInterface
import dagger.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Scope
import javax.inject.Singleton


@Singleton
@Component(modules = [CacheModule::class, RepositoryModule::class, BindsAppModule::class, SubcomponentsModule::class])//, dependencies = [ProviderCacheParam::class])
interface AppComponent {
    fun injectApplication(application: AppShopLocal)
    //fun injectMainActivity(activity: MainActivity)
    @Component.Factory
    interface Factory{
        fun create(@BindsInstance dir: String,
                   @BindsInstance size: Int
        ): AppComponent
    }

    fun mainActivitySubcomponent(): MainActivitySubcomponent.Factory
  /*  @Component.Builder
    interface Builder{

        @BindsInstance
        //fun cacheDir(@CacheDir dir: String): Builder
        fun cacheDir(dir: String): Builder
        @BindsInstance
        //fun cacheSize(@CacheSize size: Int): Builder
        fun cacheSize(size: Int): Builder
        //fun providerCacheParam(cacheParam: ProviderCacheParam): Builder
        fun build(): AppComponent
    }*/
}
/*@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class CacheDir
@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class CacheSize*/

@Module(subcomponents = [MainActivitySubcomponent::class])
interface SubcomponentsModule {
   /* @Binds
    @IntoMap
    @ClassKey(MainActivity::class)
    fun bindAndroidInjectorFactory(factory: MainActivitySubcomponent.Factory): AndroidInjector.Factory<*>*/
}

@Module
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

@Module
interface BindsAppModule {

    @Binds
    fun bindDiskCache_to_ImageDiskCache(diskCache: DiskCache): ImageDiskCache

    @Binds
    fun bindMemoryCache_to_ImageMemoryCache(memoryCache: MemoryCache): ImageMemoryCache

    /*@Binds
    fun bindAccessUser_to_AccessUserInterface(accessUser: AccessUser): AccessUserInterface*/


    /*@Binds
    fun bindUserPasswordStorage_to_UserPasswordStorageInterface(passwordStorage: UserPasswordStorage): UserPasswordStorageInterface*/

    @Binds
    fun bindDatabaseCRUD_to_DatabaseCRUDInterface(databaseCRUD: DatabaseCRUD): DatabaseCRUDInterface

   /* @Binds
    fun bindUserFingerPrint_to_UserFingerPrintInterface(userFingerPrint: UserFingerPrint): UserFingerPrintInterface*/
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
