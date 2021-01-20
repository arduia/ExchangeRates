package com.arduia.exchangerates.domain

import org.junit.Assert.*
import org.junit.Test

class ExchangeRateConverterImplTest {

    @Test
    fun shouldCalculateWork() {
        val converter: ExchangeRateConverter = ExchangeRateConverterImpl()
        val exchangeRateA = Amount.fromString("56.03")
        val exchangeRateB = Amount.fromString("30.000003")

        //Test A: 1USD = 1350.44MMK
        //EnterAmount = 8000.88
        val oneUSDtoMMKRate = Amount.fromString("1350.44")
        val desiredCurrencyValue = Amount.fromString("8000.88")
        converter.setUSDRate(oneUSDtoMMKRate)
        converter.setEnterdValue(desiredCurrencyValue)

        //Test with RateA("56.03") ( 1USD = 56.03)
        val resultForRateA = converter.calculate(exchangeRateA)
        assertEquals(Amount.fromString("331.955338"), resultForRateA)

        //Test with RateA("30.000003") ( 1USD = 30.000003)
        val resultForRateB = converter.calculate(exchangeRateB)
        assertEquals(Amount.fromString("177.7380177738"), resultForRateB)

        //Test A: 1USD = 0.826057Euro
        //EnterAmount = 3300000.88
        val onUSDToEuro = Amount.fromString("0.826057")
        val desireCurrencyValueTwo = Amount.fromString("3300000.88")
        converter.setUSDRate(onUSDToEuro)
        converter.setEnterdValue(desireCurrencyValueTwo)

        //Test with RateA("56.03") ( 1USD = 56.03)
        val testTwoResultForA = converter.calculate(exchangeRateA)
        assertEquals(Amount.fromString("223833281.849632"), testTwoResultForA)

        //Test with RateA("30.000003") ( 1USD = 30.000003)
        val testTowResultForB = converter.calculate(exchangeRateB)
        assertEquals(Amount.fromString("119846495.2166483232"), testTowResultForB)
    }
}