package com.arduia.exchangerates.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * Created by Aung Ye Htet at 17/01/2021 1:30PM.
 */
@Entity(tableName = CacheExchangeRateDto.TABLE_NAME)
data class CacheExchangeRateDto(

    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    val id: Int,

    @SerializedName("currency_code")
    val currencyCode: String,

    @SerializedName("exchange_rate")
    val exchangeRate: String
) {

    companion object {
        const val TABLE_NAME = "cache_exchange_rates"
    }
}