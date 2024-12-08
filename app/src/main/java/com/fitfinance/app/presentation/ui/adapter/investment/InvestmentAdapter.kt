package com.fitfinance.app.presentation.ui.adapter.investment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import com.fitfinance.app.R
import com.fitfinance.app.databinding.DialogInvestmentDetailsCustomBinding
import com.fitfinance.app.databinding.ItemRecyclerViewBinding
import com.fitfinance.app.domain.model.InvestmentType
import com.fitfinance.app.domain.response.InvestmentGetResponse
import com.fitfinance.app.presentation.ui.adapter.BaseAdapter
import com.fitfinance.app.presentation.ui.adapter.ItemPositionProvider
import com.fitfinance.app.presentation.ui.investmentdetails.InvestmentDetailsFragment
import com.fitfinance.app.util.toLocalDateBrFormat
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class InvestmentAdapter(
    deleteListener: (Long) -> Unit,
    typeConverter: (String) -> String
) : BaseAdapter<InvestmentGetResponse>(
    deleteListener,
    getItemType = { it.type.ordinal },
    getItemId = { it.id },
    getItemTitle = { it.name },
    bindItem = { investment, binding, context ->
        val itemBinding = binding as ItemRecyclerViewBinding
        itemBinding.tvTitle.text = investment.name
        itemBinding.mcvItem.backgroundTintList = context.getColorStateList(
            when (investment.type) {
                InvestmentType.STOCK -> R.color.bg_stocks3
                InvestmentType.FII -> R.color.bg_fiis3
                InvestmentType.FIXED_INCOME -> R.color.bg_fixed_income3
            }
        )
        itemBinding.ivInvestmentType.setImageResource(
            when (investment.type) {
                InvestmentType.STOCK -> R.drawable.ic_bars_graphic_24dp
                InvestmentType.FII -> R.drawable.ic_house_24dp
                InvestmentType.FIXED_INCOME -> R.drawable.ic_coin_send_24dp
            }
        )
        itemBinding.mcvItem.setOnLongClickListener {
            showInfoDialog(context, investment, typeConverter)
            true
        }
        itemBinding.ibMoreOptions.setOnClickListener {
            showPopupMenu(context, it, investment, deleteListener, typeConverter)
        }
    }
), ItemPositionProvider {
    override fun getItemPositionById(itemId: String): Int {
        return currentList.indexOfFirst { it.name == itemId }
    }

    companion object {
        private fun showInfoDialog(context: Context, investment: InvestmentGetResponse, typeConverter: (String) -> String) {
            val inflater = LayoutInflater.from(context)
            val dialogView = DialogInvestmentDetailsCustomBinding.inflate(inflater, null, false)

            val investmentTypeUiFriendly = typeConverter(investment.type.name)

            dialogView.tvInvestmentName.text = investment.name
            dialogView.tvInvestmentType.text = context.getString(R.string.txt_investment_type, investmentTypeUiFriendly)
            dialogView.tvInvestmentCost.text = context.getString(R.string.txt_investment_cost, investment.price)
            dialogView.tvInvestmentQuantity.text = context.getString(R.string.txt_investment_quantity, investment.quantity)
            dialogView.tvInvestmentStartDate.text = context.getString(R.string.txt_finance_start_date, investment.startDate.toLocalDateBrFormat())
            investment.endDate?.let {
                dialogView.tvInvestmentEndDate.text = context.getString(R.string.txt_finance_end_date, it.toLocalDateBrFormat())
                dialogView.tvInvestmentEndDate.visibility = View.VISIBLE
            }

            val dialog = MaterialAlertDialogBuilder(context)
                .setView(dialogView.root)
                .setCancelable(true)
                .setPositiveButton(android.R.string.ok, null)
                .create()

            dialog.show()
        }

        private fun showPopupMenu(context: Context, view: View, investment: InvestmentGetResponse, deleteListener: (Long) -> Unit, typeConverter: (String) -> String) {
            val popupMenu = PopupMenu(view.context, view)
            popupMenu.inflate(R.menu.adapter_item_menu)

            val editInvestmentMenuItem = popupMenu.menu.findItem(R.id.edit_item)
            editInvestmentMenuItem.title = view.context.getString(R.string.txt_edit_investment)

            val deleteInvestmentMenuItem = popupMenu.menu.findItem(R.id.delete_item)
            deleteInvestmentMenuItem.title = view.context.getString(R.string.txt_delete_investment)

            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.view_item_details -> {
                        showInfoDialog(context, investment, typeConverter)
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
}
