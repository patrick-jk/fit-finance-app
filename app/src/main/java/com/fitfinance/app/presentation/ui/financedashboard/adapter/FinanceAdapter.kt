package com.fitfinance.app.presentation.ui.financedashboard.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fitfinance.app.R
import com.fitfinance.app.databinding.FinanceDetailsCustomDialogBinding
import com.fitfinance.app.databinding.ItemFinanceBinding
import com.fitfinance.app.domain.response.FinanceGetResponse
import com.fitfinance.app.presentation.ui.financedetails.FinanceDetailsFragment
import com.fitfinance.app.presentation.ui.financedetails.FinanceDetailsViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class FinanceAdapter : ListAdapter<FinanceGetResponse, FinanceAdapter.FinanceViewHolder>(FinanceAdapter) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FinanceViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemFinanceBinding.inflate(inflater, parent, false)
        return FinanceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FinanceViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class FinanceViewHolder(private val binding: ItemFinanceBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(finance: FinanceGetResponse) {
            binding.tvTitle.text = finance.name

            binding.ibMoreOptions.setOnClickListener {
                showPopupMenu(it, finance)
            }
        }

        private fun showPopupMenu(view: View, finance: FinanceGetResponse) {
            val popupMenu = PopupMenu(view.context, view)
            popupMenu.inflate(R.menu.item_finance_menu)
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.view_finance_details -> {
                        val inflater = LayoutInflater.from(view.context)
                        val dialogView = FinanceDetailsCustomDialogBinding.inflate(inflater, null, false)

                        dialogView.tvFinanceName.text = finance.name
                        dialogView.tvFinanceValue.text = finance.value.toString()
                        dialogView.tvFinanceDescription.text = finance.description
                        dialogView.tvFinanceStartDate.text = finance.startDate
                        dialogView.tvFinanceEndDate.text = finance.endDate

                        val dialog = MaterialAlertDialogBuilder(view.context)
                            .setView(dialogView.root)
                            .setCancelable(true)
                            .setPositiveButton(android.R.string.ok, null)
                            .create()

                        dialog.show()
                        true
                    }
                    R.id.edit_finance -> {
                        openFinanceDetailsFragment(view, finance)
                        true
                    }
                    R.id.delete_finance -> {
                        // Handle delete finance action
                        true
                    }
                    else -> false
                }
            }
            popupMenu.show()
        }

        private fun openFinanceDetailsFragment(view: View, finance: FinanceGetResponse) {
            val fragment = FinanceDetailsFragment.newInstance(finance)
            val fragmentManager = (view.context as AppCompatActivity).supportFragmentManager
            fragment.show(fragmentManager, "FinanceDetailsFragment")
        }
    }

    private companion object : DiffUtil.ItemCallback<FinanceGetResponse>() {
        override fun areItemsTheSame(oldItem: FinanceGetResponse, newItem: FinanceGetResponse): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: FinanceGetResponse, newItem: FinanceGetResponse): Boolean {
            return oldItem == newItem
        }
    }
}