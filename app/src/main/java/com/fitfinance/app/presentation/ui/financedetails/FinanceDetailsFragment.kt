package com.fitfinance.app.presentation.ui.financedetails

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.DatePicker
import androidx.appcompat.app.AlertDialog
import androidx.core.os.BundleCompat
import com.fitfinance.app.R
import com.fitfinance.app.databinding.FragmentFinanceDetailsBinding
import com.fitfinance.app.domain.model.FinanceType
import com.fitfinance.app.domain.request.FinancePostRequest
import com.fitfinance.app.domain.request.FinancePutRequest
import com.fitfinance.app.domain.response.FinanceGetResponse
import com.fitfinance.app.presentation.statepattern.State
import com.fitfinance.app.util.CurrencyTextWatcher
import com.fitfinance.app.util.DatePickerFragment
import com.fitfinance.app.util.SHARED_PREF_NAME
import com.fitfinance.app.util.ValidateInput
import com.fitfinance.app.util.createDialog
import com.fitfinance.app.util.getUserFriendlyErrorMessage
import com.fitfinance.app.util.getProgressDialog
import com.fitfinance.app.util.hideSoftKeyboard
import com.fitfinance.app.util.removeCurrencyFormatting
import com.fitfinance.app.util.text
import com.fitfinance.app.util.toLocalDateApiFormat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale

class FinanceDetailsFragment : BottomSheetDialogFragment(), DatePickerDialog.OnDateSetListener {
    private val brazilianDateFormat = "dd/MM/yyyy"

    private var _binding: FragmentFinanceDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var finance: FinanceGetResponse
    private lateinit var lastDatePickerTag: String
    private val dateTimeFormatterBrFormat: DateTimeFormatter = DateTimeFormatter.ofPattern(brazilianDateFormat)
    private val dateTimeFormatterApiFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    private val viewModel by viewModel<FinanceDetailsViewModel>()

    private val sharedPreferences by lazy { requireContext().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE) }
    private var progressDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            finance = BundleCompat.getParcelable(it, "finance", FinanceGetResponse::class.java)!!
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

        arguments?.let {
            binding.tilFinanceName.text = finance.name
            binding.tilFinanceValue.text = String.format(Locale.US, "%.2f", finance.value)
            binding.tilFinanceDescription.text = finance.description
            binding.tilFinanceStartDate.text = DateTimeFormatter.ofPattern(brazilianDateFormat).format(dateTimeFormatterApiFormat.parse(finance.startDate))
            binding.tilFinanceEndDate.text = DateTimeFormatter.ofPattern(brazilianDateFormat).format(dateTimeFormatterApiFormat.parse(finance.endDate))
            binding.actFinanceType.setText(convertFinanceTypeToUi(finance.type.name), false)
        }

        return root
    }

    private fun setupUi() {
        _binding?.tilFinanceStartDate?.editText?.setOnClickListener {
            it.hideSoftKeyboard()
            try {
                val date = LocalDate.parse(binding.tilFinanceStartDate.text, dateTimeFormatterBrFormat)
                showDatePickerDialog("StartDate", date)
            } catch (e: DateTimeParseException) {
                showDatePickerDialog("StartDate")
            }
        }
        _binding?.tilFinanceEndDate?.editText?.setOnClickListener {
            it.hideSoftKeyboard()
            try {
                val date = LocalDate.parse(binding.tilFinanceEndDate.text, dateTimeFormatterBrFormat)
                showDatePickerDialog("EndDate", date)
            } catch (e: DateTimeParseException) {
                showDatePickerDialog("EndDate")
            }
        }

        binding.tilFinanceValue.editText?.addTextChangedListener(CurrencyTextWatcher(binding.tilFinanceValue))

        if (arguments == null) {
            binding.btnSaveFinance.setOnClickListener {
                if (!validateFields()) {
                    return@setOnClickListener
                }
                viewModel.createFinance(
                    FinancePostRequest(
                        name = binding.tilFinanceName.text,
                        value = removeCurrencyFormatting(binding.tilFinanceValue.text).toDouble(),
                        description = binding.tilFinanceDescription.text,
                        startDate = binding.tilFinanceStartDate.text.toLocalDateApiFormat(),
                        endDate = binding.tilFinanceEndDate.text.toLocalDateApiFormat(),
                        type = convertFinanceTypeToData(binding.actFinanceType.text.toString()),
                    ), sharedPreferences.getString(getString(R.string.pref_user_token), "")!!
                )
            }
        } else {
            binding.btnSaveFinance.setOnClickListener {
                if (!validateFields()) {
                    return@setOnClickListener
                }
                viewModel.updateFinance(
                    FinancePutRequest(
                        id = finance.id,
                        name = binding.tilFinanceName.text,
                        value = removeCurrencyFormatting(binding.tilFinanceValue.text).toDouble(),
                        description = binding.tilFinanceDescription.text,
                        startDate = binding.tilFinanceStartDate.text.toLocalDateApiFormat(),
                        endDate = binding.tilFinanceEndDate.text.toLocalDateApiFormat(),
                        type = convertFinanceTypeToData(binding.actFinanceType.text.toString()),
                    ), sharedPreferences.getString(getString(R.string.pref_user_token), "")!!
                )
            }
        }
    }

    private fun validateFields(): Boolean {
        val inputValidator = ValidateInput(requireContext())
        val isNameValid = inputValidator.validateInputText(binding.tilFinanceName)
        val isValueValid = inputValidator.validateInputText(binding.tilFinanceValue)
        val isDescriptionValid = inputValidator.validateInputText(binding.tilFinanceDescription)
        val isStartDateValid = inputValidator.validateInputText(binding.tilFinanceStartDate)
        val isEndDateValid = inputValidator.validateInputText(binding.tilFinanceEndDate)
        val isTypeValid = inputValidator.validateInputText(binding.tilFinanceType)

        return isNameValid && isValueValid && isDescriptionValid && isStartDateValid && isEndDateValid && isTypeValid
    }

    private fun convertFinanceTypeToUi(financeType: String): String {
        return when (financeType) {
            "INCOME" -> resources.getStringArray(R.array.finance_types)[0]
            "EXPENSE" -> resources.getStringArray(R.array.finance_types)[1]
            else -> "Income"
        }
    }

    private fun convertFinanceTypeToData(financeType: String): FinanceType {
        return when (financeType) {
            resources.getStringArray(R.array.finance_types)[0] -> FinanceType.INCOME
            resources.getStringArray(R.array.finance_types)[1] -> FinanceType.EXPENSE
            else -> FinanceType.INCOME
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.financePostResponse.observe(viewLifecycleOwner) {
            when (it) {
                is State.Loading -> {
                    progressDialog = requireContext().getProgressDialog(it.loadingMessage)
                    progressDialog?.show()
                }

                is State.Success -> {
                    progressDialog?.dismiss()
                    requireActivity().supportFragmentManager.setFragmentResult("updateFinanceList", Bundle())
                    dismiss()
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

        viewModel.financePutLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is State.Loading -> {
                    progressDialog = requireContext().getProgressDialog(it.loadingMessage)
                    progressDialog?.show()
                }

                is State.Success -> {
                    progressDialog?.dismiss()
                    Log.d("FinanceDetailsFragment", "Finance successfully updated or created")
                    requireActivity().supportFragmentManager.setFragmentResult("updateFinanceList", Bundle())
                    dismiss()
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

        fun newInstance() = FinanceDetailsFragment()
    }
}