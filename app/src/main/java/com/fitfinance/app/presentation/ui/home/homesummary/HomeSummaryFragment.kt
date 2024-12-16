package com.fitfinance.app.presentation.ui.home.homesummary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.BundleCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.fitfinance.app.R
import com.fitfinance.app.databinding.FragmentHomeSummaryBinding
import com.fitfinance.app.domain.response.HomeSummaryResponse
import com.fitfinance.app.presentation.ui.home.HomeFragmentDirections

class HomeSummaryFragment : Fragment() {

    private lateinit var _binding: FragmentHomeSummaryBinding

    private lateinit var homeSummaryResponse: HomeSummaryResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            homeSummaryResponse = BundleCompat.getParcelable(it, HOME_SUMMARY_RESPONSE_EXTRA, HomeSummaryResponse::class.java)!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeSummaryBinding.inflate(inflater, container, false)
        val root: View = _binding.root

        setupUi(homeSummaryResponse)
        return root
    }

    private fun setupUi(homeSummaryResponse: HomeSummaryResponse) {
        _binding.apply {
            tvOverviewBiggestExpenseValue.text = resources.getString(R.string.txt_generic_decimal, homeSummaryResponse.biggestExpense.value)
            tvOverviewSmallestExpenseValue.text = resources.getString(R.string.txt_generic_decimal, homeSummaryResponse.smallestExpense.value)
            tvOverviewBiggestInvestmentValue.text = resources.getString(R.string.txt_generic_decimal, homeSummaryResponse.biggestInvestment.price)
            tvOverviewSmallestInvestmentValue.text = resources.getString(R.string.txt_generic_decimal, homeSummaryResponse.smallestInvestment.price)

            tvOverviewBiggestExpenseValue.setOnClickListener {
                navigateToFinances(homeSummaryResponse.biggestExpense.name)
            }
            tvOverviewSmallestExpenseValue.setOnClickListener {
                navigateToFinances(homeSummaryResponse.smallestExpense.name)
            }
            tvOverviewBiggestInvestmentValue.setOnClickListener {
                navigateToInvestments(homeSummaryResponse.biggestInvestment.name)
            }
            tvOverviewSmallestInvestmentValue.setOnClickListener {
                navigateToInvestments(homeSummaryResponse.smallestInvestment.name)
            }
        }
    }

    private fun navigateToFinances(itemId: String) {
        val actionNavigation = HomeFragmentDirections.actionNavigationHomeToFinances(itemId)
        findNavController().navigate(actionNavigation)
    }

    private fun navigateToInvestments(itemId: String) {
        val actionNavigation = HomeFragmentDirections.actionNavigationHomeToInvestments(itemId)
        findNavController().navigate(actionNavigation)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding.root.removeAllViews()
    }

    companion object {
        const val HOME_SUMMARY_RESPONSE_EXTRA = "homeSummaryResponse"
        fun newInstance(homeSummaryResponse: HomeSummaryResponse) = HomeSummaryFragment().apply {
            arguments = Bundle().apply {
                putParcelable(HOME_SUMMARY_RESPONSE_EXTRA, homeSummaryResponse)
            }
        }
    }
}