package com.rafiul.photoalbumwithcache.repository

import android.util.Log
import com.rafiul.photoalbumwithcache.base.ApiState
import com.rafiul.photoalbumwithcache.base.BaseRepositoryWithCache
import com.rafiul.photoalbumwithcache.mapper.PhotoMapper
import com.rafiul.photoalbumwithcache.model.response.ResponsePhotoItem
import com.rafiul.photoalbumwithcache.source.local.PhotoDao
import com.rafiul.photoalbumwithcache.source.remote.PhotoApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.transformLatest
import java.io.IOException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PhotoRepositoryImpl @Inject constructor(
    private val photoApi: PhotoApi,
    private val photoDao: PhotoDao
) : PhotoRepository, BaseRepositoryWithCache() {

    companion object{
        private const val TAG = "Check"
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getAllPhotosFromRepo(): Flow<ApiState<List<ResponsePhotoItem>>> {
        val cachedPhotosFlow = fetchFromLocal(photoDao.getAllPhotos(), PhotoMapper::fromEntityList)
        return cachedPhotosFlow.transformLatest { cacheState ->
            if (cacheState !is ApiState.Success || cacheState.data.isEmpty()) {
                safeApiCall {
                    photoApi.getAllPhotosFromApi()
                }.retry(3) { throwable ->
                    throwable is UnknownHostException || throwable is IOException
                }.collect { apiState ->
                    if (apiState is ApiState.Success) {
                        handlingTheLocalSource(apiState.data)
                    }
                    emit(cacheState)
                }
            } else {
                emit(cacheState)
            }
        }.flowOn(Dispatchers.IO)
    }


    override suspend fun refreshPhotos() {
        safeApiCall {
            photoApi.getAllPhotosFromApi()
        }.retry(3) { throwable ->
            (throwable is UnknownHostException || throwable is IOException).also { shouldRetry ->
                if (shouldRetry) {
                    Log.d(TAG, "Retrying due to network issue: ${throwable.message}")
                }
            }
        }.collect { apiState ->
            when (apiState) {
                is ApiState.Success -> {
                    handlingTheLocalSource(apiState.data)
                    Log.d(TAG, "Successfully refreshed photos")
                }
                is ApiState.Failure -> {
                    Log.e(TAG, "Failed to refresh photos: ${apiState.message}")
                }
                else -> {
                    Log.d(TAG, "Unexpected ApiState: $apiState")
                }
            }
        }
    }

    private suspend fun handlingTheLocalSource(data: List<ResponsePhotoItem>) {
        try {
            photoDao.clearAll()
            photoDao.insertAll(PhotoMapper.toEntityList(data))
            Log.d(TAG, "Successfully updated local database with photos")
        } catch (e: Exception) {
            Log.e(TAG, "Error updating local database: ${e.message}")
        }
    }

}