package com.fitfinance.app.presentation.ui.financedashboard

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import com.fitfinance.app.R
import com.fitfinance.app.databinding.DialogFilterListCustomBinding
import com.fitfinance.app.databinding.FragmentFinanceDashboardBinding
import com.fitfinance.app.domain.model.FinanceType
import com.fitfinance.app.domain.response.FinanceGetResponse
import com.fitfinance.app.presentation.statepattern.State
import com.fitfinance.app.presentation.ui.adapter.finance.FinanceAdapter
import com.fitfinance.app.presentation.ui.financedetails.FinanceDetailsFragment
import com.fitfinance.app.util.BaseDashboardFragment
import com.fitfinance.app.util.SHARED_PREF_NAME
import com.fitfinance.app.util.createDialog
import com.fitfinance.app.util.getProgressDialog
import com.fitfinance.app.util.getUserFriendlyErrorMessage
import com.fitfinance.app.util.hideSoftKeyboard
import com.fitfinance.app.util.scrollToItem
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FinanceDashboardFragment : BaseDashboardFragment() {
    private lateinit var _binding: FragmentFinanceDashboardBinding

    private val viewModel: FinanceDashboardViewModel by viewModels()

    private val sharedPreferences by lazy { requireContext().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE) }
    private lateinit var userToken: String

    private var financeList: List<FinanceGetResponse> = emptyList()

    private val financeAdapter by lazy {
        FinanceAdapter(deleteListener = {
            viewModel.deleteFinance(it, sharedPreferences.getString(getString(R.string.pref_user_token), "")!!)
        })
    }

    private var isAscending = false
    private var isTypeSorted = false
    private var isExpenseHidden = false
    private var isIncomeHidden = false

    private var progressDialog: AlertDialog? = null
    override val filterListFunction: () -> Unit = { filterFinances() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFinanceDashboardBinding.inflate(inflater, container, false)
        val root: View = _binding.root

        userToken = sharedPreferences.getString(getString(R.string.pref_user_token), "")!!

        requireActivity().supportFragmentManager.setFragmentResultListener(UPDATE_FINANCE_LIST, viewLifecycleOwner) { _, _ ->
            viewModel.getFinancesByUserId(userToken)
        }

        requireActivity().supportFragmentManager.setFragmentResultListener(EXECUTE_ITEM_CLICK, this) { _, _ ->
            arguments?.getString("itemId")?.let { args ->
                _binding.rvFinanceList.scrollToItem(args, financeAdapter)
            }
        }

        userToken.let {
            viewModel.getFinancesByUserId(it)
        }

        _binding.rvFinanceList.adapter = financeAdapter

        setupUi()
        super.setupMenuItems()
        return root
    }

    private fun setupUi() {
        _binding.fabAddFinance.setOnClickListener {
            FinanceDetailsFragment.newInstance().show(parentFragmentManager, "FinanceDetailsFragment")
        }
    }

    private fun filterFinances() {
        val dialogView = DialogFilterListCustomBinding.inflate(LayoutInflater.from(requireContext()), null, false)

        dialogView.cbFilterFinanceByNameAsc.isChecked = isAscending
        dialogView.cbFilterFinanceByType.isChecked = isTypeSorted
        dialogView.cbHideExpenses.isChecked = isExpenseHidden
        dialogView.cbHideIncome.isChecked = isIncomeHidden

        requireContext().createDialog {
            setView(dialogView.root)
            setPositiveButton(resources.getString(android.R.string.ok)) { _, _ ->
                dialogView.apply {
                    isAscending = cbFilterFinanceByNameAsc.isChecked
                    isTypeSorted = cbFilterFinanceByType.isChecked
                    isExpenseHidden = cbHideExpenses.isChecked
                    isIncomeHidden = cbHideIncome.isChecked
                }

                sortList()
            }
            setNegativeButton(resources.getString(android.R.string.cancel)) { dialog, _ -> dialog.dismiss() }
        }.show()
    }

    private fun sortList() {
        var filteredList = financeList

        if (isAscending) {
            filteredList = filteredList.sortedBy { it.name }
        }
        if (isTypeSorted) {
            filteredList = filteredList.sortedBy { it.type }
        }
        if (isExpenseHidden) {
            filteredList = filteredList.filter { it.type != FinanceType.EXPENSE }
        }
        if (isIncomeHidden) {
            filteredList = filteredList.filter { it.type != FinanceType.INCOME }
        }

        financeAdapter.submitList(filteredList)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        val filteredList = if (query.isNullOrEmpty()) {
            financeList
        } else {
            financeList.filter {
                it.name.contains(query, true)
            }
        }
        financeAdapter.submitList(filteredList)
        _binding.root.hideSoftKeyboard()
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        val filteredList = if (newText.isNullOrEmpty()) {
            financeList
        } else {
            financeList.filter {
                it.name.contains(newText, true)
            }
        }
        financeAdapter.submitList(filteredList)
        return true
    }

    override fun onStart() {
        super.onStart()
        viewModel.financesList.observe(viewLifecycleOwner) {
            when (it) {
                is State.Loading -> {
                    progressDialog = requireContext().getProgressDialog(resources.getString(R.string.txt_loading_finances))
                    progressDialog?.show()
                }

                is State.Success -> {
                    progressDialog?.dismiss()
                    financeList = it.info
                    financeAdapter.submitList(financeList)
                    requireActivity().supportFragmentManager.setFragmentResult(EXECUTE_ITEM_CLICK, Bundle())
                }

                is State.Error -> {
                    progressDialog?.dismiss()
                    requireContext().createDialog {
                        setTitle(resources.getString(R.string.txt_error))
                        setMessage(requireContext().getUserFriendlyErrorMessage(it.error))
                        setPositiveButton("OK", null)
                    }.show()
                }
            }
        }

        viewModel.financeDeleteObserver.observe(viewLifecycleOwner) {
            when (it) {
                is State.Loading -> {
                    progressDialog = requireContext().getProgressDialog(resources.getString(R.string.txt_deleting_finance))
                    progressDialog?.show()
                }

                is State.Success -> {
                    progressDialog?.dismiss()
                    requireActivity().supportFragmentManager.setFragmentResult(UPDATE_FINANCE_LIST, Bundle())
                }

                is State.Error -> {
                    progressDialog?.dismiss()
                    requireContext().createDialog {
                        setTitle(resources.getString(R.string.txt_error))
                        setMessage(requireContext().getUserFriendlyErrorMessage(it.error))
                        setPositiveButton("OK", null)
                    }.show()
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        arguments?.clear()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        requireActivity().removeMenuProvider(super.menuProvider)
        _binding.root.removeAllViews()
    }

    companion object {
        const val EXECUTE_ITEM_CLICK = "executeClick"
        const val UPDATE_FINANCE_LIST = "updateFinanceList"

        fun newInstance() = FinanceDashboardFragment()
    }
}