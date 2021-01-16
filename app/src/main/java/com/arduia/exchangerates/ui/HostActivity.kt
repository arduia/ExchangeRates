package com.arduia.exchangerates.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.arduia.exchangerates.R
import com.arduia.exchangerates.databinding.FragHomeBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 *  Created by Aung Ye Htet at 15/01/2021 11:32 PM
 */
@AndroidEntryPoint
class HostActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activ_host)
    }

}