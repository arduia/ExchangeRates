package com.arduia.exchangerates.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.arduia.exchangerates.R
import com.arduia.exchangerates.databinding.FragSplashBinding
import com.arduia.exchangerates.ui.common.BaseBindingFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Created by Aung Ye Htet at 16/1/2021 6:03 PM.
 */
class SplashFragment : BaseBindingFragment<FragSplashBinding>() {

    override fun createBinding(layoutInflater: LayoutInflater, parent: ViewGroup?): FragSplashBinding {
        return FragSplashBinding.inflate(layoutInflater, parent, false)
    }

    override fun onViewCreated(savedInstanceState: Bundle?) {
        super.onViewCreated(savedInstanceState)
        lifecycleScope.launch {
            delay(1000)
            with(findNavController()) {
                popBackStack()
                navigate(R.id.dest_home)
            }
        }
    }
}