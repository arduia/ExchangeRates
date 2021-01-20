package com.arduia.exchangerates.domain

import java.lang.Exception
import java.lang.NumberFormatException
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Crated by Aung Ye Het at 17/01/2021 11:57 AM.
 */
//Wrapper class for exchange rate to support calculations
class Amount private constructor(private val value: BigDecimal) {

    override fun toString(): String {
        return value.toString()
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Amount) return false
        return this.value.toDouble() == other.value.toDouble() //TODO:Should Support over DoubleMAX
    }

    override fun hashCode(): Int {
        return this.value.hashCode()
    }

    fun format(formatter: AmountFormat): String {
        return formatter.format(value)
    }

    companion object {

        private const val MAX_DIV_ROUND_SCALE = 4 //Maximum rounded scale -> 0.1234
        fun fromString(value: String): Amount {
            checkIsNumberOrError(value)
            return Amount(BigDecimal(value))
        }

        fun fromFloat(float: Float): Amount {
            return Amount(float.toBigDecimal())
        }

        infix operator fun Amount.times(amount: Amount): Amount {
            val value = this.value.multiply(amount.value)
            return Amount(value)
        }

        infix operator fun Amount.div(amount: Amount): Amount {
            if (amount.value.equals("0")) throw Exception("Cannot be divided by zero")
            val value = this.value.divide(amount.value, MAX_DIV_ROUND_SCALE, RoundingMode.DOWN)
            return Amount(value)
        }
    }

}


//Number Format
interface AmountFormat {
    fun format(bigDecimal: BigDecimal): String
}

private fun checkIsNumberOrError(value: String) {
    value.toFloatOrNull()
        ?: throw AmountException(
            message = "Inserted value($value) is not a number.",
            NumberFormatException()
        )
}

class AmountException(override val message: String?, override val cause: Throwable?) : Exception()
