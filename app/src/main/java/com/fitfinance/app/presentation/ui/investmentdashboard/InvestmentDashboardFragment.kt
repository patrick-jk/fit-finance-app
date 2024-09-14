package com.fitfinance.app.presentation.ui.investmentdashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fitfinance.app.databinding.FragmentFinanceDashboardBinding
import com.fitfinance.app.databinding.FragmentFinanceListBinding
import com.fitfinance.app.databinding.FragmentInvestmentDashboardBinding

class InvestmentDashboardFragment : Fragment() {

    private var _binding: FragmentInvestmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInvestmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}