package com.arduia.exchangerates.ui.home

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.arduia.exchangerates.R
import com.arduia.exchangerates.databinding.FragHomeBinding
import com.arduia.exchangerates.ui.common.BaseBindingFragment
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by Aung Ye Htet at 16/1/2021 6:00 PM.
 */
@AndroidEntryPoint
class HomeFragment : BaseBindingFragment<FragHomeBinding>() {

    private val viewModel by viewModels<HomeViewModel>()

    private var rateDownloadingDialog: Dialog? = null
    private var syncRotateAnimation: Animation? = null

    override fun createBinding(layoutInflater: LayoutInflater, parent: ViewGroup?): FragHomeBinding {
        return FragHomeBinding.inflate(layoutInflater, parent, false)
    }

    override fun onViewCreated(savedInstanceState: Bundle?) {
        super.onViewCreated(savedInstanceState)
        setupView()
        setupViewModel()
    }

    private fun setupView() {
        binding.rlChooseCurrency.setOnClickListener {
            it.isClickable = false //To Avoid Multiple Click
            navigateToChooseCurrency()
            it.isClickable = true
        }
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
        rateDownloadingDialog = null
        stopSyncRotation()
        syncRotateAnimation = null
    }

    private fun startSyncRotate() {
        stopSyncRotation()
        syncRotateAnimation = RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f).apply {
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