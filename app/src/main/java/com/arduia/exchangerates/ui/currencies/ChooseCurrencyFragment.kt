package com.arduia.exchangerates.ui.currencies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.arduia.exchangerates.databinding.DialogDownloadingCurrenciesBinding
import com.arduia.exchangerates.databinding.FragChooseCurrencyBinding
import com.arduia.exchangerates.ui.common.BaseBindingFragment
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by Aung Ye Htet at 16/1/2021 6:01 PM.
 */
@AndroidEntryPoint
class ChooseCurrencyFragment : BaseBindingFragment<FragChooseCurrencyBinding>() {

    private val viewModel by viewModels<ChooseCurrencyViewModel>()

    private var currenciesDownloadingDialog: DownloadingCurrenciesDialog? = null

    override fun createBinding(layoutInflater: LayoutInflater, parent: ViewGroup?): FragChooseCurrencyBinding {
        return FragChooseCurrencyBinding.inflate(layoutInflater, parent, false)
    }

    override fun onViewCreated(savedInstanceState: Bundle?) {
        super.onViewCreated(savedInstanceState)
        setupView()
        setupViewModel()
    }

    private fun setupView() {
        binding.btnNavigateBack.setOnClickListener {
            it.isClickable = false //Avoid Multiple Clicking
            navigateBackToHome()
        }
    }

    private fun navigateBackToHome() {
        findNavController().popBackStack()
    }

    private fun setupViewModel() {
        viewModel.isCurrenciesDownloading.observe(viewLifecycleOwner, {
            when (it) {
                true -> showDownloadingCurrenciesDialog()
                else -> hideDownloadingCurrenciesDialog()
            }
        })

        viewModel.isEmptyCurrencies.observe(viewLifecycleOwner, {
            when(it){
                true -> showEmptyCurrencies()
                else -> hideEmptyCurrencies()
            }
        })
    }

    private fun showEmptyCurrencies(){
        with(binding){
            tvEmptyCurrencies.visibility = View.VISIBLE
            imvDollar.visibility = View.VISIBLE
        }
    }

    private fun hideEmptyCurrencies(){
        with(binding){
            tvEmptyCurrencies.visibility = View.INVISIBLE
            imvDollar.visibility = View.INVISIBLE
        }
    }

    private fun showDownloadingCurrenciesDialog() {
        hideDownloadingCurrenciesDialog()
        currenciesDownloadingDialog = DownloadingCurrenciesDialog(requireContext())
        currenciesDownloadingDialog?.show()
    }

    private fun hideDownloadingCurrenciesDialog() {
        currenciesDownloadingDialog?.dismiss()
    }

    override fun onBeforeBindingDestroy() {
        super.onBeforeBindingDestroy()
        hideDownloadingCurrenciesDialog()
    }

}