package com.fitfinance.app.presentation.ui.adapter.finance

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import com.fitfinance.app.R
import com.fitfinance.app.databinding.DialogFinanceDetailsCustomBinding
import com.fitfinance.app.databinding.ItemRecyclerViewBinding
import com.fitfinance.app.domain.model.FinanceType
import com.fitfinance.app.domain.response.FinanceGetResponse
import com.fitfinance.app.presentation.ui.adapter.BaseAdapter
import com.fitfinance.app.presentation.ui.adapter.ItemPositionProvider
import com.fitfinance.app.presentation.ui.financedetails.FinanceDetailsFragment
import com.fitfinance.app.util.toLocalDateBrFormat
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class FinanceAdapter(deleteListener: (Long) -> Unit) : BaseAdapter<FinanceGetResponse>(
    deleteListener,
    getItemType = { it.type.ordinal },
    getItemId = { it.id },
    getItemTitle = { it.name },
    bindItem = { finance, binding, context ->
        val itemBinding = binding as ItemRecyclerViewBinding
        itemBinding.tvTitle.text = finance.name
        itemBinding.mcvItem.backgroundTintList = context.getColorStateList(
            if (finance.type == FinanceType.INCOME) R.color.incometest else R.color.expensetest
        )
        itemBinding.ivInvestmentType.setImageResource(
            if (finance.type == FinanceType.INCOME) R.drawable.ic_money_income_24dp else R.drawable.ic_money_expense_24dp
        )
        itemBinding.mcvItem.setOnLongClickListener {
            showInfoDialog(context, finance)
            true
        }
        itemBinding.ibMoreOptions.setOnClickListener {
            showPopupMenu(context, it, finance, deleteListener)
        }
    }
), ItemPositionProvider {

    override fun getItemPositionById(itemId: String): Int {
        return currentList.indexOfFirst { it.name == itemId }
    }

    companion object {
        private fun showInfoDialog(context: Context, finance: FinanceGetResponse) {
            val inflater = LayoutInflater.from(context)
            val dialogView = DialogFinanceDetailsCustomBinding.inflate(inflater, null, false)
            dialogView.tvFinanceEndDate.visibility = View.VISIBLE

            dialogView.tvFinanceName.text = finance.name
            dialogView.tvFinanceValue.text = context.getString(R.string.txt_finance_value, finance.value)
            dialogView.tvFinanceDescription.text = context.getString(R.string.txt_finance_description, finance.description)
            dialogView.tvFinanceStartDate.text = context.getString(R.string.txt_finance_start_date, finance.startDate.toLocalDateBrFormat())
            dialogView.tvFinanceEndDate.text = context.getString(R.string.txt_finance_end_date, finance.endDate.toLocalDateBrFormat())


            val dialog = MaterialAlertDialogBuilder(context)
                .setView(dialogView.root)
                .setCancelable(true)
                .setPositiveButton(android.R.string.ok) { dialog, _ -> dialog.dismiss() }
                .create()

            dialog.show()
        }

        private fun showPopupMenu(context: Context, view: View, finance: FinanceGetResponse, deleteListener: (Long) -> Unit) {
            val popupMenu = PopupMenu(view.context, view)
            popupMenu.inflate(R.menu.adapter_item_menu)
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.view_item_details -> {
                        showInfoDialog(context, finance)
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

        private fun openFinanceDetailsFragment(view: View, finance: FinanceGetResponse) {
            val fragment = FinanceDetailsFragment.newInstance(finance)
            val fragmentManager = (view.context as AppCompatActivity).supportFragmentManager
            fragment.show(fragmentManager, "FinanceDetailsFragment")
        }
    }
}
