package com.fitfinance.app.util

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import java.time.LocalDate

class DatePickerFragment : DialogFragment() {
    private lateinit var dateSetListener: DatePickerDialog.OnDateSetListener
    private val day by lazy {
        arguments?.getInt("day") ?: LocalDate.now().dayOfMonth
    }
    private val month by lazy {
        arguments?.getInt("month") ?: LocalDate.now().monthValue
    }
    private val year by lazy {
        arguments?.getInt("year") ?: LocalDate.now().year
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return DatePickerDialog(requireContext(), dateSetListener, year, month - 1, day)
    }

    fun onDateSet(listener: DatePickerDialog.OnDateSetListener) {
        dateSetListener = listener
    }

    companion object {
        fun newInstance(listener: DatePickerDialog.OnDateSetListener, actualDate: LocalDate = LocalDate.now()): DatePickerFragment {
            val instance = DatePickerFragment().apply {
                arguments = Bundle().apply {
                    putInt("year", actualDate.year)
                    putInt("month", actualDate.monthValue)
                    putInt("day", actualDate.dayOfMonth)
                }
            }
            instance.onDateSet(listener)
            return instance
        }
    }
}