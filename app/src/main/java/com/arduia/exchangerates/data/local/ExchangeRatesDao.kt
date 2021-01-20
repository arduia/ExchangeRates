package com.arduia.exchangerates.data.local

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.arduia.exchangerates.data.ExchangeRateDto


/**
 * Created by Aung Ye Htet at 17/01/2021 2:42 PM.
 */
@Dao
interface ExchangeRatesDao {

    @Query("SELECT cache_exchange_rates.id,cache_exchange_rates.currency_code, cache_exchange_rates.exchange_rate, currency_types.currency_name FROM `cache_exchange_rates` INNER JOIN `currency_types` ON cache_exchange_rates.currency_code=currency_types.currency_code WHERE cache_exchange_rates.currency_code!=:selectedCurrencyCode ORDER BY cache_exchange_rates.currency_code ASC")
    fun getAllDataSource(selectedCurrencyCode: String): DataSource.Factory<Int, ExchangeRateDto>

    @Query("SELECT cache_exchange_rates.id,cache_exchange_rates.currency_code, cache_exchange_rates.exchange_rate, currency_types.currency_name FROM `cache_exchange_rates` INNER JOIN `currency_types` ON cache_exchange_rates.currency_code=currency_types.currency_code WHERE cache_exchange_rates.currency_code==:code LIMIT 1")
    suspend fun getCurrencyRateByCurrencyCode(code: String): ExchangeRateDto?
}