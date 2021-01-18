package com.arduia.exchangerates.ui.home

import com.arduia.exchangerates.data.ExchangeRateDto
import com.arduia.exchangerates.domain.Amount
import com.arduia.exchangerates.domain.AmountFormat
import com.arduia.exchangerates.domain.ExchangeRateConverter
import com.arduia.exchangerates.domain.Mapper
import javax.inject.Inject
import javax.inject.Provider

/**
 * Created by Aung Ye Htet at 18/01/2021 5:54 PM.
 */
class ExchangeRateMapper @Inject constructor(
        private val selectedCurrencyType: Provider<String>,
        private val rateConverter: ExchangeRateConverter,
        private val amountFormat: AmountFormat
) : Mapper<ExchangeRateDto, ExchangeRateItemUiModel> {

    override fun map(input: ExchangeRateDto): ExchangeRateItemUiModel {

        val rateAmount = rateConverter.calculate(input.exchangeRate)
        val unitRate = rateConverter.calculate(Amount.fromFloat(1f))
        return ExchangeRateItemUiModel(
                id = input.id,
                currencyCode = input.currencyCode,
                currencyName = input.currencyName,
                exchangeBalance = "1${selectedCurrencyType.get()}=$unitRate${input.currencyCode}",
                exchangeRate = rateAmount.format(amountFormat)
        )
    }

}