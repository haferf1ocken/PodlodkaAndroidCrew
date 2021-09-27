package com.example.podlodkaandroidcrew.data

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Throwable) : Result<Nothing>()

    companion object {
        inline fun <T> on(f: () -> T) : Result<T> = try {
            Success(f())
        } catch (ex: Exception) {
            Error(ex)
        }
    }
}