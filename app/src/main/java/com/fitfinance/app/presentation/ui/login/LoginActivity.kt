package com.fitfinance.app.presentation.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.fitfinance.app.R
import com.fitfinance.app.databinding.ActivityLoginBinding
import com.fitfinance.app.presentation.MainActivity
import com.fitfinance.app.presentation.statepattern.State
import com.fitfinance.app.presentation.ui.register.RegisterActivity
import com.fitfinance.app.presentation.ui.register.RegisterActivity.Companion.EXTRA_USER_EMAIL
import com.fitfinance.app.util.ClearErrorTextWatcher
import com.fitfinance.app.util.SHARED_PREF_NAME
import com.fitfinance.app.util.ValidateInput
import com.fitfinance.app.util.createDialog
import com.fitfinance.app.util.getProgressDialog
import com.fitfinance.app.util.getUserFriendlyErrorMessage
import com.fitfinance.app.util.isInternetAvailable
import com.fitfinance.app.util.text
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LoginActivity : AppCompatActivity() {
    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private val viewModel by viewModel<LoginViewModel>()
    private val sharedPreferences by lazy { getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE) }
    private val dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    private var progressDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        if (sharedPreferences.contains(getString(R.string.pref_user_token))) checkUserSession()
        setupUi()
    }

    private fun setupUi() {
        binding.apply {
            tilEmail.editText?.addTextChangedListener(ClearErrorTextWatcher(tilEmail))
            tilPassword.editText?.addTextChangedListener(ClearErrorTextWatcher(tilPassword))

            if (intent?.hasExtra(EXTRA_USER_EMAIL) == true) {
                tilEmail.text = intent?.getStringExtra(EXTRA_USER_EMAIL).toString()
            }

            btnRegister.setOnClickListener {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            }

            btnLogin.setOnClickListener {
                if (!validateFields()) {
                    return@setOnClickListener
                }

                sharedPreferences.edit().putBoolean(getString(R.string.pref_remember_me), cbRememberMe.isChecked).apply()
                viewModel.authenticateUser(tilEmail.text, tilPassword.text)
            }
        }
    }

    private fun validateFields(): Boolean {
        val inputValidator = ValidateInput(this)
        val isEmailValid = inputValidator.validateInputText(binding.tilEmail) && inputValidator.validateEmail(binding.tilEmail)
        val isPasswordValid = inputValidator.validateInputText(binding.tilPassword) && inputValidator.validatePassword(binding.tilPassword)

        return isEmailValid && isPasswordValid
    }

    private fun checkUserSession() {
        val userToken = sharedPreferences.getString(getString(R.string.pref_user_token), null)
        val refreshToken = sharedPreferences.getString(getString(R.string.pref_refresh_token), null)
        val accessTime = sharedPreferences.getString(getString(R.string.pref_authentication_date_time), null)
        val refreshTime = sharedPreferences.getString(getString(R.string.pref_refresh_date_time), null)
        val rememberMe = sharedPreferences.getBoolean(getString(R.string.pref_remember_me), false)

        if (userToken != null && accessTime != null && refreshToken != null && refreshTime != null) {
            val loginDateTime = LocalDateTime.parse(accessTime, dateTimeFormatter)
            val refreshDateTime = LocalDateTime.parse(refreshTime, dateTimeFormatter)
            val currentDateTime = LocalDateTime.now()

            if (currentDateTime.isAfter(loginDateTime.plusDays(1)) && isInternetAvailable()) {
                if (!rememberMe || currentDateTime.isAfter(refreshDateTime.plusDays(7))) {
                    Log.i("LoginActivity", "Session Expired $rememberMe")
                    createDialog {
                        setTitle(getString(R.string.txt_session_expired))
                        setMessage(getString(R.string.txt_session_expired_description))
                        setPositiveButton(android.R.string.ok, null)
                    }.show()
                } else {
                    viewModel.refreshToken(refreshToken)
                }
            } else {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.authenticationState.observe(this) { state ->
            when (state) {
                is State.Loading -> {
                    progressDialog = getProgressDialog(getString(R.string.txt_authenticating_user))
                    progressDialog?.show()
                }

                is State.Success -> {
                    progressDialog?.dismiss()

                    sharedPreferences.edit().apply {
                        putString(getString(R.string.pref_user_token), state.info.accessToken)
                        putString(getString(R.string.pref_refresh_token), state.info.refreshToken)
                        putString(getString(R.string.pref_authentication_date_time), LocalDateTime.now().format(dateTimeFormatter))
                        putString(getString(R.string.pref_refresh_date_time), LocalDateTime.now().format(dateTimeFormatter))
                        apply()
                    }
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }

                is State.Error -> {
                    progressDialog?.dismiss()

                    createDialog {
                        setTitle(getString(R.string.txt_error))
                        setMessage(getUserFriendlyErrorMessage(state.error))
                        setPositiveButton(android.R.string.ok) { _, _ ->
                            progressDialog?.dismiss()
                            binding.tilPassword.text = ""
                        }
                    }.show()
                }
            }
        }

        viewModel.refreshTokenState.observe(this) { state ->
            when (state) {
                is State.Loading -> {
                    progressDialog = getProgressDialog(getString(R.string.txt_refreshing_token))
                    progressDialog?.show()
                }

                is State.Success -> {
                    progressDialog?.dismiss()

                    sharedPreferences.edit().apply {
                        putString(getString(R.string.pref_user_token), state.info.accessToken)
                        putString(getString(R.string.pref_refresh_token), state.info.refreshToken)
                        apply()
                    }
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }

                is State.Error -> {
                    progressDialog?.dismiss()

                    createDialog {
                        setTitle(getString(R.string.txt_error))
                        setMessage(getUserFriendlyErrorMessage(state.error))
                        setPositiveButton(android.R.string.ok, null)
                    }.show()
                }
            }
        }
    }
}