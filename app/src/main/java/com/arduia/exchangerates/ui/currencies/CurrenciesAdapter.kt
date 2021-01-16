package com.arduia.exchangerates.ui.currencies

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.arduia.exchangerates.databinding.ItemCurrencyTypeBinding
import com.arduia.exchangerates.ui.common.CurrencyTypeItemUiModel

/**
 * Created by Aung Ye Htet at 16/01/2021 6:38 PM.
 */
class CurrenciesAdapter(private val layoutInflater: LayoutInflater) :
        ListAdapter<CurrencyTypeItemUiModel, CurrenciesAdapter.VH>(DIFF_CALLBACK) {

    private var onItemClickListener: ((CurrencyTypeItemUiModel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemCurrencyTypeBinding.inflate(layoutInflater, parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = getItem(position)
        with(holder.binding) {
            tvCountryName.text = item.countryName
            tvCurrencyCode.text = item.currencyCode
        }
    }

    private fun setOnItemClickListener(listener: ((CurrencyTypeItemUiModel) -> Unit)?) {
        this.onItemClickListener
    }

    inner class VH(val binding: ItemCurrencyTypeBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position == -1) return //When viewHolder is not ready.
            val item = getItem(position)
            onItemClickListener?.invoke(item)
        }
    }

}

private val DIFF_CALLBACK
    get() = object : DiffUtil.ItemCallback<CurrencyTypeItemUiModel>() {
        override fun areItemsTheSame(oldItem: CurrencyTypeItemUiModel, newItem: CurrencyTypeItemUiModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: CurrencyTypeItemUiModel, newItem: CurrencyTypeItemUiModel): Boolean {
            return (oldItem.countryName == newItem.countryName) and
                    (oldItem.currencyCode == newItem.currencyCode) and
                    (oldItem.id == newItem.id)
        }
    }