package com.arduia.exchangerates.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.arduia.exchangerates.R
import com.arduia.exchangerates.databinding.ActivHostBinding
import com.arduia.exchangerates.databinding.FragHomeBinding
import com.arduia.exchangerates.ui.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 *  Created by Aung Ye Htet at 15/01/2021 11:32 PM.
 */
@AndroidEntryPoint
class HostActivity : AppCompatActivity() {

    private val viewModel by viewModels<HostViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivHostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.toString() // Just to activate viewModel
    }

}