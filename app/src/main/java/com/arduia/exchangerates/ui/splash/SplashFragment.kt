package com.arduia.exchangerates.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.arduia.exchangerates.R
import com.arduia.exchangerates.databinding.FragSplashBinding
import com.arduia.exchangerates.ui.common.BaseBindingFragment
import com.arduia.exchangerates.ui.common.EventObserver
import com.arduia.exchangerates.ui.common.ext.getApplicationVersionName
import dagger.hilt.android.AndroidEntryPoint
import java.lang.StringBuilder

/**
 * Created by Aung Ye Htet at 16/1/2021 6:03 PM.
 */
@AndroidEntryPoint
class SplashFragment : BaseBindingFragment<FragSplashBinding>() {

    private val viewModel by viewModels<SplashViewModel>()

    override fun createBinding(layoutInflater: LayoutInflater, parent: ViewGroup?): FragSplashBinding {
        return FragSplashBinding.inflate(layoutInflater, parent, false)
    }

    override fun onViewCreated(savedInstanceState: Bundle?) {
        super.onViewCreated(savedInstanceState)
        setupView()
        setupViewModel()
    }

    private fun setupView() {

        binding.tvVersion.text = StringBuilder().append(getString(R.string.prefix_version))
                .append(" ")
                .append(requireContext().getApplicationVersionName())
                .toString()
    }

    private fun setupViewModel() {
        viewModel.isLoading.observe(viewLifecycleOwner, {
            when (it) {
                true -> showLoadingProgress()
                else -> hideLoadingProgress()
            }
        })

        viewModel.onSplashFinished.observe(viewLifecycleOwner, EventObserver {
            navigateToHome()
        })

    }

    private fun navigateToHome() {
        with(findNavController()) {
            popBackStack()
            navigate(R.id.dest_home)
        }
    }


    private fun showLoadingProgress() {
        binding.pbLoading.visibility = View.VISIBLE
    }

    private fun hideLoadingProgress() {
        binding.pbLoading.visibility = View.INVISIBLE
    }
}