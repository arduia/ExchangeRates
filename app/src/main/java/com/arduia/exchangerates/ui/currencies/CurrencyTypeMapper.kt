package com.arduia.exchangerates.ui.currencies

import com.arduia.exchangerates.data.CurrencyTypeDto
import com.arduia.exchangerates.domain.Mapper
import com.arduia.exchangerates.ui.common.CurrencyTypeItemUiModel
import javax.inject.Inject

/**
 * Created by Aung Ye Htet at 18/01/2021 6:01PM.
 */
class CurrencyTypeMapper @Inject constructor() : Mapper<CurrencyTypeDto, CurrencyTypeItemUiModel> {

    override fun map(input: CurrencyTypeDto): CurrencyTypeItemUiModel {
        return CurrencyTypeItemUiModel(
                id = input.id,
                currencyCode = input.currencyCode,
                currencyName = input.currencyName
        )
    }

}