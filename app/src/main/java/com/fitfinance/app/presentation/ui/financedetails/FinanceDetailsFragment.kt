package com.fitfinance.app.presentation.ui.financedetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.fitfinance.app.R
import com.fitfinance.app.databinding.FragmentFinanceDetailsBinding
import com.fitfinance.app.domain.response.FinanceGetResponse
import com.fitfinance.app.presentation.ui.home.HomeViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FinanceDetailsFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentFinanceDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var finance: FinanceGetResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            finance = it.getParcelable("finance")!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFinanceDetailsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //TODO: Implement Edit Finance functionality
        // Use the finance object to populate the UI
//        binding.tvFinanceName.text = finance.name
//        binding.tvFinanceValue.text = finance.value.toString()
//        binding.tvFinanceDescription.text = finance.description
//        binding.tvFinanceStartDate.text = finance.startDate
//        binding.tvFinanceEndDate.text = finance.endDate

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(finance: FinanceGetResponse) = FinanceDetailsFragment().apply {
            arguments = Bundle().apply {
                putParcelable("finance", finance)
            }
        }
    }
}