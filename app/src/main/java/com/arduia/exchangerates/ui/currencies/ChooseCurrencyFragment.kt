package com.arduia.exchangerates.ui.currencies

import android.view.LayoutInflater
import android.view.ViewGroup
import com.arduia.exchangerates.databinding.FragChooseCurrencyBinding
import com.arduia.exchangerates.ui.common.BaseBindingFragment
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by Aung Ye Htet at 16/1/2021 6:01 PM.
 */
@AndroidEntryPoint
class ChooseCurrencyFragment : BaseBindingFragment<FragChooseCurrencyBinding>() {

    override fun createBinding(layoutInflater: LayoutInflater, parent: ViewGroup?): FragChooseCurrencyBinding {
        return FragChooseCurrencyBinding.inflate(layoutInflater, parent, false)
    }

}