package com.arduia.exchangerates.ui.currencies

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.arduia.exchangerates.databinding.DialogDownloadingCurrenciesBinding

/**
 * Created by Aung Ye Htet at 16/1/2021 10:03 PM.
 */
class DownloadingCurrenciesDialog(context: Context) : AlertDialog(context) {

    private var _binding: DialogDownloadingCurrenciesBinding? =
            DialogDownloadingCurrenciesBinding.inflate(layoutInflater)
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