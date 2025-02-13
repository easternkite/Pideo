package com.easternkite.pideo.core.common

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val exception: Throwable) : Result<Nothing>()
    data object Loading : Result<Nothing>()
}

val <T> Result<T>.dataOrNull get() = (this as? Result.Success)?.data
