package com.fitfinance.app.presentation.ui.financedashboard.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fitfinance.app.R
import com.fitfinance.app.databinding.DialogFinanceDetailsCustomBinding
import com.fitfinance.app.databinding.ItemFinanceBinding
import com.fitfinance.app.domain.model.FinanceType
import com.fitfinance.app.domain.response.FinanceGetResponse
import com.fitfinance.app.presentation.ui.financedetails.FinanceDetailsFragment
import com.fitfinance.app.util.toLocalDateBrFormat
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class FinanceAdapter(val deleteListener: (Long) -> Unit) : ListAdapter<FinanceGetResponse, FinanceAdapter.FinanceViewHolder>(FinanceAdapter) {

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
            binding.mcvItemFinance.backgroundTintList = binding.root.context.getColorStateList(
                if (finance.type == FinanceType.INCOME) R.color.income else R.color.expense
            )

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
                        val dialogView = DialogFinanceDetailsCustomBinding.inflate(inflater, null, false)

                        dialogView.tvFinanceName.text = finance.name
                        dialogView.tvFinanceValue.text = view.context.getString(R.string.txt_finance_value, finance.value)
                        dialogView.tvFinanceDescription.text = view.context.getString(R.string.txt_finance_description, finance.description)
                        dialogView.tvFinanceStartDate.text = view.context.getString(R.string.txt_finance_start_date, finance.startDate.toLocalDateBrFormat())
                        dialogView.tvFinanceEndDate.text = view.context.getString(R.string.txt_finance_end_date, finance.endDate.toLocalDateBrFormat())

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
                        MaterialAlertDialogBuilder(view.context)
                            .setTitle(R.string.txt_delete_finance_title)
                            .setMessage(R.string.txt_delete_finance_message)
                            .setPositiveButton(android.R.string.ok) { _, _ ->
                                deleteListener(finance.id)
                            }
                            .setNegativeButton(android.R.string.cancel, null)
                            .show()
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