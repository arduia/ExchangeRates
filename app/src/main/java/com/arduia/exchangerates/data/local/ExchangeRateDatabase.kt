package com.arduia.exchangerates.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.arduia.exchangerates.data.CacheExchangeRateDto
import com.arduia.exchangerates.data.CurrencyTypeDto

@Database(
        entities = [CacheExchangeRateDto::class, CurrencyTypeDto::class],
        version = 6, exportSchema = true
)
@TypeConverters(AmountTypeConverter::class)
abstract class ExchangeRateDatabase : RoomDatabase() {

    abstract val cacheExchangeRateDao: CacheExchangeRateDao

    abstract val currencyTypeDao: CurrencyTypeDao

    abstract val exchangeRateDao: ExchangeRatesDao

}