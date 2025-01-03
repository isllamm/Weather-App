package com.weatherapp.core


sealed class Resource<T> {
    data class Success<T>(val data: T) : Resource<T>()
    data class Error<T>(val error: AppError) : Resource<T>()
    class Loading<T> : Resource<T>()
    class Idle<T> : Resource<T>()
}
