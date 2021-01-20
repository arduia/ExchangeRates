package com.arduia.exchangerates.ui.currencies

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.arduia.exchangerates.databinding.ItemCurrencyTypeBinding
import com.arduia.exchangerates.ui.common.CurrencyTypeItemUiModel

/**
 * Created by Aung Ye Htet at 16/01/2021 6:38 PM.
 */
class CurrenciesAdapter(private val layoutInflater: LayoutInflater) :
        PagedListAdapter<CurrencyTypeItemUiModel, CurrenciesAdapter.VH>(DIFF_CALLBACK) {

    private var onItemClickListener: ((CurrencyTypeItemUiModel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemCurrencyTypeBinding.inflate(layoutInflater, parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = getItem(position) ?: throw Exception("getItem at position($position) not found!")
        with(holder.binding) {
            tvCurrencyName.text = item.currencyName
            tvCurrencyCode.text = item.currencyCode
        }
    }

    fun setOnItemClickListener(listener: ((CurrencyTypeItemUiModel) -> Unit)?) {
        this.onItemClickListener = listener
    }

    inner class VH(val binding: ItemCurrencyTypeBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            binding.rlCurrencies.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position == -1) return //When viewHolder is not ready.
            val item = getItem(position)
                    ?: throw Exception("getItem at position($position) not found!")
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
            return (oldItem.currencyName == newItem.currencyName) and
                    (oldItem.currencyCode == newItem.currencyCode) and
                    (oldItem.id == newItem.id)
        }
    }