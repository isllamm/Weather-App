package com.weatherapp.core

import retrofit2.HttpException
import java.io.IOException

suspend fun <T> safeApiCall(
    apiCall: suspend () -> T
): Resource<T> {
    return try {
        Resource.Success(apiCall())
    } catch (e: IOException) {
        Resource.Error(AppError.NetworkError("Network error occurred"))
    } catch (e: HttpException) {
        Resource.Error(AppError.ApiError("API error occurred: ${e.message}"))
    } catch (e: Exception) {
        Resource.Error(AppError.UnknownError("Unknown error occurred: ${e.message}"))
    }
}