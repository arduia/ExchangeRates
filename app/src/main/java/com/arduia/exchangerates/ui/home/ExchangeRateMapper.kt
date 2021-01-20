package com.arduia.exchangerates.ui.home

import com.arduia.exchangerates.data.CurrencyTypeDto
import com.arduia.exchangerates.data.ExchangeRateDto
import com.arduia.exchangerates.domain.Amount
import com.arduia.exchangerates.domain.AmountFormat
import com.arduia.exchangerates.domain.ExchangeRateConverter
import com.arduia.exchangerates.domain.Mapper
import java.math.BigDecimal
import java.text.DecimalFormat
import javax.inject.Inject
import javax.inject.Provider

/**
 * Created by Aung Ye Htet at 18/01/2021 5:54 PM.
 */
class ExchangeRateMapper private constructor(
        private val selectedCurrencyType: Provider<String>,
        private val rateConverter: ExchangeRateConverter,
        private val amountFormat: AmountFormat
) : Mapper<ExchangeRateDto, ExchangeRateItemUiModel> {

    override fun map(input: ExchangeRateDto): ExchangeRateItemUiModel {

        val rateAmount = rateConverter.calculate(input.exchangeRate).format(amountFormat)
        val unitRate = rateConverter.calculateOneUnit(input.exchangeRate).format(amountFormat)

        return ExchangeRateItemUiModel(
                id = input.id,
                currencyCode = input.currencyCode,
                currencyName = input.currencyName,
                exchangeBalance = "1${selectedCurrencyType.get()}=$unitRate${input.currencyCode}",
                exchangeRate = rateAmount
        )
    }

    class ExchangeRateMapperFactoryImpl @Inject constructor(
            private val amountFormat: AmountFormat
    ) : ExchangeRateMapperFactory {
        override fun create(
                rateConverter: ExchangeRateConverter,
                selectedCurrencyType: Provider<String>
        ): Mapper<ExchangeRateDto, ExchangeRateItemUiModel> {
            return ExchangeRateMapper(
                    selectedCurrencyType,
                    rateConverter,
                    amountFormat
            )
        }
    }
}

class ExchangeRateAmountFormat @Inject constructor() : AmountFormat {

    private val decimalFormat = DecimalFormat("#,###.##")

    override fun format(bigDecimal: BigDecimal): String {
        return decimalFormat.format(bigDecimal)
    }

}

interface ExchangeRateMapperFactory {

    fun create(
            rateConverter: ExchangeRateConverter,
            selectedCurrencyType: Provider<String>
    ): Mapper<ExchangeRateDto, ExchangeRateItemUiModel>

}