package com.arduia.exchangerates.domain

import java.lang.Exception
import java.lang.NumberFormatException
import java.math.BigDecimal

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
        return this.value.toString() == other.toString()
    }

    override fun hashCode(): Int {
        return this.value.hashCode()
    }

    fun format(formatter: AmountFormat): String{
        return formatter.format(value)
    }

    companion object {
        fun fromString(value: String): Amount {
            checkIsNumberOrError(value)
            return Amount(BigDecimal(value))
        }

        fun fromFloat(float: Float): Amount {
            return Amount(float.toBigDecimal())
        }

        infix operator fun Amount.times(amount: Amount): Amount{
            val value = this.value.multiply(amount.value)
            return Amount(value)
        }
    }

}


//Number Format
interface AmountFormat{
    fun format(bigDecimal: BigDecimal): String
}

private fun checkIsNumberOrError(value: String) {
    value.toFloatOrNull()
            ?: throw AmountException(message = "Inserted value($value) is not a number.", NumberFormatException())
}

class AmountException(override val message: String?, override val cause: Throwable?) : Exception()
