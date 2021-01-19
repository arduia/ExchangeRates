package com.arduia.exchangerates.domain

import com.arduia.exchangerates.domain.Amount.Companion.div
import com.arduia.exchangerates.domain.Amount.Companion.times
import javax.inject.Inject

/**
 * Created by Aung Ye Htet on 17/01/2021.
 */
class ExchangeRateConverterImpl @Inject constructor(): ExchangeRateConverter {

    private val oneUnitAmount = Amount.fromFloat(1f)
    private var usdRate = oneUnitAmount
    private var enteredCurrencyValue = oneUnitAmount

    //Set USD Rate for Selected Currency
    override fun setUSDRate(value: Amount) {
        usdRate = value
    }

    //Calculate for Every Rate
    override fun calculate(rate: Amount): Amount {
        return internalCalculate(rate)
    }

    override fun calculateOneUnit(rate: Amount): Amount {
        return internalCalculateOneUnit(rate)
    }

    private fun internalCalculateOneUnit(rate: Amount): Amount {
        val enteredCurrencyUSDRate = (oneUnitAmount.div(usdRate))
        return rate.times(enteredCurrencyUSDRate)
    }

    private fun internalCalculate(rate: Amount): Amount {
        val enteredCurrencyUSDRate = (enteredCurrencyValue.div(usdRate))
        return rate.times(enteredCurrencyUSDRate)
    }

    override fun setEnterdValue(value: Amount) {
        this.enteredCurrencyValue = value
    }

    override fun getUnitRate(): Amount {
        return usdRate
    }
}