package com.arduia.exchangerates.di

import com.arduia.exchangerates.domain.AmountFormat
import com.arduia.exchangerates.ui.home.ExchangeRateAmountFormat
import com.arduia.exchangerates.ui.home.format.DateFormatter
import com.arduia.exchangerates.ui.home.format.SyncDateFormatter
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

/**
 * Created by Aung Ye Htet on 18/01/2021
 */
@Module
@InstallIn(ApplicationComponent::class)
abstract class AbstractFormatterModule {

    @Binds
    abstract fun bindAmountFormat(impl: ExchangeRateAmountFormat): AmountFormat

    @Binds
    abstract fun bindDateFormat(impl: SyncDateFormatter): DateFormatter

}