package com.arduia.exchangerates.di

import com.arduia.exchangerates.domain.ExchangeRateConverter
import com.arduia.exchangerates.domain.ExchangeRateConverterImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

/**
 * Created by Aung Ye Htet on 18/01/2021
 */
@Module
@InstallIn(ActivityComponent::class)
abstract class AbstractDomainModule {

    @Binds
    abstract fun bindRateConverter(impl: ExchangeRateConverterImpl): ExchangeRateConverter

}