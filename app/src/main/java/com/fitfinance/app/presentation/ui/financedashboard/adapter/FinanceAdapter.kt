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
import com.fitfinance.app.databinding.ItemRecyclerViewBinding
import com.fitfinance.app.domain.model.FinanceType
import com.fitfinance.app.domain.response.FinanceGetResponse
import com.fitfinance.app.presentation.ui.adapter.ItemPositionProvider
import com.fitfinance.app.presentation.ui.financedetails.FinanceDetailsFragment
import com.fitfinance.app.util.toLocalDateBrFormat
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class FinanceAdapter(val deleteListener: (Long) -> Unit) : ListAdapter<FinanceGetResponse, FinanceAdapter.FinanceViewHolder>(FinanceAdapter), ItemPositionProvider {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FinanceViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRecyclerViewBinding.inflate(inflater, parent, false)
        return FinanceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FinanceViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemPositionById(itemId: String): Int {
        return currentList.indexOfFirst { it.name == itemId }
    }

    inner class FinanceViewHolder(private val binding: ItemRecyclerViewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(finance: FinanceGetResponse) {
            binding.tvTitle.text = finance.name
            binding.mcvItem.backgroundTintList = binding.root.context.getColorStateList(
                if (finance.type == FinanceType.INCOME) R.color.incometest else R.color.expensetest
            )

            binding.ivInvestmentType.setImageResource(
                if(finance.type == FinanceType.INCOME) R.drawable.ic_money_income_24dp
                else R.drawable.ic_money_expense_24dp
            )

            binding.mcvItem.setOnLongClickListener {
                showInfoDialog(it, finance)
                true
            }

            binding.ibMoreOptions.setOnClickListener {
                showPopupMenu(it, finance)
            }
        }

        private fun showPopupMenu(view: View, finance: FinanceGetResponse) {
            val popupMenu = PopupMenu(view.context, view)
            popupMenu.inflate(R.menu.adapter_item_menu)
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.view_item_details -> {
                        showInfoDialog(view, finance)
                        true
                    }

                    R.id.edit_item -> {
                        openFinanceDetailsFragment(view, finance)
                        true
                    }

                    R.id.delete_item -> {
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

        private fun showInfoDialog(view: View, finance: FinanceGetResponse) {
            val inflater = LayoutInflater.from(view.context)
            val dialogView = DialogFinanceDetailsCustomBinding.inflate(inflater, null, false)
            dialogView.tvFinanceEndDate.visibility = View.VISIBLE

            dialogView.tvFinanceName.text = finance.name
            dialogView.tvFinanceValue.text = view.context.getString(R.string.txt_finance_value, finance.value)
            dialogView.tvFinanceDescription.text = view.context.getString(R.string.txt_finance_description, finance.description)
            dialogView.tvFinanceStartDate.text = view.context.getString(R.string.txt_finance_start_date, finance.startDate.toLocalDateBrFormat())
            dialogView.tvFinanceEndDate.text = view.context.getString(R.string.txt_finance_end_date, finance.endDate.toLocalDateBrFormat())


            val dialog = MaterialAlertDialogBuilder(view.context)
                .setView(dialogView.root)
                .setCancelable(true)
                .setPositiveButton(android.R.string.ok) { dialog, _ -> dialog.dismiss() }
                .create()

            dialog.show()
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