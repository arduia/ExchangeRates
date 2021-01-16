package com.arduia.exchangerates.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import com.arduia.exchangerates.databinding.FragHomeBinding
import com.arduia.exchangerates.ui.common.BaseBindingFragment
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by Aung Ye Htet at 16/1/2021 6:00 PM.
 */
@AndroidEntryPoint
class HomeFragment : BaseBindingFragment<FragHomeBinding>() {

    override fun createBinding(layoutInflater: LayoutInflater, parent: ViewGroup?): FragHomeBinding {
        return FragHomeBinding.inflate(layoutInflater, parent, false)
    }

}