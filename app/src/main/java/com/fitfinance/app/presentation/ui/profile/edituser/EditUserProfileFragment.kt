package com.fitfinance.app.presentation.ui.profile.edituser

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.appcompat.app.AlertDialog
import androidx.core.os.BundleCompat
import androidx.fragment.app.viewModels
import com.fitfinance.app.R
import com.fitfinance.app.databinding.FragmentEditUserProfileBinding
import com.fitfinance.app.domain.request.UserPutRequest
import com.fitfinance.app.domain.response.UserGetResponse
import com.fitfinance.app.presentation.statepattern.State
import com.fitfinance.app.util.ClearErrorTextWatcher
import com.fitfinance.app.util.CpfTextWatcher
import com.fitfinance.app.util.CurrencyTextWatcher
import com.fitfinance.app.util.DatePickerFragment
import com.fitfinance.app.util.PhoneTextWatcher
import com.fitfinance.app.util.SHARED_PREF_NAME
import com.fitfinance.app.util.ValidateInput
import com.fitfinance.app.util.createDialog
import com.fitfinance.app.util.formatToCpf
import com.fitfinance.app.util.formatToCurrency
import com.fitfinance.app.util.formatToPhone
import com.fitfinance.app.util.hideSoftKeyboard
import com.fitfinance.app.util.removeCpfFormatting
import com.fitfinance.app.util.removeCurrencyFormatting
import com.fitfinance.app.util.removePhoneFormatting
import com.fitfinance.app.util.text
import com.fitfinance.app.util.toLocalDateApiFormat
import com.fitfinance.app.util.toLocalDateBrFormat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate

@AndroidEntryPoint
class EditUserProfileFragment : BottomSheetDialogFragment(), DatePickerDialog.OnDateSetListener {
    private lateinit var _binding: FragmentEditUserProfileBinding

    private val viewModel: EditUserProfileViewModel by viewModels()

    private val user by lazy {
        arguments?.let {
            BundleCompat.getParcelable(it, EDIT_USER_EXTRA, UserGetResponse::class.java)
        }
    }
    private val sharedPreferences by lazy {
        requireContext().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
    }
    private var progressDialog: AlertDialog? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentEditUserProfileBinding.inflate(inflater, container, false)
        val root = _binding.root

        setupUi()
        return root
    }

    private fun setupUi() {
        _binding.apply {
            user?.let {
                tilName.text = it.name
                tilCpf.text = formatToCpf(it.cpf)
                tilEmail.text = it.email
                tilPhone.text = formatToPhone(it.phone)
                tilBirthdate.text = it.birthdate.toLocalDateBrFormat()
                tilIncome.text = formatToCurrency(it.income.toString())
            }

            tilBirthdate.editText?.setOnClickListener {
                it.hideSoftKeyboard()
                showDatePickerDialog("Birthdate")
            }

            tilName.editText?.addTextChangedListener(ClearErrorTextWatcher(tilName))
            tilCpf.editText?.addTextChangedListener(CpfTextWatcher(tilCpf))
            tilEmail.editText?.addTextChangedListener(ClearErrorTextWatcher(tilEmail))
            tilPhone.editText?.addTextChangedListener(PhoneTextWatcher(tilPhone))
            tilBirthdate.editText?.addTextChangedListener(ClearErrorTextWatcher(tilBirthdate))
            tilIncome.editText?.addTextChangedListener(CurrencyTextWatcher(tilIncome))

            btnUpdateUser.setOnClickListener {
                if (!validateFields()) {
                    return@setOnClickListener
                }

                val formattedBirthdate = tilBirthdate.text.toLocalDateApiFormat()

                val rawCpf = removeCpfFormatting(tilCpf.text)
                val rawPhone = removePhoneFormatting(tilPhone.text)
                val rawIncome = removeCurrencyFormatting(tilIncome.text)

                viewModel.updateUser(
                    sharedPreferences.getString(resources.getString(R.string.pref_user_token), "")!!,
                    UserPutRequest(
                        id = user!!.id,
                        name = tilName.text,
                        cpf = rawCpf,
                        email = tilEmail.text,
                        phone = rawPhone,
                        birthdate = formattedBirthdate,
                        income = rawIncome.toDouble()
                    )
                )
            }
        }
    }

    private fun validateFields(): Boolean {
        val inputValidator = ValidateInput(requireContext())
        val isNameValid = inputValidator.validateInputText(_binding.tilName)
        val isCpfValid = inputValidator.validateInputText(_binding.tilCpf) && inputValidator.validateCpf(_binding.tilCpf)
        val isEmailValid = inputValidator.validateInputText(_binding.tilEmail) && inputValidator.validateEmail(_binding.tilEmail)
        val isPhoneValid = inputValidator.validateInputText(_binding.tilPhone) && inputValidator.validatePhone(_binding.tilPhone)
        val isBirthdateValid = inputValidator.validateInputText(_binding.tilBirthdate)
        val isIncomeValid = inputValidator.validateInputText(_binding.tilIncome)

        return isNameValid && isCpfValid && isEmailValid && isPhoneValid && isBirthdateValid && isIncomeValid
    }

    private fun showDatePickerDialog(tag: String, actualDate: LocalDate = LocalDate.now()) {
        Log.i("AddFinanceActivity", "showDatePickerDialog: $actualDate")
        val datePicker = DatePickerFragment.newInstance(this, actualDate)
        datePicker.show(parentFragmentManager, "datePicker$tag")
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val monthString = if (month < 9) "0${month + 1}" else "${month + 1}"
        val dayString = if (dayOfMonth < 10) "0$dayOfMonth" else "$dayOfMonth"
        val selectedDate = "$dayString/$monthString/$year"
        _binding.tilBirthdate.text = selectedDate
    }

    override fun onStart() {
        super.onStart()
        viewModel.editUserProfileState.observe(viewLifecycleOwner) {
            when (it) {
                is State.Loading -> {
                    progressDialog = requireContext().createDialog {
                        setMessage(resources.getString(R.string.txt_loading_home_data))
                        setCancelable(false)
                    }
                    progressDialog?.show()
                }

                is State.Success -> {
                    progressDialog?.dismiss()
                    requireContext().createDialog {
                        setTitle(resources.getString(R.string.txt_success))
                        setMessage(resources.getString(R.string.txt_user_updated))
                        setPositiveButton(resources.getString(android.R.string.ok)) { _, _ ->
                            requireActivity().supportFragmentManager.setFragmentResult("updateUserInfo", Bundle())
                            dismiss()
                        }
                    }.show()
                }

                is State.Error -> {
                    progressDialog?.dismiss()
                    requireContext().createDialog {
                        setTitle(resources.getString(R.string.txt_error))
                        setMessage(it.error.message)
                        setPositiveButton(resources.getString(android.R.string.ok), null)
                    }.show()
                }
            }
        }
    }

    companion object {
        const val EDIT_USER_EXTRA = "edit_user_extra"

        fun newInstance(userGetResponse: UserGetResponse) = EditUserProfileFragment().apply {
            arguments = Bundle().apply {
                putParcelable(EDIT_USER_EXTRA, userGetResponse)
            }
        }
    }
}