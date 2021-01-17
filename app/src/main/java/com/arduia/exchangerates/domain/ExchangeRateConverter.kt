package com.arduia.exchangerates.domain

interface ExchangeRateConverter {

    fun setUSDRate(value: Amount)

    fun calculate(rate: Amount): Amount

    fun setEnterdValue(value: Amount)
}