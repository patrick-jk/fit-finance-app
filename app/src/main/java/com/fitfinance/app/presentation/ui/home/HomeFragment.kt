package com.fitfinance.app.presentation.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.fitfinance.app.R
import com.fitfinance.app.databinding.FragmentHomeBinding
import com.fitfinance.app.presentation.statepattern.State
import com.fitfinance.app.presentation.ui.home.chart.HomeChartFragment
import com.fitfinance.app.presentation.ui.home.homesummary.HomeSummaryFragment
import com.fitfinance.app.util.SHARED_PREF_NAME
import com.fitfinance.app.util.createDialog
import com.fitfinance.app.util.getProgressDialog
import com.fitfinance.app.util.getUserFriendlyErrorMessage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var _binding: FragmentHomeBinding

    private val viewModel: HomeViewModel by viewModels()

    private val sharedPreferences by lazy {
        requireActivity().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
    }
    private var progressDialog: AlertDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = _binding.root

        viewModel.getHomeData(sharedPreferences.getString(resources.getString(R.string.pref_user_token), "")!!)
        return root
    }

    override fun onStart() {
        super.onStart()
        viewModel.homeSummary.observe(viewLifecycleOwner) {
            when (it) {
                is State.Loading -> {
                    progressDialog = requireContext().getProgressDialog(resources.getString(R.string.txt_loading_home_data))
                    progressDialog?.show()
                }

                is State.Success -> {
                    progressDialog?.dismiss()

                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fragment_home_chart_container, HomeChartFragment.newInstance(it.info))
                        .commit()

                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fragment_home_summary_container, HomeSummaryFragment.newInstance(it.info))
                        .commit()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding.root.removeAllViews()
    }
}