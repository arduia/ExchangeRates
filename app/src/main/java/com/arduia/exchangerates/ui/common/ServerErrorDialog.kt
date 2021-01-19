package com.arduia.exchangerates.ui.common

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.arduia.exchangerates.databinding.DialogServerErrorBinding

class ServerErrorDialog(context: Context) : AlertDialog(context) {
    private var _binding: DialogServerErrorBinding? = DialogServerErrorBinding.inflate(layoutInflater)
    private val binding get() = _binding!!

    private var onExitClickListener: (() -> Unit)? = null
    private var onTryAgainClickListener: (() -> Unit)? = null

    init {
        setView(binding.root)
        setCancelable(false)
        setupView()
    }

    private fun setupView() {
        with(binding) {
            btnExit.setOnClickListener { onExitClickListener?.invoke() }
            btnTryAgain.setOnClickListener {
                onTryAgainClickListener?.invoke()
            }
        }
    }

    fun setOnExitClickListener(listener: (() -> Unit)?) {
        this.onExitClickListener = listener
    }

    fun setOnTryAgainClickListener(listener: (() -> Unit)?) {
        this.onTryAgainClickListener = listener
    }

    override fun dismiss() {
        super.dismiss()
        onExitClickListener = null
        onTryAgainClickListener = null
        _binding?.btnTryAgain?.setOnClickListener(null)
        _binding?.btnExit?.setOnClickListener(null)
        _binding = null
    }
}