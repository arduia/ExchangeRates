package com.arduia.exchangerates.data

import androidx.room.ColumnInfo
import com.arduia.exchangerates.domain.Amount
import com.google.gson.annotations.SerializedName

/**
 * Created by Aung Ye Htet at 17/01/2021 1:46PM.
 */
data class ExchangeRateDto(

    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "currency_code")
    val currencyCode: String,

    @ColumnInfo(name = "exchange_rate")
    val exchangeRate: Amount,

    @ColumnInfo(name = "currency_name")
    val currencyName: String
)
