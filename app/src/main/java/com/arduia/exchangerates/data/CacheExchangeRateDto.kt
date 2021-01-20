package com.arduia.exchangerates.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * Created by Aung Ye Htet at 17/01/2021 1:30PM.
 */
@Entity(tableName = CacheExchangeRateDto.TABLE_NAME, indices = [Index("currency_code",unique=true)])
data class CacheExchangeRateDto(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "currency_code")
    val currencyCode: String,

    @ColumnInfo(name = "exchange_rate")
    val exchangeRate: String
) {

    companion object {
        const val TABLE_NAME = "cache_exchange_rates"
    }
}