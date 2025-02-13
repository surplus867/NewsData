package com.example.newsdata.core.domain

sealed class NewsResult<T>(
    val data: T? = null,
    val error: String? = null,
    val message: String? = null
) {
    class Success<T>(data: T?, message: String? = null) : NewsResult<T>(data,message = message)
    class Error<T>(error:String?, message: String? = null) : NewsResult<T>(error = error, message = message)
}