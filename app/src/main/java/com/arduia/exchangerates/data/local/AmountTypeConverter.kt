package com.arduia.exchangerates.data.local

import androidx.room.TypeConverter
import com.arduia.exchangerates.domain.Amount

class AmountTypeConverter {
    @TypeConverter
    fun fromRawAmount(amount: String): Amount{
        return Amount.fromString(amount)
    }

    @TypeConverter
    fun toRawAmount(amount: Amount): String{
        return amount.toString()
    }
}