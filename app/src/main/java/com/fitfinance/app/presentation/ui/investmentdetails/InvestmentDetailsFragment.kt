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
import androidx.fragment.app.viewModels
import com.fitfinance.app.R
import com.fitfinance.app.databinding.FragmentInvestmentDetailsBinding
import com.fitfinance.app.domain.model.InvestmentType
import com.fitfinance.app.domain.request.InvestmentPostRequest
import com.fitfinance.app.domain.request.InvestmentPutRequest
import com.fitfinance.app.domain.response.InvestmentGetResponse
import com.fitfinance.app.presentation.statepattern.State
import com.fitfinance.app.util.ClearErrorTextWatcher
import com.fitfinance.app.util.CurrencyTextWatcher
import com.fitfinance.app.util.DatePickerFragment
import com.fitfinance.app.util.SHARED_PREF_NAME
import com.fitfinance.app.util.ValidateInput
import com.fitfinance.app.util.createDialog
import com.fitfinance.app.util.getProgressDialog
import com.fitfinance.app.util.getUserFriendlyErrorMessage
import com.fitfinance.app.util.hideSoftKeyboard
import com.fitfinance.app.util.removeCurrencyFormatting
import com.fitfinance.app.util.text
import com.fitfinance.app.util.toLocalDateApiFormat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale

@AndroidEntryPoint
class InvestmentDetailsFragment : BottomSheetDialogFragment(), DatePickerDialog.OnDateSetListener {
    private val brazilianDateFormat = "dd/MM/yyyy"

    private lateinit var _binding: FragmentInvestmentDetailsBinding

    private val viewModel: InvestmentDetailsViewModel by viewModels()

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
        val root: View = _binding.root

        setupDropDown()
        setupUi()

        arguments?.let {
            _binding.apply {
                tilInvestmentName.text = investment.name
                tilInvestmentCost.text = String.format(Locale.US, "%.2f", investment.price)
                tilInvestmentQuantity.text = investment.quantity.toString()
                tilInvestmentStartDate.text = DateTimeFormatter.ofPattern(brazilianDateFormat).format(dateTimeFormatterApiFormat.parse(investment.startDate))
                if (investment.endDate != null) tilInvestmentEndDate.text =
                    DateTimeFormatter.ofPattern(brazilianDateFormat).format(dateTimeFormatterApiFormat.parse(investment.endDate))
                actInvestmentType.setText(convertInvestmentTypeToUi(investment.type.name), false)
            }
        }

        return root
    }

    private fun setupUi() {
        _binding.apply {
            tilInvestmentStartDate.editText?.setOnClickListener {
                it.hideSoftKeyboard()
                try {
                    val date = LocalDate.parse(tilInvestmentStartDate.text, dateTimeFormatterBrFormat)
                    showDatePickerDialog("StartDate", date)
                } catch (e: DateTimeParseException) {
                    showDatePickerDialog("StartDate")
                }
            }
            tilInvestmentEndDate.editText?.setOnClickListener {
                it.hideSoftKeyboard()
                try {
                    val date = LocalDate.parse(tilInvestmentEndDate.text, dateTimeFormatterBrFormat)
                    showDatePickerDialog("EndDate", date)
                } catch (e: DateTimeParseException) {
                    showDatePickerDialog("EndDate")
                }
            }

            tilInvestmentName.editText?.addTextChangedListener(ClearErrorTextWatcher(tilInvestmentName))
            tilInvestmentQuantity.editText?.addTextChangedListener(ClearErrorTextWatcher(tilInvestmentQuantity))
            tilInvestmentCost.editText?.addTextChangedListener(CurrencyTextWatcher(tilInvestmentCost))
            tilInvestmentType.editText?.addTextChangedListener(ClearErrorTextWatcher(tilInvestmentType))
            tilInvestmentStartDate.editText?.addTextChangedListener(ClearErrorTextWatcher(tilInvestmentStartDate))
            tilInvestmentEndDate.editText?.addTextChangedListener(ClearErrorTextWatcher(tilInvestmentEndDate))

            btnSaveInvestment.setOnClickListener {
                if (!validateFields()) {
                    return@setOnClickListener
                }
                val investmentRequest: Any = if (arguments == null) {
                    InvestmentPostRequest(
                        name = tilInvestmentName.text,
                        price = removeCurrencyFormatting(tilInvestmentCost.text).toDouble(),
                        startDate = tilInvestmentStartDate.text.toLocalDateApiFormat(),
                        endDate = tilInvestmentEndDate.text.toLocalDateApiFormat(),
                        quantity = tilInvestmentQuantity.text.toInt(),
                        type = convertInvestmentTypeToData(actInvestmentType.text.toString())
                    )
                } else {
                    InvestmentPutRequest(
                        id = investment.id,
                        name = tilInvestmentName.text,
                        price = removeCurrencyFormatting(tilInvestmentCost.text).toDouble(),
                        startDate = tilInvestmentStartDate.text.toLocalDateApiFormat(),
                        endDate = tilInvestmentEndDate.text.toLocalDateApiFormat(),
                        quantity = tilInvestmentQuantity.text.toInt(),
                        type = convertInvestmentTypeToData(actInvestmentType.text.toString()),
                    )
                }
                createOrUpdateInvestment(investmentRequest, sharedPreferences.getString(getString(R.string.pref_user_token), "")!!)
            }
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
        val inputValidator = ValidateInput(requireContext())
        val isNameValid = inputValidator.validateInputText(_binding.tilInvestmentName)
        val isValueValid = inputValidator.validateInputText(_binding.tilInvestmentCost)
        val isQuantityValid = inputValidator.validateInputText(_binding.tilInvestmentQuantity)
        val isStartDateValid = inputValidator.validateInputText(_binding.tilInvestmentStartDate)
        val isTypeValid = inputValidator.validateInputText(_binding.tilInvestmentType)

        return isNameValid && isValueValid && isQuantityValid && isStartDateValid && isTypeValid
    }

    private fun convertInvestmentTypeToUi(investmentType: String): String {
        return when (investmentType) {
            "STOCK" -> resources.getStringArray(R.array.investment_types)[0]
            "FII" -> resources.getStringArray(R.array.investment_types)[1]
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
                        setTitle(resources.getString(R.string.txt_error))
                        setMessage(requireContext().getUserFriendlyErrorMessage(it.error))
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
                        setTitle(resources.getString(R.string.txt_error))
                        setMessage(requireContext().getUserFriendlyErrorMessage(it.error))
                        setPositiveButton(android.R.string.ok, null)
                    }.show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding.root.removeAllViews()
    }

    private fun setupDropDown() {
        val financeTypes = resources.getStringArray(R.array.investment_types)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, financeTypes)
        _binding.actInvestmentType.setAdapter(arrayAdapter)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val monthString = if (month < 9) "0${month + 1}" else "${month + 1}"
        val dayString = if (dayOfMonth < 10) "0$dayOfMonth" else "$dayOfMonth"
        val selectedDate = "$dayString/$monthString/$year"
        if (lastDatePickerTag == "StartDate") _binding.tilInvestmentStartDate.text = selectedDate
        else _binding.tilInvestmentEndDate.text = selectedDate
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