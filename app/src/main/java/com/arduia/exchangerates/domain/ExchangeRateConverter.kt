package com.arduia.exchangerates.domain

/**
 * Created by Aung Ye Htet on 17/01/2021
 */
interface ExchangeRateConverter {

    fun setUSDRate(value: Amount)

    fun calculate(rate: Amount): Amount

    fun calculateOneUnit(rate: Amount): Amount

    fun setEnterdValue(value: Amount)

    fun getUnitRate(): Amount
}