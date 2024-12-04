package com.fitfinance.app.util

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.fitfinance.app.R
import com.fitfinance.app.presentation.ui.adapter.ItemPositionProvider
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
    if (this.isEmpty()) return ""

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

fun Context.isInternetAvailable(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return false
    val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
    return when {
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
        else -> false
    }
}

fun Context.getUserFriendlyErrorMessage(throwable: Throwable): String {
    Log.e("Exception", throwable.message ?: "Unknown error")
    if (!isInternetAvailable()) {
        return getString(R.string.error_no_internet_connection)
    } else if (throwable is HttpException) {
        when (throwable.code()) {
            403 -> return getString(R.string.txt_invalid_credentials)
            503 -> return getString(R.string.error_service_unavailable)
        }
    }
    return getString(R.string.error_unknown)
}

fun RecyclerView.scrollToItem(itemId: String, itemPositionProvider: ItemPositionProvider) {
    val position = itemPositionProvider.getItemPositionById(itemId)
    if (position != RecyclerView.NO_POSITION) {
        this.scrollToPosition(position)
        this.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            val viewHolder = findViewHolderForAdapterPosition(position) ?: return@addOnLayoutChangeListener
            viewHolder.itemView.performLongClick()
        }
    }
}