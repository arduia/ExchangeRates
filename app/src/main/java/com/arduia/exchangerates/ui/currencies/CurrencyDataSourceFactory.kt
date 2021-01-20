package com.arduia.exchangerates.ui.currencies

import androidx.paging.DataSource
import com.arduia.exchangerates.data.CurrencyLayerRepository
import com.arduia.exchangerates.data.CurrencyTypeDto
import javax.inject.Inject

/**
 * Created by Aung Ye Htet at 20/01/2021.
 */
class CurrencyDataSourceFactory @Inject constructor(
        private val currencyLayerRepository: CurrencyLayerRepository
) : DataSource.Factory<Int, CurrencyTypeDto>() {

    private var query = ""

    override fun create(): DataSource<Int, CurrencyTypeDto> {
        return if (query.isBlank()) {
            currencyLayerRepository.getAllCurrencyTypeDataSource().create()
        } else
            currencyLayerRepository.getFilteredAllDataSource(query).create()
    }

    fun setQuery(query: String) {
        this.query = query
    }

}