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
import kotlinx.coroutines.flow.catch
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
        private const val TAG = "RepositoryImpl"
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getAllPhotosFromRepo(): Flow<ApiState<List<ResponsePhotoItem>>> {
        // Fetch data from local cache
        val cachedPhotosFlow = fetchFromLocal(photoDao.getAllPhotos(), PhotoMapper::fromEntityList)

        // Transform the cached flow to handle cache and network logic
        return cachedPhotosFlow.transformLatest { cacheState ->
            if (cacheState !is ApiState.Success || cacheState.data.isEmpty()) {
                // If cache is empty or outdated, make a network request
                safeApiCall {
                    photoApi.getAllPhotosFromApi()
                }.collect { apiState ->
                    when (apiState) {
                        is ApiState.Success -> {
                            // Update local cache with new data
                            handlingTheLocalSource(apiState.data)
                            // Emit updated data state
                            emit(ApiState.Success(apiState.data))
                            Log.d(TAG, "Successfully refreshed photos from network")
                        }
                        is ApiState.Failure -> {
                            // Emit failure state
                            emit(ApiState.Failure(apiState.message))
                            Log.e(TAG, "Failed to refresh photos from network: ${apiState.message}")
                        }
                        is ApiState.Loading -> emit(ApiState.Loading)
                    }
                }
            } else {
                // Emit cached data state
                emit(cacheState)
            }
        }.flowOn(Dispatchers.IO).catch { e ->
            // Handle and emit exceptions
            emit(ApiState.Failure(e))
        }
    }


    override suspend fun refreshPhotos() {
        safeApiCall {
            photoApi.getAllPhotosFromApi()
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