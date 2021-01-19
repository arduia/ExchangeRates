package com.arduia.exchangerates.ui.currencies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.arduia.exchangerates.R
import com.arduia.exchangerates.databinding.FragChooseCurrencyBinding
import com.arduia.exchangerates.ui.common.BaseBindingFragment
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by Aung Ye Htet at 16/1/2021 6:01 PM.
 */
@AndroidEntryPoint
class ChooseCurrencyFragment : BaseBindingFragment<FragChooseCurrencyBinding>() {

    private val viewModel by viewModels<ChooseCurrencyViewModel>()

    private var currenciesAdapter: CurrenciesAdapter? = null

    override fun createBinding(
            layoutInflater: LayoutInflater,
            parent: ViewGroup?
    ): FragChooseCurrencyBinding {
        return FragChooseCurrencyBinding.inflate(layoutInflater, parent, false)
    }

    override fun onViewCreated(savedInstanceState: Bundle?) {
        super.onViewCreated(savedInstanceState)
        setupView()
        setupViewModel()
    }

    private fun setupView() {

        currenciesAdapter = CurrenciesAdapter(layoutInflater)
        binding.rvCurrencies.adapter = currenciesAdapter

        currenciesAdapter?.setOnItemClickListener(viewModel::onSelect)

        binding.btnNavigateBack.setOnClickListener {
            it.isClickable = false //Avoid Multiple Clicking
            navigateBackToHome()
        }

        binding.edtSearch.addTextChangedListener {
            viewModel.onQuery(it.toString())
        }

    }

    private fun navigateBackToHome() {
        findNavController().popBackStack()
    }

    private fun setupViewModel() {

        viewModel.isEmptyCurrencies.observe(viewLifecycleOwner, {
            when (it) {
                true -> {
                    showEmptyCurrencies()
                    binding.edtSearch.isEnabled = false
                }
                else -> {
                    hideEmptyCurrencies()
                    binding.edtSearch.isEnabled = true
                }
            }
        })

        viewModel.currencyTypeList.observe(viewLifecycleOwner) {
            currenciesAdapter?.submitList(it)
        }

        viewModel.onItemSelectError.observe(viewLifecycleOwner) {
            Toast.makeText(
                    requireContext(),
                    getString(R.string.item_select_error),
                    Toast.LENGTH_SHORT
            ).show()
        }

        viewModel.onItemSelected.observe(viewLifecycleOwner) {
            navigateBackToHome()
        }
    }

    private fun showEmptyCurrencies() {
        with(binding) {
            tvEmptyCurrencies.visibility = View.VISIBLE
            imvDollar.visibility = View.VISIBLE
        }
    }

    private fun hideEmptyCurrencies() {
        with(binding) {
            tvEmptyCurrencies.visibility = View.INVISIBLE
            imvDollar.visibility = View.INVISIBLE
        }
    }

    override fun onBeforeBindingDestroy() {
        super.onBeforeBindingDestroy()
        currenciesAdapter = null
    }

}