package com.arduia.exchangerates.data.local

import com.arduia.exchangerates.data.CurrencyTypeDto

object ExchangeRateDbUtil{

    fun createCurrencyTypes(): List<CurrencyTypeDto>{
        val list = mutableListOf<CurrencyTypeDto>()

        list.add(CurrencyTypeDto(0, "USD", "United State Dollar"))
        list.add(CurrencyTypeDto(0, "MMK", "Myanmar"))

        return list
    }
}