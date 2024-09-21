package com.fitfinance.app.presentation.statepattern

sealed class State<T> {
    data class Loading<T>(val loadingMessage: String) : State<T>()
    data class Success<T>(val info: T) : State<T>()
    data class Error<T>(val error: Throwable) : State<T>()
}