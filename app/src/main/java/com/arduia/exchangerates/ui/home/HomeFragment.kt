package com.arduia.exchangerates.ui.home

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.arduia.exchangerates.databinding.FragHomeBinding
import com.arduia.exchangerates.ui.common.BaseBindingFragment
import com.arduia.exchangerates.ui.common.NoInternetConnectionDialog
import com.arduia.exchangerates.ui.currencies.DownloadingCurrenciesDialog
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by Aung Ye Htet at 16/1/2021 6:00 PM.
 */
@AndroidEntryPoint
class HomeFragment : BaseBindingFragment<FragHomeBinding>() {

    private var rateDownloadingDialog: Dialog? = null

    override fun createBinding(layoutInflater: LayoutInflater, parent: ViewGroup?): FragHomeBinding {
        return FragHomeBinding.inflate(layoutInflater, parent, false)
    }

    override fun onViewCreated(savedInstanceState: Bundle?) {
        super.onViewCreated(savedInstanceState)
        showDownloadingRatesDialog()
    }

    private fun showDownloadingRatesDialog() {
        hideDownloadingRatesDialog()
        rateDownloadingDialog = DownloadingRatesDialog(requireContext())
        rateDownloadingDialog?.show()
    }

    private fun hideDownloadingRatesDialog() {
        rateDownloadingDialog?.dismiss()
    }

    override fun onBeforeBindingDestroy() {
        super.onBeforeBindingDestroy()
        hideDownloadingRatesDialog()
    }


}