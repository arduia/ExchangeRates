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
    ) = FragChooseCurrencyBinding.inflate(layoutInflater, parent, false)

    override fun onViewCreated(savedInstanceState: Bundle?) {
        super.onViewCreated(savedInstanceState)
        setupView()
        setupViewModel()
    }

    private fun setupView() {
        setupCurrencyListRecyclerView()
        setupNavigateBackButton()
        setupSearchEditText()
    }

    private fun setupViewModel() {
        observeIsEmptyCurrencies()
        observeCurrencyTypeList()
        observeOnCurrencySelectErrorEvent()
        observeOnCurrencySelectedEvent()
    }

    private fun observeIsEmptyCurrencies() {
        viewModel.isEmptyCurrencies.observe(viewLifecycleOwner, { isEmpty ->
            when (isEmpty) {
                true -> {
                    showEmptyCurrencies()
                    disableSearchView()
                }
                else -> {
                    hideEmptyCurrencies()
                    enableSearchView()
                }
            }
        })
    }

    private fun observeCurrencyTypeList() {
        viewModel.currencyTypeList.observe(viewLifecycleOwner) {
            currenciesAdapter?.submitList(it)
        }
    }

    private fun observeOnCurrencySelectErrorEvent() {
        viewModel.onCurrencySelectError.observe(viewLifecycleOwner) {
            Toast.makeText(
                    requireContext(),
                    getString(R.string.item_select_error),
                    Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun observeOnCurrencySelectedEvent() {
        viewModel.onCurrencySelected.observe(viewLifecycleOwner) {
            navigateBackToHome()
        }
    }

    private fun setupNavigateBackButton() {
        binding.btnNavigateBack.setOnClickListener {
            it.isClickable = false //Avoid Multiple Clicking
            navigateBackToHome()
        }
    }

    private fun setupSearchEditText() {
        binding.edtSearch.addTextChangedListener {
            viewModel.onQuery(it.toString())
        }
    }

    private fun setupCurrencyListRecyclerView() {
        currenciesAdapter = CurrenciesAdapter(layoutInflater)
        binding.rvCurrencies.adapter = currenciesAdapter
        currenciesAdapter?.setOnItemClickListener(viewModel::onCurrencySelected)
    }

    private fun navigateBackToHome() {
        findNavController().popBackStack()
    }

    private fun disableSearchView() {
        binding.edtSearch.isEnabled = false
    }

    private fun enableSearchView() {
        binding.edtSearch.isEnabled = true
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

    override fun onBeforeBindingDestroyed() {
        binding.rvCurrencies.adapter = null
        currenciesAdapter?.setOnItemClickListener(null)
        currenciesAdapter = null
    }

}