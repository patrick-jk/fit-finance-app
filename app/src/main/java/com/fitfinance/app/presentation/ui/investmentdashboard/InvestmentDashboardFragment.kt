package com.fitfinance.app.presentation.ui.investmentdashboard

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import com.fitfinance.app.R
import com.fitfinance.app.databinding.FragmentInvestmentDashboardBinding
import com.fitfinance.app.domain.response.InvestmentGetResponse
import com.fitfinance.app.presentation.statepattern.State
import com.fitfinance.app.presentation.ui.investmentdashboard.adapter.InvestmentAdapter
import com.fitfinance.app.presentation.ui.investmentdetails.InvestmentDetailsFragment
import com.fitfinance.app.util.SHARED_PREF_NAME
import com.fitfinance.app.util.createDialog
import com.fitfinance.app.util.getDashboardMenuProvider
import com.fitfinance.app.util.getNoConnectionErrorOrExceptionMessage
import com.fitfinance.app.util.getProgressDialog
import com.fitfinance.app.util.hideSoftKeyboard
import com.fitfinance.app.util.scrollToItem
import org.koin.androidx.viewmodel.ext.android.viewModel

class InvestmentDashboardFragment : Fragment(), SearchView.OnQueryTextListener {
    private var _binding: FragmentInvestmentDashboardBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<InvestmentDashboardViewModel>()

    private val sharedPreferences by lazy { requireContext().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE) }
    private var investmentList: List<InvestmentGetResponse> = emptyList()


    private val investmentAdapter by lazy {
        InvestmentAdapter(deleteListener = {
            viewModel.deleteInvestment(it, sharedPreferences.getString(getString(R.string.pref_user_token), "")!!)
        }, typeConverter = { convertInvestmentTypeToUi(it) })
    }

    private var progressDialog: AlertDialog? = null
    private lateinit var menuProvider: MenuProvider


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInvestmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        requireActivity().supportFragmentManager.setFragmentResultListener("updateInvestmentList", viewLifecycleOwner) { _, _ ->
            viewModel.getInvestmentsByUserId(sharedPreferences.getString(getString(R.string.pref_user_token), "")!!)
        }

        val sharedPreferences = requireContext().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        sharedPreferences.getString(getString(R.string.pref_user_token), "")?.let {
            viewModel.getInvestmentsByUserId(it)
        }

        binding.rvInvestmentList.adapter = investmentAdapter
        setupUi()
        setupSearch()

        return root
    }

    private fun setupUi() {
        binding.fabAddInvestment.setOnClickListener {
            InvestmentDetailsFragment.newInstance().show(parentFragmentManager, "InvestmentDetailsFragment")
        }
    }

    private fun setupSearch() {
        menuProvider = getDashboardMenuProvider(this)
        requireActivity().addMenuProvider(menuProvider)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        val filteredList = if (query.isNullOrEmpty()) {
            investmentList
        } else {
            investmentList.filter {
                it.name.contains(query, true)
            }
        }
        investmentAdapter.submitList(filteredList)
        binding.root.hideSoftKeyboard()
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        val filteredList = if (newText.isNullOrEmpty()) {
            investmentList
        } else {
            investmentList.filter {
                it.name.contains(newText, true)
            }
        }
        investmentAdapter.submitList(filteredList)
        return true
    }

    private fun convertInvestmentTypeToUi(investmentType: String): String {
        return when (investmentType) {
            "STOCK" -> resources.getStringArray(R.array.investment_types)[0]
            "FII" -> resources.getStringArray(R.array.investment_types)[1]
            "FIXED_INCOME" -> resources.getStringArray(R.array.investment_types)[2]
            else -> resources.getStringArray(R.array.investment_types)[0]
        }
    }

    override fun onStart() {
        super.onStart()

        viewModel.investmentsList.observe(viewLifecycleOwner) {
            when (it) {
                is State.Loading -> {
                    progressDialog = requireContext().getProgressDialog(resources.getString(R.string.txt_loading_investments))
                    progressDialog?.show()
                }

                is State.Success -> {
                    progressDialog?.dismiss()
                    investmentList = it.info
                    investmentAdapter.submitList(investmentList)
                    arguments?.getString("itemId")?.let { investmentId ->
                        binding.rvInvestmentList.scrollToItem(investmentId, investmentAdapter)
                    }
                }

                is State.Error -> {
                    progressDialog?.dismiss()
                    requireContext().createDialog {
                        setMessage(it.error.message.toString())
                        setPositiveButton("OK", null)
                    }.show()
                }
            }
        }

        viewModel.investmentDeleteObserver.observe(viewLifecycleOwner) {
            when (it) {
                is State.Loading -> {
                    progressDialog = requireContext().getProgressDialog(resources.getString(R.string.txt_deleting_investment))
                    progressDialog?.show()
                }

                is State.Success -> {
                    progressDialog?.dismiss()
                    viewModel.getInvestmentsByUserId(sharedPreferences.getString(getString(R.string.pref_user_token), "")!!)
                }

                is State.Error -> {
                    progressDialog?.dismiss()
                    requireContext().createDialog {
                        setTitle(resources.getString(R.string.txt_error))
                        setMessage(requireContext().getNoConnectionErrorOrExceptionMessage(it.error))
                        setPositiveButton("OK", null)
                    }.show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        requireActivity().removeMenuProvider(menuProvider)
        _binding = null
    }
}