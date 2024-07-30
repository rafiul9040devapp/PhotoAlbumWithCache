package com.rafiul.photoalbumwithcache.base

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.retry
import retrofit2.Response
import java.io.IOException
import java.net.UnknownHostException

abstract class BaseRepositoryWithCache {

    protected fun <T, R> fetchFromLocal(
        localDataSource: Flow<List<T>>,
        mapper: (List<T>) -> R
    ): Flow<ApiState<R>> {
        return localDataSource.map { data ->
            if (data.isNotEmpty()) {
                ApiState.Success(mapper(data))
            } else {
                ApiState.Loading
            }
        }.flowOn(Dispatchers.IO)
    }

    protected suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): Flow<ApiState<T>> =
        flow {
            emit(ApiState.Loading)
            try {
                val response = apiCall()
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null) {
                        emit(ApiState.Success(data))
                    } else {
                        emit(ApiState.Failure(IOException("Response body is null")))
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = errorBody ?: "Unknown error"
                    emit(ApiState.Failure(IOException(errorMessage)))
                }
            } catch (e: Exception) {
                if (e is UnknownHostException){
                    e.printStackTrace()
                    emit(ApiState.Failure(IOException("Check Your Internet Connection")))
                }else{
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
