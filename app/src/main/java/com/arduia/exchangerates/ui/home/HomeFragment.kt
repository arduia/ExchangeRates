package com.arduia.exchangerates.ui.home

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arduia.exchangerates.R
import com.arduia.exchangerates.databinding.FragHomeBinding
import com.arduia.exchangerates.ui.common.BaseBindingFragment
import com.arduia.exchangerates.ui.common.EventObserver
import com.arduia.exchangerates.ui.common.NoInternetConnectionDialog
import dagger.hilt.android.AndroidEntryPoint
import java.lang.StringBuilder

/**
 * Created by Aung Ye Htet at 16/1/2021 6:00 PM.
 */
@AndroidEntryPoint
class HomeFragment : BaseBindingFragment<FragHomeBinding>() {

    private val viewModel by viewModels<HomeViewModel>()

    private var rateDownloadingDialog: Dialog? = null
    private var syncRotateAnimation: Animation? = null
    private var noConnectionDialog: NoInternetConnectionDialog? = null

    private var exchangeRateAdapter: ExchangeRatesAdapter? = null

    override fun createBinding(
        layoutInflater: LayoutInflater,
        parent: ViewGroup?
    ): FragHomeBinding {
        return FragHomeBinding.inflate(layoutInflater, parent, false)
    }

    override fun onViewCreated(savedInstanceState: Bundle?) {
        super.onViewCreated(savedInstanceState)
        setupView()
        setupViewModel()
    }

    private fun setupView() {
        exchangeRateAdapter = ExchangeRatesAdapter(layoutInflater)
        binding.rvExchangeRates.adapter = exchangeRateAdapter
        binding.rlChooseCurrency.setOnClickListener {
            it.isClickable = false //To Avoid Multiple Click
            navigateToChooseCurrency()
            it.isClickable = true
        }

        binding.btnSync.setOnClickListener {
            viewModel.startSync()
        }

        binding.btnBackspace.setOnClickListener {
            clearEnteredCurrencyValue()
        }

        binding.edtCurrencyValue.filters = arrayOf(FloatingInputFilter())
        binding.edtCurrencyValue.addTextChangedListener {
            if (it == null) return@addTextChangedListener
            viewModel.onEnterCurrencyValue(it.toString())
        }
    }

    private fun clearEnteredCurrencyValue() {
        binding.edtCurrencyValue.setText("")
    }

    private fun navigateToChooseCurrency() {
        val navOptions = createChooseCurrencyNavOptions()
        findNavController().navigate(R.id.dest_choose_currency, null, navOptions)
    }

    private fun setupViewModel() {
        viewModel.isEmptyRates.observe(viewLifecycleOwner, {
            when (it) {
                true -> showEmptyRates()
                else -> hideEmptyRates()
            }
        })

        viewModel.selectedCurrencyType.observe(viewLifecycleOwner, {
            with(binding) {
                tvSelectedCurrencyCode.text = it.currencyCode
                tvSelectedCurrencyName.text = it.currencyName
            }
        })

        viewModel.lastUpdateDate.observe(viewLifecycleOwner, {
            binding.tvLastUpdateDate.text = it
        })

        viewModel.isSyncRunning.observe(viewLifecycleOwner, {
            when (it) {
                true -> {
                    binding.tvUpdateStatus.text = getString(R.string.updating)
                    startSyncRotate()
                    binding.btnSync.isEnabled = false
                }
                false -> {
                    binding.tvUpdateStatus.text = getString(R.string.last_update)
                    stopSyncRotation()
                    binding.btnSync.isEnabled = true
                }
            }
        })

        viewModel.isRatesDownloading.observe(viewLifecycleOwner, {
            when (it) {
                true -> showDownloadingRatesDialog()
                else -> hideDownloadingRatesDialog()
            }
        })

        viewModel.onNoConnection.observe(viewLifecycleOwner, EventObserver {
            showNoConnectionDialog()
        })

        viewModel.exchangeRates.observe(viewLifecycleOwner) {
            exchangeRateAdapter?.submitList(it){

                //Source: https://github.com/android/architecture-components-samples/
                //Disable Auto Scroll-up
                val layoutManager = (binding.rvExchangeRates.layoutManager as LinearLayoutManager)
                val position = layoutManager.findFirstCompletelyVisibleItemPosition()
                if (position != RecyclerView.NO_POSITION) {
                    binding.rvExchangeRates.scrollToPosition(position)
                }
            }
        }

        viewModel.currentRatePostfix.observe(viewLifecycleOwner) {
            val stringBuilder = StringBuilder()
                .append(getString(R.string.prefix_exchange_rates_title))
                .append(" ")
                .append(it)
            binding.tvRatesDescription.text = stringBuilder.toString()
        }
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
        exchangeRateAdapter?.setOnItemClickListener(null)
        exchangeRateAdapter = null
        noConnectionDialog?.setOnExitClickListener(null)
        noConnectionDialog?.setOnExitClickListener(null)
        hideNoConnectionDialog()
        noConnectionDialog = null
        hideDownloadingRatesDialog()
        rateDownloadingDialog = null
        stopSyncRotation()
        syncRotateAnimation = null
    }

    private fun showNoConnectionDialog() {
        noConnectionDialog = NoInternetConnectionDialog(requireContext())
        noConnectionDialog?.setOnExitClickListener {
            exitFromHome()
        }
        noConnectionDialog?.setOnTryAgainClickListener {

        }
        noConnectionDialog?.show()
    }

    private fun exitFromHome() {
        requireActivity().finish()
    }

    private fun hideNoConnectionDialog() {
        noConnectionDialog?.dismiss()
    }

    private fun startSyncRotate() {
        stopSyncRotation()
        syncRotateAnimation = RotateAnimation(
            0f, 360f, Animation.RELATIVE_TO_SELF,
            0.5f, Animation.RELATIVE_TO_SELF, 0.5f
        ).apply {
            duration = 800
            repeatCount = Animation.INFINITE
            repeatMode = Animation.RESTART
        }
        binding.btnSync.startAnimation(syncRotateAnimation)
    }

    private fun stopSyncRotation() {
        syncRotateAnimation?.cancel()
    }

    private fun showEmptyRates() {
        with(binding) {
            tvEmptyExchangeRate.visibility = View.VISIBLE
            imvEmptyWallet.visibility = View.VISIBLE
        }
    }

    private fun hideEmptyRates() {
        with(binding) {
            tvEmptyExchangeRate.visibility = View.INVISIBLE
            imvEmptyWallet.visibility = View.INVISIBLE
        }
    }

    private fun createChooseCurrencyNavOptions() =
        navOptions {
            anim {
                //Home Fragment Animation
                exit = R.anim.home_enter_right
                popEnter = R.anim.home_exit_left

                //Choose Currency Fragment Animation
                popExit = R.anim.choose_currency_exit
                enter = R.anim.choose_currency_enter
            }
        }

}