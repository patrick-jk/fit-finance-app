package com.fitfinance.app.presentation.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fitfinance.app.domain.response.HomeSummaryResponse

class HomeViewModel(
    private val homeSummaryResponse: HomeSummaryResponse
) : ViewModel() {
}