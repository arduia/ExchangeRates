package com.arduia.exchangerates.domain

import com.arduia.exchangerates.domain.Amount.Companion.div
import com.arduia.exchangerates.domain.Amount.Companion.times

class ExchangeRateConverterImpl : ExchangeRateConverter {
    private var usdRate = Amount.fromFloat(1f)
    private var enteredCurrencyValue = Amount.fromFloat(1f)

    //Set USD Rate for Selected Currency
    override fun setUSDRate(value: Amount) {
        usdRate = value
    }

    //Calculate for Every Rate
    override fun calculate(rate: Amount): Amount {
        return internalCalculate(rate)
    }

    private fun internalCalculate(rate: Amount): Amount {
        val enteredCurrencyUSDRate = (enteredCurrencyValue.div(usdRate))
        return rate.times(enteredCurrencyUSDRate)
    }

    override fun setEnterdValue(value: Amount) {
        this.enteredCurrencyValue = value
    }
}