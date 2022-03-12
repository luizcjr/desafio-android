package com.picpay.desafio.android.data.utils

sealed class ResultState<out T: Any> {
    object Loading : ResultState<Nothing>()
    object Empty : ResultState<Nothing>()
    data class Success<out T: Any>(val result: T) : ResultState<T>()
    data class Error(val error: Throwable) : ResultState<Nothing>()
}