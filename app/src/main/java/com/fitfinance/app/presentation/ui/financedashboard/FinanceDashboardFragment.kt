package com.fitfinance.app.presentation.ui.financedashboard

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import com.fitfinance.app.R
import com.fitfinance.app.databinding.DialogFilterListCustomBinding
import com.fitfinance.app.databinding.FragmentFinanceDashboardBinding
import com.fitfinance.app.domain.model.FinanceType
import com.fitfinance.app.domain.response.FinanceGetResponse
import com.fitfinance.app.presentation.statepattern.State
import com.fitfinance.app.presentation.ui.financedashboard.adapter.FinanceAdapter
import com.fitfinance.app.presentation.ui.financedetails.FinanceDetailsFragment
import com.fitfinance.app.util.SHARED_PREF_NAME
import com.fitfinance.app.util.createDialog
import com.fitfinance.app.util.getProgressDialog
import com.fitfinance.app.util.getUserFriendlyErrorMessage
import com.fitfinance.app.util.hideSoftKeyboard
import com.fitfinance.app.util.scrollToItem
import org.koin.androidx.viewmodel.ext.android.viewModel

class FinanceDashboardFragment : Fragment(), SearchView.OnQueryTextListener {
    private var _binding: FragmentFinanceDashboardBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<FinanceDashboardViewModel>()

    private val sharedPreferences by lazy { requireContext().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE) }
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
    private lateinit var menuProvider: MenuProvider

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFinanceDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        requireActivity().supportFragmentManager.setFragmentResultListener(UPDATE_FINANCE_LIST, viewLifecycleOwner) { _, _ ->
            viewModel.getFinancesByUserId(sharedPreferences.getString(getString(R.string.pref_user_token), "")!!)
        }

        requireActivity().supportFragmentManager.setFragmentResultListener(EXECUTE_ITEM_CLICK, this) { _, _ ->
            arguments?.getString("itemId")?.let { args ->
                binding.rvFinanceList.scrollToItem(args, financeAdapter)
            }
        }

        val sharedPreferences = requireContext().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        sharedPreferences.getString(getString(R.string.pref_user_token), "")?.let {
            viewModel.getFinancesByUserId(it)
        }

        binding.rvFinanceList.adapter = financeAdapter

        setupUi()
        setupMenuItems()
        return root
    }

    private fun setupUi() {
        binding.fabAddFinance.setOnClickListener {
            FinanceDetailsFragment.newInstance().show(parentFragmentManager, "FinanceDetailsFragment")
        }
    }

    private fun setupMenuItems() {
        menuProvider = object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.fragment_dashboard_menu, menu)
                if (menu.findItem(R.id.menu_action_search) != null) {
                    val searchItem = menu.findItem(R.id.menu_action_search)
                    val searchView = searchItem.actionView as SearchView
                    searchView.setOnQueryTextListener(this@FinanceDashboardFragment)
                }
                if (menu.findItem(R.id.menu_action_filter) != null) {
                    val filterItem = menu.findItem(R.id.menu_action_filter)
                    filterItem.setOnMenuItemClickListener {
                        filterFinances()
                        true
                    }
                }
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean = true
        }
        requireActivity().addMenuProvider(menuProvider, viewLifecycleOwner)
    }

    fun filterFinances() {
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
        binding.root.hideSoftKeyboard()
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
        requireActivity().removeMenuProvider(menuProvider)
        _binding = null
    }

    companion object {
        const val EXECUTE_ITEM_CLICK = "executeClick"
        const val UPDATE_FINANCE_LIST = "updateFinanceList"

        fun newInstance() = FinanceDashboardFragment()
    }
}