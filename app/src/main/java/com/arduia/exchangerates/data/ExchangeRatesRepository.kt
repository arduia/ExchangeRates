package com.arduia.exchangerates.data

import androidx.paging.DataSource
import com.arduia.exchangerates.domain.Result

/**
 * Created by Aung Ye Htet at 18/01/2021 7:23 PM.
 */
interface ExchangeRatesRepository {

    fun getAllDataSource(selectedCurrencyCode: String): DataSource.Factory<Int, ExchangeRateDto>

    suspend fun getCurrencyRateByCurrencyCode(code: String): Result<ExchangeRateDto?>
}