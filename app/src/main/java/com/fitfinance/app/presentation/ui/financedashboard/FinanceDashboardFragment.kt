package com.fitfinance.app.presentation.ui.financedashboard

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.fitfinance.app.R
import com.fitfinance.app.databinding.FragmentFinanceDashboardBinding
import com.fitfinance.app.presentation.statepattern.State
import com.fitfinance.app.presentation.ui.financedashboard.adapter.FinanceAdapter
import com.fitfinance.app.presentation.ui.financedetails.FinanceDetailsFragment
import com.fitfinance.app.util.SHARED_PREF_NAME
import com.fitfinance.app.util.createDialog
import com.fitfinance.app.util.getNoConnectionErrorOrExceptionMessage
import com.fitfinance.app.util.getProgressDialog
import org.koin.androidx.viewmodel.ext.android.viewModel

class FinanceDashboardFragment : Fragment() {
    private var _binding: FragmentFinanceDashboardBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<FinanceDashboardViewModel>()

    private val sharedPreferences by lazy { requireContext().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE) }
    private val financeAdapter by lazy {
        FinanceAdapter(deleteListener = {
            viewModel.deleteFinance(it, sharedPreferences.getString(getString(R.string.pref_user_token), "")!!)
        })
    }
    private var progressDialog: AlertDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFinanceDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        requireActivity().supportFragmentManager.setFragmentResultListener("updateFinanceList", viewLifecycleOwner) { _, _ ->
            viewModel.getFinancesByUserId(sharedPreferences.getString(getString(R.string.pref_user_token), "")!!)
        }

        val sharedPreferences = requireContext().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        sharedPreferences.getString(getString(R.string.pref_user_token), "")?.let {
            viewModel.getFinancesByUserId(it)
        }

        binding.rvFinanceList.adapter = financeAdapter
        setupUi()

        return root
    }

    private fun setupUi() {
        binding.fabAddFinance.setOnClickListener {
            FinanceDetailsFragment.newInstance().show(parentFragmentManager, "FinanceDetailsFragment")
        }
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
                    financeAdapter.submitList(it.info)
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

        viewModel.financeDeleteObserver.observe(viewLifecycleOwner) {
            when (it) {
                is State.Loading -> {
                    progressDialog = requireContext().getProgressDialog(resources.getString(R.string.txt_deleting_finance))
                    progressDialog?.show()
                }

                is State.Success -> {
                    progressDialog?.dismiss()
                    requireActivity().supportFragmentManager.setFragmentResult("updateFinanceList", Bundle())
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
        _binding = null
    }
}