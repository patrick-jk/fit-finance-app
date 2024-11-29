package com.fitfinance.app.presentation.ui.register

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.fitfinance.app.R
import com.fitfinance.app.databinding.ActivityRegisterBinding
import com.fitfinance.app.domain.request.RegisterRequest
import com.fitfinance.app.presentation.statepattern.State
import com.fitfinance.app.presentation.ui.login.LoginActivity
import com.fitfinance.app.util.CpfTextWatcher
import com.fitfinance.app.util.CurrencyTextWatcher
import com.fitfinance.app.util.DatePickerFragment
import com.fitfinance.app.util.PhoneTextWatcher
import com.fitfinance.app.util.ValidateInput
import com.fitfinance.app.util.createDialog
import com.fitfinance.app.util.getUserFriendlyErrorMessage
import com.fitfinance.app.util.getProgressDialog
import com.fitfinance.app.util.hideSoftKeyboard
import com.fitfinance.app.util.removeCpfFormatting
import com.fitfinance.app.util.removeCurrencyFormatting
import com.fitfinance.app.util.removePhoneFormatting
import com.fitfinance.app.util.text
import com.fitfinance.app.util.toLocalDateApiFormat
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalDate

class RegisterActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {
    private val binding by lazy { ActivityRegisterBinding.inflate(LayoutInflater.from(this)) }
    private val viewModel by viewModel<RegisterViewModel>()
    private var progressDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        setupUi()
//        mockRegister()
    }

    private fun setupUi() {
        binding.apply {
            btnGoToLogin.setOnClickListener {
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            }

            tilRegisterBirthdate.editText?.setOnClickListener {
                it.hideSoftKeyboard()
                showDatePickerDialog("Birthdate")
            }

            tilRegisterCpf.editText?.addTextChangedListener(CpfTextWatcher(tilRegisterCpf))
            tilRegisterPhone.editText?.addTextChangedListener(PhoneTextWatcher(tilRegisterPhone))
            tilRegisterIncome.editText?.addTextChangedListener(CurrencyTextWatcher(tilRegisterIncome))

            btnRegister.setOnClickListener {
                if (!validateFields()) {
                    return@setOnClickListener
                }

                val birthdate = tilRegisterBirthdate.text
                val formattedBirthdate = birthdate.toLocalDateApiFormat()

                val rawCpf = removeCpfFormatting(tilRegisterCpf.text)
                val rawPhone = removePhoneFormatting(tilRegisterPhone.text)
                val rawIncome = removeCurrencyFormatting(tilRegisterIncome.text)

                viewModel.registerUser(
                    RegisterRequest(
                        name = tilRegisterName.text,
                        cpf = rawCpf,
                        email = tilRegisterEmail.text,
                        password = tilRegisterPassword.text,
                        phone = rawPhone,
                        birthdate = formattedBirthdate,
                        income = rawIncome.toDouble()
                    )
                )
            }
        }
    }

    private fun validateFields(): Boolean {
        val inputValidator = ValidateInput(this)
        val isNameValid = inputValidator.validateInputText(binding.tilRegisterName)
        val isCpfValid = inputValidator.validateInputText(binding.tilRegisterCpf) && inputValidator.validateCpf(binding.tilRegisterCpf)
        val isEmailValid = inputValidator.validateInputText(binding.tilRegisterEmail) && inputValidator.validateEmail(binding.tilRegisterEmail)
        val isPasswordValid = inputValidator.validateInputText(binding.tilRegisterPassword) && inputValidator.validatePassword(binding.tilRegisterPassword)
        val isPhoneValid = inputValidator.validateInputText(binding.tilRegisterPhone) && inputValidator.validatePhone(binding.tilRegisterPhone)
        val isBirthdateValid = inputValidator.validateInputText(binding.tilRegisterBirthdate)
        val isIncomeValid = inputValidator.validateInputText(binding.tilRegisterIncome)

        return isNameValid && isCpfValid && isEmailValid && isPasswordValid && isPhoneValid && isBirthdateValid && isIncomeValid
    }

    private fun showDatePickerDialog(tag: String, actualDate: LocalDate = LocalDate.now()) {
        Log.i("AddFinanceActivity", "showDatePickerDialog: $actualDate")
        val datePicker = DatePickerFragment.newInstance(this, actualDate)
        datePicker.show(supportFragmentManager, "datePicker$tag")
    }

    override fun onStart() {
        super.onStart()
        viewModel.registerState.observe(this) {
            when (it) {
                is State.Loading -> {
                    progressDialog = getProgressDialog(it.loadingMessage)
                    progressDialog?.show()
                }

                is State.Success -> {
                    progressDialog?.dismiss()

                    val user = it.info
                    Toast.makeText(this, getString(R.string.txt_register_success), Toast.LENGTH_SHORT).show()

                    val intent = Intent(this, LoginActivity::class.java).apply {
                        putExtra(EXTRA_USER_EMAIL, user.email)
                    }

                    startActivity(intent)
                }

                is State.Error -> {
                    progressDialog?.dismiss()
                    createDialog {
                        setTitle(getString(R.string.txt_error))
                        setMessage(getUserFriendlyErrorMessage(it.error))
                        setPositiveButton(android.R.string.ok, null)
                    }
                }
            }
        }
    }

    companion object {
        const val EXTRA_USER_EMAIL = "USER_EMAIL"
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val monthString = if (month < 9) "0${month + 1}" else "${month + 1}"
        val dayString = if (dayOfMonth < 10) "0$dayOfMonth" else "$dayOfMonth"
        val selectedDate = "$dayString/$monthString/$year"
        binding.tilRegisterBirthdate.text = selectedDate
    }
}