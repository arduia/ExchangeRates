package com.arduia.exchangerates.data

import androidx.paging.DataSource
import com.arduia.exchangerates.data.remote.GetCurrencyLiveDto
import com.arduia.exchangerates.data.remote.GetCurrencyNameListDto
import com.arduia.exchangerates.domain.FlowResult
import com.arduia.exchangerates.domain.Result
import kotlinx.coroutines.flow.Flow

/**
 * Created by Aung Ye Htet at 18/01/2021 7:45 PM.
 */
interface CurrencyLayerRepository {

    //CacheExchangeRate
    suspend fun insertAllCacheExchangeRate(item: List<CacheExchangeRateDto>): Result<Unit>

    suspend fun deleteAllCacheExchangeRate(): Result<Unit>

    suspend fun getAllCacheExchangeRate(): Result<List<CacheExchangeRateDto>>

    //CurrencyType
    suspend fun insertAllCurrencyType(items: List<CurrencyTypeDto>): Result<Unit>

    fun getAllCurrencyTypeDataSource(): DataSource.Factory<Int, CurrencyTypeDto>

    fun getCurrencyTypeByCurrencyCodeFlow(code: String): FlowResult<CurrencyTypeDto>

    suspend fun getCurrencyTypeByCurrencyCode(code: String):  Result<CurrencyTypeDto?>

    suspend fun deleteAllCurrencyType(): Result<Unit>

    //remoteCurrencyLayer
    suspend fun getDownloadedUSDCurrencyRates(): Result<GetCurrencyLiveDto.Response>

    suspend fun getDownloadCurrencyNames(): Result<GetCurrencyNameListDto.Response>

}