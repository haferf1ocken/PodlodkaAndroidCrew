package com.example.podlodkaandroidcrew.ui

sealed class UiState<out T> where T : Any? {
    object Default : UiState<Nothing>()
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val errorMessage: String) : UiState<Nothing>()
}