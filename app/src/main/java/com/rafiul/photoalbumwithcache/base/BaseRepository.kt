package com.rafiul.photoalbumwithcache.base

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.retryWhen
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.UnknownHostException
import kotlin.math.pow

abstract class BaseRepository {

    private companion object {
        const val NUMBER_OF_RETRIES: Int = 3
        const val INITIAL_DELAY_IN_MILLIS: Long = 100
        const val MAX_DELAY_IN_MILLIS: Long = 1000
        const val FACTOR: Double = 2.0
    }

    suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): Flow<ApiState<T>> = flow {
        emit(ApiState.Loading)
        try {
            val response = apiCall()
            if (response.isSuccessful) {
                val dataFromResponse = response.body()
                if (dataFromResponse != null) {
                    emit(ApiState.Success(dataFromResponse))
                } else {
                    emit(ApiState.Failure(IOException("Response body is null")))
                }
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = errorBody ?: "Unknown error"
                emit(ApiState.Failure(IOException(errorMessage)))
            }
        } catch (e: Exception) {
            if (e is UnknownHostException || e is IOException) {
                emit(ApiState.Failure(e))
            } else {
                e.printStackTrace()
                emit(ApiState.Failure(e))
            }
        }
    }.retryWhen { cause, attempt ->
        val shouldRetry =
            cause is UnknownHostException || cause is IOException || cause is HttpException
        if (shouldRetry && attempt <= NUMBER_OF_RETRIES) {
            val delayTime = (INITIAL_DELAY_IN_MILLIS * FACTOR.pow(attempt.toInt())).toLong()
                .coerceAtMost(MAX_DELAY_IN_MILLIS)
            delay(delayTime)
            true
        } else {
            false
        }
    }.flowOn(Dispatchers.IO)
}