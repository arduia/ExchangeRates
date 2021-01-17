package com.arduia.exchangerates.data.local

import androidx.room.*
import com.arduia.exchangerates.data.CacheExchangeRateDto


/**
 * Created by Aung Ye Htet at 17/01/2021 1:47PM.
 */
@Dao
interface CacheExchangeRateDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(item: List<CacheExchangeRateDto>)

    @Query("DELETE FROM `cache_exchange_rates`")
    suspend fun deleteAll()

    @Query("SELECT * FROM `cache_exchange_rates`")
    suspend fun getAll(): List<CacheExchangeRateDto> // Testing Purpose
}