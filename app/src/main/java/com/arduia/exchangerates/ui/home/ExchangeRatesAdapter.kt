package com.arduia.exchangerates.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.arduia.exchangerates.databinding.ItemExchangeRateBinding
import java.lang.Exception

/**
 * Created by Aung Ye Htet at 16/01/2021 6:34 PM.
 */
class ExchangeRatesAdapter(private val layoutInflater: LayoutInflater) :
        PagedListAdapter<ExchangeRateItemUiModel, ExchangeRatesAdapter.VH>(DIFF_CALLBACK) {

    private var onItemClickListener: ((ExchangeRateItemUiModel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemExchangeRateBinding.inflate(layoutInflater, parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = getItem(position) ?: return
        with(holder.binding) {
            tvCurrencyCode.text = item.currencyCode
            tvCurrencyName.text = item.currencyName
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
            val item = getItem(position) ?: throw Exception("getItem position $position not found!")
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
            return (oldItem.currencyName == newItem.currencyName) and
                    (oldItem.currencyCode == newItem.currencyName) and
                    (oldItem.exchangeRate == newItem.exchangeRate) and
                    (oldItem.exchangeBalance == newItem.exchangeBalance)
        }
    }