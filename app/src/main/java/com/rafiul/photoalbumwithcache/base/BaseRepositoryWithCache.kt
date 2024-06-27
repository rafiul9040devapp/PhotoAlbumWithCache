package com.rafiul.photoalbumwithcache.base

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import retrofit2.Response
import java.io.IOException

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

    protected suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): Flow<ApiState<T>> = flow {
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
            e.printStackTrace()
            emit(ApiState.Failure(e))
        }
    }.flowOn(Dispatchers.IO)
}
