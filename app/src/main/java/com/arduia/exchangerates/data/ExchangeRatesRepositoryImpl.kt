package com.arduia.exchangerates.data

import androidx.paging.DataSource
import com.arduia.exchangerates.data.local.ExchangeRatesDao
import javax.inject.Inject

/**
 * Created by Aung Ye Htet at 18/01/2021 8:30 PM.
 */
class ExchangeRatesRepositoryImpl @Inject constructor(private val dao: ExchangeRatesDao) : ExchangeRatesRepository {
    override fun getAllDataSource(selectedCurrencyCode: String): DataSource.Factory<Int, ExchangeRateDto> {
        return dao.getAllDataSource(selectedCurrencyCode)
    }
}