package com.arduia.exchangerates.di

import com.arduia.exchangerates.data.CacheSyncManager
import com.arduia.exchangerates.data.CacheSyncManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AbstractCacheModule {

    @Singleton
    @Binds
    abstract fun bindCacheManager(impl: CacheSyncManagerImpl): CacheSyncManager
}