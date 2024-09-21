package com.fitfinance.app.util

import android.text.Editable
import android.text.TextWatcher
import com.google.android.material.textfield.TextInputLayout
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale

fun removeCpfFormatting(cpf: String): String {
    return cpf.replace("\\D".toRegex(), "")
}

fun removePhoneFormatting(phone: String): String {
    return phone.replace("\\D".toRegex(), "")
}

fun removeCurrencyFormatting(currency: String): String {
    val cleanString = currency.replace("[R$]".toRegex(), "")
    return cleanString.replace(",", "")
}

fun convertDateFormat(birthdate: String): String? {
    return try {
        val inputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val date = LocalDate.parse(birthdate, inputFormatter)
        date.format(outputFormatter)
    } catch (e: DateTimeParseException) {
        null
    }
}

class CpfTextWatcher(private val textInputLayout: TextInputLayout) : TextWatcher {
    private var isUpdating = false
    private val mask = "###.###.###-##"

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (isUpdating) {
            isUpdating = false
            return
        }

        var str = s.toString().replace("\\D".toRegex(), "")
        if (str.length > 11) str = str.substring(0, 11)

        var formatted = ""
        var i = 0
        for (m in mask.toCharArray()) {
            if (m != '#') {
                formatted += m
                continue
            }
            if (i >= str.length) break
            formatted += str[i]
            i++
        }

        isUpdating = true
        textInputLayout.editText?.setText(formatted)
        textInputLayout.editText?.setSelection(formatted.length)
    }

    override fun afterTextChanged(s: Editable?) {}
}

class CurrencyTextWatcher(private val textInputLayout: TextInputLayout) : TextWatcher {
    private var current = ""

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (s.toString() != current) {
            textInputLayout.editText?.removeTextChangedListener(this)

            val locale = Locale.US

            val cleanString = s.toString().replace("\\D".toRegex(), "")
            val parsed = cleanString.toDoubleOrNull() ?: 0.0

            val formatted = NumberFormat.getCurrencyInstance(locale).format(parsed / 100)

            current = formatted
            textInputLayout.editText?.setText(formatted)
            textInputLayout.editText?.setSelection(formatted.length)

            textInputLayout.editText?.addTextChangedListener(this)
        }
    }

    override fun afterTextChanged(s: Editable?) {}
}

class PhoneTextWatcher(private val textInputLayout: TextInputLayout) : TextWatcher {
    private var isUpdating = false
    private val mask = "(##) #####-####"

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (isUpdating) {
            isUpdating = false
            return
        }

        var str = s.toString().replace("\\D".toRegex(), "")
        if (str.length > 11) str = str.substring(0, 11)

        var formatted = ""
        var i = 0
        for (m in mask.toCharArray()) {
            if (m != '#') {
                formatted += m
                continue
            }
            if (i >= str.length) break
            formatted += str[i]
            i++
        }

        isUpdating = true
        textInputLayout.editText?.setText(formatted)
        textInputLayout.editText?.setSelection(formatted.length)
    }

    override fun afterTextChanged(s: Editable?) {}
}