package com.arduia.exchangerates.data

import androidx.paging.DataSource
import com.arduia.exchangerates.data.exception.ServerErrorException
import com.arduia.exchangerates.data.ext.asResultFlow
import com.arduia.exchangerates.data.ext.result
import com.arduia.exchangerates.data.ext.resultNullable
import com.arduia.exchangerates.data.local.CacheExchangeRateDao
import com.arduia.exchangerates.data.local.CurrencyTypeDao
import com.arduia.exchangerates.data.remote.CurrencyLayerDao
import com.arduia.exchangerates.data.remote.GetCurrencyLiveDto
import com.arduia.exchangerates.data.remote.GetCurrencyNameListDto
import com.arduia.exchangerates.domain.ErrorResult
import com.arduia.exchangerates.domain.FlowResult
import com.arduia.exchangerates.domain.Result
import com.arduia.exchangerates.domain.SuccessResult
import retrofit2.Call
import java.lang.Exception
import javax.inject.Inject

/**
 * Created by Aung Ye Htet at 18/01/2021 8:33 PM.
 */
class CurrencyLayerRepositoryImpl @Inject constructor(
        private val cacheExchangeRateDao: CacheExchangeRateDao,
        private val currencyTypeDao: CurrencyTypeDao,
        private val currencyLayerDao: CurrencyLayerDao
) : CurrencyLayerRepository {

    override suspend fun insertAllCacheExchangeRate(item: List<CacheExchangeRateDto>): Result<Unit> {
        return result { cacheExchangeRateDao.insertAll(item) }
    }

    override suspend fun deleteAllCacheExchangeRate(): Result<Unit> {
        return result { cacheExchangeRateDao.deleteAll() }
    }

    override suspend fun getAllCacheExchangeRate(): Result<List<CacheExchangeRateDto>> {
        return result { cacheExchangeRateDao.getAll() }
    }


    override suspend fun insertAllCurrencyType(items: List<CurrencyTypeDto>): Result<Unit> {
        return result { currencyTypeDao.insertAll(items) }
    }

    override fun getAllCurrencyTypeDataSource(): DataSource.Factory<Int, CurrencyTypeDto> {
        return currencyTypeDao.getAllDataSource()
    }

    override fun getFilteredAllDataSource(query: String): DataSource.Factory<Int, CurrencyTypeDto> {
        return currencyTypeDao.getFilteredAllDataSource(query)
    }

    override fun getCurrencyTypeByCurrencyCodeFlow(code: String): FlowResult<CurrencyTypeDto> {
        return currencyTypeDao.getByCurrencyCodeFlow(code).asResultFlow()
    }

    override suspend fun getCurrencyTypeByCurrencyCode(code: String): Result<CurrencyTypeDto?> {
        return resultNullable { currencyTypeDao.getByCurrencyCode(code) }
    }

    override suspend fun deleteAllCurrencyType(): Result<Unit> {
        return result { currencyTypeDao.deleteAll() }
    }

    override suspend fun getDownloadedUSDCurrencyRates(): Result<GetCurrencyLiveDto.Response> {
        return handleRetrofitCall { currencyLayerDao.getCurrencyLive() }
    }

    override suspend fun getDownloadCurrencyNames(): Result<GetCurrencyNameListDto.Response> {
        return handleRetrofitCall { currencyLayerDao.getCurrencyNameList() }
    }

    private inline fun <T> handleRetrofitCall(io: () -> Call<T>): Result<T> {
        return try {
            val call = io.invoke().execute()

            if (call.isSuccessful.not()) return ErrorResult(ServerErrorException(404, "not successful"))

            val response = call.body()
                    ?: return ErrorResult(ServerErrorException(404, "body not found!"))

            return SuccessResult(response)
        } catch (e: Exception) {
            ErrorResult(e)
        }
    }
}