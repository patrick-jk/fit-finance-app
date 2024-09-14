package com.fitfinance.app.presentation.ui.financelist

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.fitfinance.app.R
import com.fitfinance.app.databinding.FragmentFinanceListBinding
import com.fitfinance.app.databinding.FragmentHomeBinding
import com.fitfinance.app.databinding.FragmentInvestmentListBinding
import com.fitfinance.app.presentation.ui.home.HomeViewModel

class FinanceListFragment : Fragment() {

    private var _binding: FragmentFinanceListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFinanceListBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}