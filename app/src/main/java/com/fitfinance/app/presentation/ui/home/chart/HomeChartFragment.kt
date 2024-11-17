package com.fitfinance.app.presentation.ui.home.chart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.BundleCompat
import androidx.fragment.app.Fragment
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.palettes.RangeColors
import com.fitfinance.app.R
import com.fitfinance.app.databinding.FragmentHomeChartBinding
import com.fitfinance.app.domain.response.HomeSummaryResponse

class HomeChartFragment : Fragment() {

    private var _binding: FragmentHomeChartBinding? = null
    private val binding get() = _binding!!

    private lateinit var chartData: List<ValueDataEntry>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val homeSummaryResponse = BundleCompat.getParcelable(it, CHART_DATA_EXTRA, HomeSummaryResponse::class.java)
            chartData = listOf(
                ValueDataEntry(resources.getString(R.string.txt_balance), homeSummaryResponse?.balance),
                ValueDataEntry(resources.getString(R.string.txt_expense), homeSummaryResponse?.totalExpenses)
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeChartBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupChart()
        return root
    }

    private fun setupChart() {
        val pieChart = AnyChart.pie3d()

        pieChart.data(chartData)

        pieChart.palette(RangeColors.instantiate().items("#1EFF00", "#FF2C1C"))

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

    companion object {
        const val CHART_DATA_EXTRA = "chart_data_extra"

        fun newInstance(chartData: HomeSummaryResponse): HomeChartFragment {
            val fragment = HomeChartFragment()
            fragment.arguments = Bundle().apply {
                putParcelable(CHART_DATA_EXTRA, chartData)
            }
            return fragment
        }
    }
}