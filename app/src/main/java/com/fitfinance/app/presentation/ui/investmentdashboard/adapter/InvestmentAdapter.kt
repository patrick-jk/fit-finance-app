package com.fitfinance.app.presentation.ui.investmentdashboard.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fitfinance.app.R
import com.fitfinance.app.databinding.DialogInvestmentDetailsCustomBinding
import com.fitfinance.app.databinding.ItemRecyclerViewBinding
import com.fitfinance.app.domain.model.InvestmentType
import com.fitfinance.app.domain.response.InvestmentGetResponse
import com.fitfinance.app.presentation.ui.investmentdetails.InvestmentDetailsFragment
import com.fitfinance.app.util.toLocalDateBrFormat
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class InvestmentAdapter(val deleteListener: (Long) -> Unit, val typeConverter: (String) -> String) :
    ListAdapter<InvestmentGetResponse, InvestmentAdapter.InvestmentViewHolder>(InvestmentAdapter) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InvestmentViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRecyclerViewBinding.inflate(inflater, parent, false)
        return InvestmentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: InvestmentViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class InvestmentViewHolder(private val binding: ItemRecyclerViewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(investment: InvestmentGetResponse) {
            binding.tvTitle.text = investment.name
            binding.mcvItemFinance.backgroundTintList = binding.root.context.getColorStateList(
                when (investment.type) {
                    InvestmentType.STOCK -> R.color.bg_stocks
                    InvestmentType.FII -> R.color.bg_fiis
                    InvestmentType.FIXED_INCOME -> R.color.bg_fixed_income
                }
            )

            binding.ibMoreOptions.setOnClickListener {
                showPopupMenu(it, investment)
            }
        }

        private fun showPopupMenu(view: View, investment: InvestmentGetResponse) {
            val popupMenu = PopupMenu(view.context, view)
            popupMenu.inflate(R.menu.adapter_item_menu)

            val editInvestmentMenuItem = popupMenu.menu.findItem(R.id.edit_item)
            editInvestmentMenuItem.title = view.context.getString(R.string.txt_edit_investment)

            val deleteInvestmentMenuItem = popupMenu.menu.findItem(R.id.delete_item)
            deleteInvestmentMenuItem.title = view.context.getString(R.string.txt_delete_investment)

            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.view_item_details -> {


                        val inflater = LayoutInflater.from(view.context)
                        val dialogView = DialogInvestmentDetailsCustomBinding.inflate(inflater, null, false)

                        val investmentTypeUiFriendly = typeConverter(investment.type.name)

                        dialogView.tvInvestmentName.text = investment.name
                        dialogView.tvInvestmentType.text = view.context.getString(R.string.txt_investment_type, investmentTypeUiFriendly)
                        dialogView.tvInvestmentCost.text = view.context.getString(R.string.txt_investment_cost, investment.price)
                        dialogView.tvInvestmentQuantity.text = view.context.getString(R.string.txt_investment_quantity, investment.quantity)
                        dialogView.tvInvestmentStartDate.text = view.context.getString(R.string.txt_finance_start_date, investment.startDate.toLocalDateBrFormat())
                        investment.endDate?.let {
                            dialogView.tvInvestmentEndDate.text = view.context.getString(R.string.txt_finance_end_date, it.toLocalDateBrFormat())
                            dialogView.tvInvestmentEndDate.visibility = View.VISIBLE
                        }

                        val dialog = MaterialAlertDialogBuilder(view.context)
                            .setView(dialogView.root)
                            .setCancelable(true)
                            .setPositiveButton(android.R.string.ok, null)
                            .create()

                        dialog.show()
                        true
                    }

                    R.id.edit_item -> {
                        openInvestmentDetailsFragment(view, investment)
                        true
                    }

                    R.id.delete_item -> {
                        MaterialAlertDialogBuilder(view.context)
                            .setTitle(R.string.txt_delete_investment_title)
                            .setMessage(R.string.txt_delete_investment_message)
                            .setPositiveButton(android.R.string.ok) { _, _ ->
                                deleteListener(investment.id)
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

        private fun openInvestmentDetailsFragment(view: View, investment: InvestmentGetResponse) {
            val fragment = InvestmentDetailsFragment.newInstance(investment)
            val fragmentManager = (view.context as AppCompatActivity).supportFragmentManager
            fragment.show(fragmentManager, "InvestmentDetailsFragment")
        }
    }

    private companion object : DiffUtil.ItemCallback<InvestmentGetResponse>() {
        override fun areItemsTheSame(oldItem: InvestmentGetResponse, newItem: InvestmentGetResponse): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: InvestmentGetResponse, newItem: InvestmentGetResponse): Boolean {
            return oldItem == newItem
        }
    }
}