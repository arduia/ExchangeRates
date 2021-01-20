package com.arduia.exchangerates.di

import com.arduia.exchangerates.data.CurrencyTypeDto
import com.arduia.exchangerates.domain.Mapper
import com.arduia.exchangerates.ui.common.CurrencyTypeItemUiModel
import com.arduia.exchangerates.ui.currencies.CurrencyTypeMapper
import com.arduia.exchangerates.ui.home.ExchangeRateMapper
import com.arduia.exchangerates.ui.home.ExchangeRateMapperFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

/**
 * Created by Aung Ye Htet on 18/01/2021
 */
@Module
@InstallIn(ActivityComponent::class)
abstract class AbstractMapperModule {

    @Binds
    abstract fun bindExchangeRateMapperFactory(impl: ExchangeRateMapper.ExchangeRateMapperFactoryImpl): ExchangeRateMapperFactory

    @Binds
    abstract fun bindCurrencyTypeMapper(impl: CurrencyTypeMapper): Mapper<CurrencyTypeDto, CurrencyTypeItemUiModel>
}