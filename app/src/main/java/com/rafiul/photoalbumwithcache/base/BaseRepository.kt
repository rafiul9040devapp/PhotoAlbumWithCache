package com.rafiul.photoalbumwithcache.base

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.retry
import retrofit2.Response
import java.io.IOException
import java.net.UnknownHostException

abstract class BaseRepository {

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
    }.retry(3) { throwable ->
        (throwable is UnknownHostException || throwable is IOException).also { shouldRetry ->
            if (shouldRetry) {
                Log.d("RetryStatus", "Retrying due to network issue: ${throwable.message}")
            }
        }
    }.flowOn(Dispatchers.IO)

}