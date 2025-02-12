package com.easternkite.pideo.core.common

sealed class Result {
    data class Success<T>(val data: T) : Result()
    data class Error(val exception: Throwable) : Result()
    data object Loading : Result()
}
