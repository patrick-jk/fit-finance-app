package com.fitfinance.app.util

import com.google.android.material.textfield.TextInputLayout

object ValidateInput {
    fun validateInputText(textInputLayout: TextInputLayout): Boolean {
        val inputText = textInputLayout.editText?.text.toString().trim()
        return if (inputText.isEmpty()) {
            textInputLayout.error = "${textInputLayout.hint} cannot be empty"
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
            passwordInput.error = "Password must be at least 8 characters, include uppercase, lowercase, and number"
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
            cpfInput.error = "Invalid CPF format (e.g., 123.456.789-00)"
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
            phoneInput.error = "Invalid phone format (e.g., (12) 91234-5978"
            false
        } else {
            phoneInput.error = null
            true
        }
    }
}