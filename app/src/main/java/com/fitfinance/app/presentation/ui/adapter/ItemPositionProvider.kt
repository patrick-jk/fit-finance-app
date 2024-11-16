package com.fitfinance.app.presentation.ui.adapter

fun interface ItemPositionProvider {
    fun getItemPositionById(itemId: String): Int
}