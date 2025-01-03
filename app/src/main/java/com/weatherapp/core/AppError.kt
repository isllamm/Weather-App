package com.weatherapp.core

sealed class AppError(val message: String?) {
    class NetworkError(message: String?) : AppError(message)
    class ApiError(message: String?) : AppError(message)
    class UnknownError(message: String?) : AppError(message)
    class ValidationError(message: String?): AppError(message)
}