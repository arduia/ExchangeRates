package com.arduia.exchangerates.domain

import org.junit.Assert.*
import org.junit.Test

class ExchangeRateConverterImplTest{

    @Test
    fun shouldCalculateWork(){
        val converter: ExchangeRateConverter = ExchangeRateConverterImpl()

        val rate = Amount.fromString("1350.44")
        val enteredValue = Amount.fromString("8000.88")
        converter.setUSDRate(rate)
        converter.setEnterdValue(enteredValue)

        val rateOne = Amount.fromString("56.03")
        val expectedValue = Amount.fromString("331.955338")
        val convertedValue = converter.calculate(rateOne)



        assertEquals(expectedValue, convertedValue)
    }
}