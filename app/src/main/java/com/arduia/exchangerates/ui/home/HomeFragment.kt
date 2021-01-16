package com.arduia.exchangerates.ui.home

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import androidx.lifecycle.lifecycleScope
import com.arduia.exchangerates.databinding.FragHomeBinding
import com.arduia.exchangerates.ui.common.BaseBindingFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Created by Aung Ye Htet at 16/1/2021 6:00 PM.
 */
@AndroidEntryPoint
class HomeFragment : BaseBindingFragment<FragHomeBinding>() {

    private var rateDownloadingDialog: Dialog? = null
    private var syncRotateAnimation: Animation? = null

    override fun createBinding(layoutInflater: LayoutInflater, parent: ViewGroup?): FragHomeBinding {
        return FragHomeBinding.inflate(layoutInflater, parent, false)
    }

    override fun onViewCreated(savedInstanceState: Bundle?) {
        super.onViewCreated(savedInstanceState)
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


}