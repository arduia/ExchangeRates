package com.arduia.exchangerates.data

import com.arduia.exchangerates.domain.Amount
import com.google.gson.annotations.SerializedName

/**
 * Created by Aung Ye Htet at 17/01/2021 1:46PM.
 */
data class ExchangeRateDto(

    @SerializedName("id")
    val id: Int,

    @SerializedName("currency_code")
    val currencyCode: String,

    @SerializedName("exchange_rate")
    val exchangeRate: Amount,

    @SerializedName("currency_name")
    val currencyName: String
)
