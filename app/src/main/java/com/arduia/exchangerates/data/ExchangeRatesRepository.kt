package com.arduia.exchangerates.data

import androidx.paging.DataSource

/**
 * Created by Aung Ye Htet at 18/01/2021 7:23 PM.
 */
interface ExchangeRatesRepository {

    fun getAllDataSource(selectedCurrencyCode: String): DataSource.Factory<Int, ExchangeRateDto>

}