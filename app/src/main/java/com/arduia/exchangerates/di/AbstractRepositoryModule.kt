package com.arduia.exchangerates.di

import com.arduia.exchangerates.data.*
import com.arduia.exchangerates.data.local.PreferencesDao
import com.arduia.exchangerates.data.local.SharedPreferencesDao
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


/**
 * Created by Aung Ye Htet on 18/01/2021
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class AbstractRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindPreferenceDao(impl: SharedPreferencesDao): PreferencesDao

    @Binds
    @Singleton
    abstract fun bindPreferenceRepository(impl: PreferenceRepositoryImpl): PreferencesRepository

    @Binds
    @Singleton
    abstract fun bindCurrencyLayerRepository(impl: CurrencyLayerRepositoryImpl): CurrencyLayerRepository

    @Binds
    @Singleton
    abstract fun bindExchangeRatesRepository(impl: ExchangeRatesRepositoryImpl): ExchangeRatesRepository

}