package com.arduia.exchangerates.ui.home

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
import com.arduia.exchangerates.R
import com.arduia.exchangerates.databinding.FragHomeBinding
import com.arduia.exchangerates.ui.common.BaseBindingFragment
import com.arduia.exchangerates.ui.common.EventObserver
import com.arduia.exchangerates.ui.common.NoInternetConnectionDialog
import com.arduia.exchangerates.ui.common.InternalServerErrorDialog
import dagger.hilt.android.AndroidEntryPoint
import java.lang.StringBuilder

/**
 * Created by Aung Ye Htet at 16/1/2021 6:00 PM.
 */
@AndroidEntryPoint
class HomeFragment : BaseBindingFragment<FragHomeBinding>() {

    private val viewModel by viewModels<HomeViewModel>()

    private var syncRotateAnimation: Animation? = null
    private var noConnectionDialog: NoInternetConnectionDialog? = null
    private var serverErrorDialog: InternalServerErrorDialog? = null

    private var exchangeRateAdapter: ExchangeRatesAdapter? = null

    override fun createBinding(
            layoutInflater: LayoutInflater,
            parent: ViewGroup?
    ) = FragHomeBinding.inflate(layoutInflater, parent, false)

    override fun onViewCreated(savedInstanceState: Bundle?) {
        super.onViewCreated(savedInstanceState)
        setupView()
        setupViewModel()
    }

    private fun setupView() {
        setupExchangeRateRecyclerView()
        setupSyncButton()
        setupBackspaceButton()
        setupCurrencyValueEditText()
    }

    private fun setupViewModel() {
        observeIsExchangeRatesEmpty()
        observeSelectedCurrencyType()
        observeLastUpdatedDate()
        observeIsSyncRunning()
        observeOnNoConnectionEvent()
        observeExchangeRates()
        observeCurrentRatePostfix()
        observerOnServerErrorEvent()
    }

    private fun observeIsExchangeRatesEmpty() {
        viewModel.isExchangeRatesEmpty.observe(viewLifecycleOwner, {
            when (it) {
                true -> {
                    showEmptyRates()
                    disableEnterCurrency()
                }
                else -> {
                    hideEmptyRates()
                    enableEnterCurrency()
                }
            }
        })
    }

    private fun observeSelectedCurrencyType() {
        viewModel.selectedCurrencyType.observe(viewLifecycleOwner, {
            with(binding) {
                tvSelectedCurrencyCode.text = it.currencyCode
                tvSelectedCurrencyName.text = it.currencyName
            }
        })
    }

    private fun observeLastUpdatedDate() {
        viewModel.lastUpdatedDate.observe(viewLifecycleOwner, {
            binding.tvLastUpdateDate.text = it
        })
    }

    private fun observeIsSyncRunning() {
        viewModel.isSyncRunning.observe(viewLifecycleOwner, {
            when (it) {
                true -> {
                    binding.tvUpdateStatus.text = getString(R.string.updating)
                    startSyncRotation()
                    binding.btnSync.isEnabled = false
                }
                false -> {
                    binding.tvUpdateStatus.text = getString(R.string.last_update)
                    stopSyncRotation()
                    binding.btnSync.isEnabled = true
                }
            }
        })
    }

    private fun observeOnNoConnectionEvent() {
        viewModel.onNoConnection.observe(viewLifecycleOwner, EventObserver {
            showNoConnectionDialog(it)
        })
    }

    private fun observeExchangeRates() {
        viewModel.exchangeRates.observe(viewLifecycleOwner) {
            exchangeRateAdapter?.submitList(it)
        }
    }

    private fun observeCurrentRatePostfix() {
        viewModel.currentRatePostfix.observe(viewLifecycleOwner) {
            val stringBuilder = StringBuilder()
                    .append(getString(R.string.prefix_exchange_rates_title))
                    .append(" ")
                    .append(it)
            binding.tvRatesDescription.text = stringBuilder.toString()
        }
    }

    private fun observerOnServerErrorEvent() {
        viewModel.onServerError.observe(viewLifecycleOwner, EventObserver {
            showServerErrorDialog()
        })
    }

    private fun setupCurrencyValueEditText() {
        binding.edtCurrencyValue.filters = arrayOf(FloatingInputFilter())
        binding.edtCurrencyValue.addTextChangedListener {
            if (it == null) return@addTextChangedListener
            viewModel.onEnterCurrencyValue(it.toString())
        }
    }

    private fun setupBackspaceButton() {
        binding.btnBackspace.setOnClickListener {
            clearEnteredCurrencyValue()
        }
    }

    private fun setupSyncButton() {
        binding.btnSync.setOnClickListener {
            viewModel.startSync()
        }
    }

    private fun setupExchangeRateRecyclerView() {
        exchangeRateAdapter = ExchangeRatesAdapter(layoutInflater)
        binding.rvExchangeRates.adapter = exchangeRateAdapter
        binding.rlChooseCurrency.setOnClickListener {
            it.isClickable = false //To Avoid Multiple Click
            navigateToChooseCurrency()
            it.isClickable = true
        }
    }

    private fun clearEnteredCurrencyValue() {
        binding.edtCurrencyValue.setText("")
    }

    private fun navigateToChooseCurrency() {
        val navOptions = createChooseCurrencyNavOptions()
        findNavController().navigate(R.id.dest_choose_currency, null, navOptions)
    }

    private fun disableEnterCurrency() {
        with(binding.edtCurrencyValue) {
            setText("")
            isEnabled = false
        }
    }

    private fun enableEnterCurrency() {
        binding.edtCurrencyValue.isEnabled = true
    }

    private fun showServerErrorDialog() {
        hideServerErrorDialog() //Hide old one if exit
        serverErrorDialog = InternalServerErrorDialog(requireContext())
        serverErrorDialog?.setOnExitClickListener {
            exitFromApp()
        }
        serverErrorDialog?.setOnTryAgainClickListener {
            hideServerErrorDialog()
            viewModel.startSync()
        }
        serverErrorDialog?.show()
    }

    private fun hideServerErrorDialog() {
        serverErrorDialog?.dismiss()
    }

    private fun showNoConnectionDialog(force: Boolean) {
        hideNoConnectionDialog()
        noConnectionDialog = NoInternetConnectionDialog(requireContext(), force)
        noConnectionDialog?.setOnExitClickListener {
            exitFromApp()
        }
        noConnectionDialog?.setOnTryAgainClickListener {
            hideNoConnectionDialog()
            viewModel.startSync()
        }
        noConnectionDialog?.show()
    }

    private fun exitFromApp() {
        requireActivity().finish()
    }

    private fun hideNoConnectionDialog() {
        noConnectionDialog?.dismiss()
    }

    private fun startSyncRotation() {
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

    override fun onBeforeBindingDestroyed() {
        super.onBeforeBindingDestroyed()
        exchangeRateAdapter?.setOnItemClickListener(null)
        exchangeRateAdapter = null
        hideNoConnectionDialog()
        noConnectionDialog?.dismiss()
        serverErrorDialog?.dismiss()
        noConnectionDialog = null
        serverErrorDialog = null
        stopSyncRotation()
        syncRotateAnimation = null
    }
}