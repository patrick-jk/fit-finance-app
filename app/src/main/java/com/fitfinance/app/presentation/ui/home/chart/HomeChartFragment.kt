package com.fitfinance.app.presentation.ui.home.chart

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.BundleCompat
import androidx.fragment.app.Fragment
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.palettes.RangeColors
import com.fitfinance.app.R
import com.fitfinance.app.databinding.FragmentHomeChartBinding
import com.fitfinance.app.domain.response.HomeSummaryResponse

class HomeChartFragment : Fragment() {

    private lateinit var _binding: FragmentHomeChartBinding

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
        val root: View = _binding.root

        setupChart()
        return root
    }

    private fun setupChart() {
        val pieChart = AnyChart.pie3d()

        pieChart.data(chartData)

        val typedValue = TypedValue()
        val theme = requireContext().theme
        theme.resolveAttribute(android.R.attr.windowBackground, typedValue, true)
        val formattedColor = getFormattedColor(ContextCompat.getColor(requireContext(), typedValue.resourceId))

        pieChart.background().fill(formattedColor)

        pieChart.palette(RangeColors.instantiate().items("#4CAF50", "#F44336"))

        pieChart.title(resources.getString(R.string.txt_balance_vs_expense))
        pieChart.title().fontSize(22).fontColor(getFormattedColor(ContextCompat.getColor(requireContext(), R.color.txt_color)))
        pieChart.labels().position("outside").fontSize(20)
        pieChart.labels().fontColor(getFormattedColor(ContextCompat.getColor(requireContext(), R.color.txt_color)))
        pieChart.legend().fontSize(15).fontColor(getFormattedColor(ContextCompat.getColor(requireContext(), R.color.txt_color)))

        _binding.homeChart.setChart(pieChart)
    }

    private fun getFormattedColor(color: Int): String {
        return String.format("#%06X", 0xFFFFFF and color)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding.root.removeAllViews()
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