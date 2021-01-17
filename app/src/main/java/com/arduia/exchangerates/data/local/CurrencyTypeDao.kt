package com.arduia.exchangerates.data.local

import androidx.paging.DataSource
import androidx.room.*
import com.arduia.exchangerates.data.CurrencyTypeDto
import kotlinx.coroutines.flow.Flow

/**
 * Created by Aung Ye Htet at 17/01/2021 2:23 PM.
 */
@Dao
interface CurrencyTypeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<CurrencyTypeDto>)

    @Query("SELECT * FROM `currency_types`")
    fun getAllDataSource(): DataSource.Factory<Int, CurrencyTypeDto>

    @Query("SELECT * FROM `currency_types` WHERE currencyCode =:code LIMIT 1")
    fun getByCountryCodeFlow(code: String): Flow<CurrencyTypeDto>

    @Query("SELECT * FROM `currency_types` WHERE currencyCode =:code LIMIT 1")
    suspend fun getByCountryCode(code: String): CurrencyTypeDto?

    @Query("DELETE FROM currency_types")
    suspend fun deleteAll()

}