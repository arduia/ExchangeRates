package com.arduia.exchangerates.ui.home

/**
 * Created by Aung Ye Htet at 16/1/2021 6:18 PM.
 */
data class ExchangeRateItemUiModel(
        val id: Int,
        val currencyCode: String,
        val countryName: String,
        val exchangeRate: String,
        val exchangeBalance: String
)
