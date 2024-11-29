package com.fitfinance.app.util

import android.content.Context
import com.fitfinance.app.R
import com.google.android.material.textfield.TextInputLayout

class ValidateInput(private val context: Context) {

    fun validateInputText(textInputLayout: TextInputLayout): Boolean {
        val inputText = textInputLayout.editText?.text.toString().trim()
        return if (inputText.isEmpty()) {
            textInputLayout.error = "${textInputLayout.hint} ${context.getString(R.string.txt_required)}"
            false
        } else {
            textInputLayout.error = null
            true
        }
    }

    fun validateEmail(emailInput: TextInputLayout): Boolean {
        val email = emailInput.editText?.text.toString().trim()
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return if (!email.matches(emailPattern.toRegex())) {
            emailInput.error = "Invalid email format"
            false
        } else {
            emailInput.error = null
            true
        }
    }

    fun validatePassword(passwordInput: TextInputLayout): Boolean {
        val password = passwordInput.editText?.text.toString().trim()
        val passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$"
        return if (!password.matches(passwordPattern.toRegex())) {
            val passwordStr = context.getString(R.string.txt_hint_password).replaceFirstChar { it.uppercase() }
            passwordInput.error = "$passwordStr ${context.getString(R.string.txt_password_required)}"
            false
        } else {
            passwordInput.error = null
            true
        }
    }

    fun validateCpf(cpfInput: TextInputLayout): Boolean {
        val cpf = cpfInput.editText?.text.toString().trim()
        val cpfPattern = "^[0-9]{3}\\.[0-9]{3}\\.[0-9]{3}-[0-9]{2}$"
        return if (!cpf.matches(cpfPattern.toRegex())) {
            val cpfStr = context.getString(R.string.txt_hint_cpf)
            cpfInput.error = context.getString(R.string.txt_cpf_required, cpfStr)
            false
        } else {
            cpfInput.error = null
            true
        }
    }

    fun validatePhone(phoneInput: TextInputLayout): Boolean {
        val phone = phoneInput.editText?.text.toString().trim()
        val phonePattern = "^\\([1-9]{2}\\) [0-9]{4,5}-[0-9]{4}$"
        return if (!phone.matches(phonePattern.toRegex())) {
            val phoneStr = context.getString(R.string.txt_hint_phone).lowercase()
            phoneInput.error = context.getString(R.string.txt_phone_required, phoneStr)
            false
        } else {
            phoneInput.error = null
            true
        }
    }
}