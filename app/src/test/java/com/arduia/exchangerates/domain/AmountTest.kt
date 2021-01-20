package com.arduia.exchangerates.domain

import com.arduia.exchangerates.domain.Amount.Companion.div
import com.arduia.exchangerates.domain.Amount.Companion.times
import org.hamcrest.core.Is.`is`
import org.junit.Assert.*
import org.junit.Test
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.NumberFormat

class AmountTest {

    @Test
    fun shouldInitWork() {
        val amount = Amount.fromFloat(5.55f)
        assertThat(amount.toString(), `is`("5.55"))

        val amountMinus = Amount.fromFloat(-6.5f)
        assertThat(amountMinus.toString(), `is`("-6.5"))

        val amountFromString = Amount.fromString("65.44")
        assertThat("65.44", `is`(amountFromString.toString()))

        val amountMinusFromString = Amount.fromString("-78.11")
        assertThat("-78.11", `is`(amountMinusFromString.toString()))

        val amountIntegerFromString = Amount.fromString("65")
        assertThat("65", `is`(amountIntegerFromString.toString()))
    }

    @Test(expected = AmountException::class)
    fun shouldInitFail() {
        val amount = Amount.fromString("abcdefg")
        //Should throw AmountException
    }

    @Test
    fun shouldFormatWork() {

        val prefixString = "test,"
        val valueString = "1.5"

        val mockFormatter = object : AmountFormat {
            override fun format(bigDecimal: BigDecimal): String {
                return "$prefixString${bigDecimal}"
            }
        }

        val amount = Amount.fromString(valueString)
        val formattedString = amount.format(mockFormatter)
        assertThat(formattedString, `is`("$prefixString$valueString"))
    }

    @Test
    fun shouldEqualWork() {
        val value = "1.0"
        val amountOne = Amount.fromString(value)
        val amountTwo = Amount.fromString(value)

        assertTrue(amountOne == amountTwo)
        assertFalse(amountOne === amountTwo)

        val random = Any()
        assertFalse(random == amountOne)
        assertFalse(random == amountTwo)

    }

    @Test
    fun shouldMultiplyWork() {

        val amountOne = Amount.fromFloat(2.5f)
        val amountTwo = Amount.fromFloat(2f)
        val amountResultExcepted = Amount.fromFloat(5f)

        val sampleFormat = object : AmountFormat {
            val numberFormat = DecimalFormat("#.##")
            override fun format(bigDecimal: BigDecimal): String {
                return numberFormat.format(bigDecimal)
            }
        }

        val amountResult = amountTwo * amountOne
        assertEquals(amountResultExcepted.format(sampleFormat), amountResult.format(sampleFormat))
    }

    @Test
    fun shouldDivisionWork(){

        val amountOne = Amount.fromFloat(300f)
        val dividerAmount  = Amount.fromFloat(15f)
        val amountExcepted =Amount.fromFloat(20f)
        val sampleFormat = object : AmountFormat {
            val numberFormat = DecimalFormat("#.##")
            override fun format(bigDecimal: BigDecimal): String {
                return numberFormat.format(bigDecimal)
            }
        }
        val result = amountOne/dividerAmount
        assertEquals(amountExcepted.format(sampleFormat), result.format(sampleFormat))
    }
}