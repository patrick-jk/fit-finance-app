package com.fitfinance.app.presentation.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.fitfinance.app.databinding.ItemRecyclerViewBinding

abstract class BaseAdapter<T : Any>(
    private val deleteListener: (Long) -> Unit,
    private val getItemType: (T) -> Int,
    getItemId: (T) -> Long,
    private val getItemTitle: (T) -> String,
    private val bindItem: (T, ViewBinding, Context) -> Unit
) : ListAdapter<T, BaseAdapter.BaseViewHolder<T>>(BaseDiffCallback(getItemId)) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<T> {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRecyclerViewBinding.inflate(inflater, parent, false)
        return BaseViewHolder(binding, bindItem)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<T>, position: Int) {
        holder.bind(getItem(position))
    }

    class BaseViewHolder<T>(
        private val binding: ItemRecyclerViewBinding, private val bindItem: (T, ViewBinding, Context) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: T) {
            bindItem(item, binding, binding.root.context)
        }
    }

    class BaseDiffCallback<T : Any>(private val getItemId: (T) -> Long) : DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
            return getItemId(oldItem) == getItemId(newItem)
        }

        override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
            return oldItem == newItem
        }
    }
}
