package com.fitfinance.app.presentation.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.palettes.RangeColors
import com.fitfinance.app.R
import com.fitfinance.app.databinding.FragmentHomeBinding
import com.fitfinance.app.domain.response.HomeSummaryResponse
import com.fitfinance.app.presentation.statepattern.State
import com.fitfinance.app.presentation.ui.home.homesummary.HomeSummaryFragment
import com.fitfinance.app.util.SHARED_PREF_NAME
import com.fitfinance.app.util.createDialog
import com.fitfinance.app.util.getNoConnectionErrorOrExceptionMessage
import com.fitfinance.app.util.getProgressDialog
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<HomeViewModel>()

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
        val root: View = binding.root

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
                    setupChart(it.info)
                    HomeSummaryFragment.newInstance(it.info)
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fragment_home_summary_container, HomeSummaryFragment.newInstance(it.info))
                        .commit()
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

    private fun setupChart(info: HomeSummaryResponse) {
        val pieChart = AnyChart.pie3d()

        val data = listOf(
            ValueDataEntry(resources.getString(R.string.txt_balance), info.balance),
            ValueDataEntry(resources.getString(R.string.txt_expense), info.totalExpenses)
        )

        pieChart.palette(RangeColors.instantiate().items("#1EFF00", "#FF2C1C"))

        pieChart.data(data)

        pieChart.title(resources.getString(R.string.txt_balance_vs_expense))
        pieChart.title().fontSize(22).fontColor("#000000")
        pieChart.labels().position("outside").fontSize(20)
        pieChart.labels()
        pieChart.legend().fontSize(15)

        binding.homeChart.setChart(pieChart)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}