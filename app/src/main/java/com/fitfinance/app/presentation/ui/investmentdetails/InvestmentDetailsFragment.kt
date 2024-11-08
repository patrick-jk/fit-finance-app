package com.fitfinance.app.presentation.ui.investmentdetails

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.DatePicker
import androidx.appcompat.app.AlertDialog
import androidx.core.os.BundleCompat
import com.fitfinance.app.R
import com.fitfinance.app.databinding.FragmentInvestmentDetailsBinding
import com.fitfinance.app.domain.model.InvestmentType
import com.fitfinance.app.domain.request.InvestmentPostRequest
import com.fitfinance.app.domain.request.InvestmentPutRequest
import com.fitfinance.app.domain.response.InvestmentGetResponse
import com.fitfinance.app.presentation.statepattern.State
import com.fitfinance.app.util.CurrencyTextWatcher
import com.fitfinance.app.util.DatePickerFragment
import com.fitfinance.app.util.SHARED_PREF_NAME
import com.fitfinance.app.util.ValidateInput
import com.fitfinance.app.util.createDialog
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

class InvestmentDetailsFragment : BottomSheetDialogFragment(), DatePickerDialog.OnDateSetListener {
    private val brazilianDateFormat = "dd/MM/yyyy"

    private var _binding: FragmentInvestmentDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<InvestmentDetailsViewModel>()

    private lateinit var investment: InvestmentGetResponse
    private lateinit var lastDatePickerTag: String
    private val dateTimeFormatterBrFormat: DateTimeFormatter = DateTimeFormatter.ofPattern(brazilianDateFormat)
    private val dateTimeFormatterApiFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    private val sharedPreferences by lazy { requireContext().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE) }
    private var progressDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            investment = BundleCompat.getParcelable(it, "investment", InvestmentGetResponse::class.java)!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInvestmentDetailsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupDropDown()
        setupUi()

        arguments?.let {
            binding.tilInvestmentName.text = investment.name
            binding.tilInvestmentCost.text = String.format(Locale.US, "%.2f", investment.price)
            binding.tilInvestmentStartDate.text = DateTimeFormatter.ofPattern(brazilianDateFormat).format(dateTimeFormatterApiFormat.parse(investment.startDate))
            if (investment.endDate != null) binding.tilInvestmentEndDate.text =
                DateTimeFormatter.ofPattern(brazilianDateFormat).format(dateTimeFormatterApiFormat.parse(investment.endDate))
            binding.actInvestmentType.setText(convertInvestmentTypeToUi(investment.type.name), false)
        }

        return root
    }

    private fun setupUi() {
//        mockInputs()
        _binding?.tilInvestmentStartDate?.editText?.setOnClickListener {
            it.hideSoftKeyboard()
            try {
                val date = LocalDate.parse(binding.tilInvestmentStartDate.text, dateTimeFormatterBrFormat)
                showDatePickerDialog("StartDate", date)
            } catch (e: DateTimeParseException) {
                showDatePickerDialog("StartDate")
            }
        }
        _binding?.tilInvestmentEndDate?.editText?.setOnClickListener {
            it.hideSoftKeyboard()
            try {
                val date = LocalDate.parse(binding.tilInvestmentEndDate.text, dateTimeFormatterBrFormat)
                showDatePickerDialog("EndDate", date)
            } catch (e: DateTimeParseException) {
                showDatePickerDialog("EndDate")
            }
        }

        binding.tilInvestmentCost.editText?.addTextChangedListener(CurrencyTextWatcher(binding.tilInvestmentCost))

        binding.btnSaveInvestment.setOnClickListener {
            if (!validateFields()) {
                return@setOnClickListener
            }
            val investmentRequest: Any = if (arguments == null) {
                InvestmentPostRequest(
                    name = binding.tilInvestmentName.text,
                    price = removeCurrencyFormatting(binding.tilInvestmentCost.text).toDouble(),
                    startDate = binding.tilInvestmentStartDate.text.toLocalDateApiFormat(),
                    endDate = binding.tilInvestmentEndDate.text.toLocalDateApiFormat(),
                    quantity = binding.tilInvestmentQuantity.text.toInt(),
                    type = convertInvestmentTypeToData(binding.actInvestmentType.text.toString())
                )
            } else {
                InvestmentPutRequest(
                    id = investment.id,
                    name = binding.tilInvestmentName.text,
                    price = removeCurrencyFormatting(binding.tilInvestmentCost.text).toDouble(),
                    startDate = binding.tilInvestmentStartDate.text.toLocalDateApiFormat(),
                    endDate = binding.tilInvestmentEndDate.text.toLocalDateApiFormat(),
                    quantity = binding.tilInvestmentQuantity.text.toInt(),
                    type = convertInvestmentTypeToData(binding.actInvestmentType.text.toString()),
                )
            }
            createOrUpdateInvestment(investmentRequest, sharedPreferences.getString(getString(R.string.pref_user_token), "")!!)
        }
    }

    private fun <T> createOrUpdateInvestment(request: T, token: String) {
        if (request is InvestmentPostRequest) {
            viewModel.createInvestment(request, token)
        } else if (request is InvestmentPutRequest) {
            viewModel.updateInvestment(request, token)
        }
    }

    private fun validateFields(): Boolean {
        val isNameValid = ValidateInput.validateInputText(binding.tilInvestmentName)
        val isValueValid = ValidateInput.validateInputText(binding.tilInvestmentCost)
        val isStartDateValid = ValidateInput.validateInputText(binding.tilInvestmentStartDate)
        val isTypeValid = ValidateInput.validateInputText(binding.tilInvestmentType)

        return isNameValid && isValueValid && isStartDateValid && isTypeValid
    }

    private fun mockInputs() {
        binding.tilInvestmentName.text = "Teste"
        binding.tilInvestmentCost.text = "10"
        binding.tilInvestmentStartDate.text = "01/01/2021"
        binding.tilInvestmentQuantity.text = "30"
    }

    private fun convertInvestmentTypeToUi(investmentType: String): String {
        return when (investmentType) {
            "STOCK" -> resources.getStringArray(R.array.investment_types)[0]
            "FIIS" -> resources.getStringArray(R.array.investment_types)[1]
            "FIXED_INCOME" -> resources.getStringArray(R.array.investment_types)[2]
            else -> resources.getStringArray(R.array.investment_types)[0]
        }
    }

    private fun convertInvestmentTypeToData(investmentType: String): InvestmentType {
        return when (investmentType) {
            resources.getStringArray(R.array.investment_types)[0] -> InvestmentType.STOCK
            resources.getStringArray(R.array.investment_types)[1] -> InvestmentType.FII
            resources.getStringArray(R.array.investment_types)[2] -> InvestmentType.FIXED_INCOME
            else -> InvestmentType.STOCK
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.investmentPostResponse.observe(this) {
            when (it) {
                is State.Loading -> {
                    progressDialog = requireContext().getProgressDialog(resources.getString(R.string.creating_investment))
                    progressDialog?.show()
                }

                is State.Success -> {
                    progressDialog?.dismiss()
                    requireActivity().supportFragmentManager.setFragmentResult("updateInvestmentList", Bundle())
                    dismiss()
                }

                is State.Error -> {
                    progressDialog?.dismiss()
                    requireContext().createDialog {
                        setMessage(it.error.message.toString())
                        setPositiveButton(android.R.string.ok, null)
                    }.show()
                }
            }
        }
        viewModel.investmentPutResponse.observe(this) {
            when (it) {
                is State.Loading -> {
                    progressDialog = requireContext().getProgressDialog(resources.getString(R.string.updating_investment))
                    progressDialog?.show()
                }

                is State.Success -> {
                    progressDialog?.dismiss()
                    requireActivity().supportFragmentManager.setFragmentResult("updateInvestmentList", Bundle())
                    dismiss()
                }

                is State.Error -> {
                    progressDialog?.dismiss()
                    requireContext().createDialog {
                        setMessage(it.error.message.toString())
                        setPositiveButton(android.R.string.ok, null)
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
        val financeTypes = resources.getStringArray(R.array.investment_types)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, financeTypes)
        _binding?.actInvestmentType?.setAdapter(arrayAdapter)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val monthString = if (month < 9) "0${month + 1}" else "${month + 1}"
        val dayString = if (dayOfMonth < 10) "0$dayOfMonth" else "$dayOfMonth"
        val selectedDate = "$dayString/$monthString/$year"
        if (lastDatePickerTag == "StartDate") binding.tilInvestmentStartDate.text = selectedDate
        else binding.tilInvestmentEndDate.text = selectedDate
    }

    private fun showDatePickerDialog(tag: String, actualDate: LocalDate = LocalDate.now()) {
        val datePicker = DatePickerFragment.newInstance(this, actualDate)
        datePicker.show(childFragmentManager, "datePicker$tag")
        lastDatePickerTag = tag
    }

    companion object {
        fun newInstance(investment: InvestmentGetResponse) = InvestmentDetailsFragment().apply {
            arguments = Bundle().apply {
                putParcelable("investment", investment)
            }
        }

        fun newInstance() = InvestmentDetailsFragment()
    }
}