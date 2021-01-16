package com.arduia.exchangerates.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.arduia.exchangerates.databinding.ItemExchangeRateBinding

/**
 * Created by Aung Ye Htet at 16/01/2021 6:34 PM.
 */
class ExchangeRatesAdapter(private val layoutInflater: LayoutInflater) :
        ListAdapter<ExchangeRateItemUiModel, ExchangeRatesAdapter.VH>(DIFF_CALLBACK) {

    private var onItemClickListener: ((ExchangeRateItemUiModel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemExchangeRateBinding.inflate(layoutInflater, parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = getItem(position)
        with(holder.binding) {
            tvCurrencyCode.text = item.currencyCode
            tvCurrencyCountryName.text = item.countryName
            tvExchangeValue.text = item.exchangeRate
            tvExchangeBalance.text = item.exchangeBalance
        }
    }

    fun setOnItemClickListener(listener: ((ExchangeRateItemUiModel) -> Unit)?) {
        this.onItemClickListener = listener
    }

    inner class VH(val binding: ItemExchangeRateBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position == -1) return // When ViewHolder is not ready yet!
            val item = getItem(position)
            onItemClickListener?.invoke(item)
        }
    }
}

private val DIFF_CALLBACK
    get() = object : DiffUtil.ItemCallback<ExchangeRateItemUiModel>() {
        override fun areItemsTheSame(oldItem: ExchangeRateItemUiModel, newItem: ExchangeRateItemUiModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ExchangeRateItemUiModel, newItem: ExchangeRateItemUiModel): Boolean {
            return (oldItem.countryName == newItem.countryName) and
                    (oldItem.currencyCode == newItem.countryName) and
                    (oldItem.exchangeRate == newItem.exchangeRate) and
                    (oldItem.exchangeBalance == newItem.exchangeBalance)
        }
    }