package com.fitfinance.app.util

import android.text.Editable
import android.text.TextWatcher
import com.google.android.material.textfield.TextInputLayout
import java.text.NumberFormat
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

fun formatToCpf(cpf: String): String {
    if (cpf.contains(".") && cpf.contains("-")) return cpf
    if (cpf.length != 11) return cpf

    val formatted = cpf.replace("\\D".toRegex(), "")
    return "${formatted.substring(0, 3)}.${formatted.substring(3, 6)}.${formatted.substring(6, 9)}-${formatted.substring(9)}"
}

fun formatToPhone(phone: String): String {
    if (phone.contains("(") && phone.contains(")")) return phone

    val formatted = phone.replace("\\D".toRegex(), "")
    return "(${formatted.substring(0, 2)}) ${formatted.substring(2, 7)}-${formatted.substring(7)}"
}

fun formatToCurrency(currency: String): String {
    val parsed = currency.toDoubleOrNull() ?: 0.0
    return NumberFormat.getCurrencyInstance(Locale.US).format(parsed)
}

class CpfTextWatcher(private val textInputLayout: TextInputLayout) : TextWatcher {
    private var isUpdating = false
    private val mask = "###.###.###-##"

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        return
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (isUpdating) {
            isUpdating = false
            return
        }

        textInputLayout.error = null

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

    override fun afterTextChanged(s: Editable?) {
        return
    }
}

class CurrencyTextWatcher(private val textInputLayout: TextInputLayout) : TextWatcher {
    private var current = ""

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        return
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (s.toString() != current) {
            textInputLayout.error = null
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

    override fun afterTextChanged(s: Editable?) {
        return
    }
}

class PhoneTextWatcher(private val textInputLayout: TextInputLayout) : TextWatcher {
    private var isUpdating = false
    private val mask = "(##) #####-####"

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        return
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (isUpdating) {
            isUpdating = false
            return
        }

        textInputLayout.error = null

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

    override fun afterTextChanged(s: Editable?) {
        return
    }
}

class ClearErrorTextWatcher(private val textInputLayout: TextInputLayout) : TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        return
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        textInputLayout.error = null
    }

    override fun afterTextChanged(s: Editable?) {
        return
    }
}