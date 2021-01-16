package com.arduia.exchangerates.ui.home

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.arduia.exchangerates.databinding.DialogDownloadingRatesBinding

/**
 * Created by Aung Ye Htet at 16/1/2021 10:00 PM.
 */
class DownloadingRatesDialog (context: Context): AlertDialog(context) {

    private var _binding: DialogDownloadingRatesBinding? = DialogDownloadingRatesBinding.inflate(layoutInflater)
    private val binding get() = _binding!!

    init {
        setView(binding.root)
        setCancelable(false)
    }

    override fun dismiss() {
        super.dismiss()
        _binding = null
    }

}