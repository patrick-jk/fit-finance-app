package com.fitfinance.app.util

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import com.fitfinance.app.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import retrofit2.HttpException

var TextInputLayout.text: String
    get() = editText?.text?.toString() ?: ""
    set(value) {
        editText?.setText(value)
    }

fun String.toBearerToken() = "Bearer $this"

fun String.toLocalDateApiFormat(): String {
    val date = this.split("/")
    return "${date[2]}-${date[1]}-${date[0]}"
}

fun String.toLocalDateBrFormat(): String {
    val date = this.split("-")
    return "${date[2]}/${date[1]}/${date[0]}"
}

fun View.hideSoftKeyboard() {
    val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun Context.createDialog(block: MaterialAlertDialogBuilder.() -> Unit = {}): AlertDialog {
    val builder = MaterialAlertDialogBuilder(this)
    block(builder)
    return builder.create()
}

fun Context.getProgressDialog(message: String = ""): AlertDialog {
    return createDialog {
        val padding = this@getProgressDialog.resources.getDimensionPixelOffset(R.dimen.base_margin)
        val progressBar = ProgressBar(this@getProgressDialog)
        progressBar.setPadding(padding, padding, padding, padding).apply {
            setTitle(message)
        }
        setView(progressBar)

        setPositiveButton(null, null)
        setCancelable(false)
    }
}

fun HttpException.throwRemoteException(errorMessage: String) {
    throw RemoteException(this.message ?: errorMessage)
}