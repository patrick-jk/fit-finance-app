package com.fitfinance.app.presentation.ui.financedetails

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.fitfinance.app.R
import com.fitfinance.app.databinding.FragmentFinanceDetailsBinding
import com.fitfinance.app.domain.response.FinanceGetResponse
import com.fitfinance.app.presentation.ui.home.HomeViewModel
import com.fitfinance.app.util.DatePickerFragment
import com.fitfinance.app.util.text
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class FinanceDetailsFragment : BottomSheetDialogFragment(), DatePickerDialog.OnDateSetListener {
    private var _binding: FragmentFinanceDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var finance: FinanceGetResponse
    private lateinit var lastDatePickerTag: String
    private val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    //TODO Implement viewmodel, validate inputs, convert date format and put type uppercase in the request

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

        setupDropDown()
        setupUi()

        //TODO: Implement Edit Finance functionality
        // Use the finance object to populate the UI
        binding.tilFinanceName.text = finance.name
        binding.tilFinanceValue.text = finance.value.toString()
        binding.tilFinanceDescription.text = finance.description
        binding.tilFinanceStartDate.text = finance.startDate
        binding.tilFinanceEndDate.text = finance.endDate
        binding.actFinanceType.setText(setFinanceType(finance.type.name), false)

        return root
    }

    private fun setupUi() {
        _binding?.tilFinanceStartDate?.editText?.setOnClickListener {
            try {
                val date = LocalDate.parse(binding.tilFinanceStartDate.text, dateFormatter)
                showDatePickerDialog("StartDate", date)
            } catch (e: DateTimeParseException) {
                showDatePickerDialog("StartDate")
            }
        }
        _binding?.tilFinanceEndDate?.editText?.setOnClickListener {
            try {
                val date = LocalDate.parse(binding.tilFinanceEndDate.text, dateFormatter)
                showDatePickerDialog("EndDate", date)
            } catch (e: DateTimeParseException) {
                showDatePickerDialog("EndDate")
            }
        }
    }

    private fun setFinanceType(financeType: String): String {
        return when (financeType) {
            "INCOME" -> resources.getStringArray(R.array.finance_types)[0]
            "EXPENSE" -> resources.getStringArray(R.array.finance_types)[1]
            else -> "Income"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupDropDown() {
        val financeTypes = resources.getStringArray(R.array.finance_types)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, financeTypes)
        _binding?.actFinanceType?.setAdapter(arrayAdapter)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val monthString = if (month < 9) "0${month + 1}" else "${month + 1}"
        val dayString = if (dayOfMonth < 10) "0$dayOfMonth" else "$dayOfMonth"
        val selectedDate = "$dayString/$monthString/$year"
        Log.i("FinanceDetailsFragment", "onDateSet: $lastDatePickerTag")
        if (lastDatePickerTag == "StartDate") binding.tilFinanceStartDate.text = selectedDate
        else binding.tilFinanceEndDate.text = selectedDate
    }

    private fun showDatePickerDialog(tag: String, actualDate: LocalDate = LocalDate.now()) {
        val datePicker = DatePickerFragment.newInstance(this, actualDate)
        datePicker.show(childFragmentManager, "datePicker$tag")
        lastDatePickerTag = tag
    }

    companion object {
        fun newInstance(finance: FinanceGetResponse) = FinanceDetailsFragment().apply {
            arguments = Bundle().apply {
                putParcelable("finance", finance)
            }
        }
    }
}