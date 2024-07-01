package com.rafiul.photoalbumwithcache.repository

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
import kotlinx.coroutines.flow.transformLatest
import javax.inject.Inject

class PhotoRepositoryImpl @Inject constructor(
    private val photoApi: PhotoApi,
    private val photoDao: PhotoDao
) : PhotoRepository, BaseRepositoryWithCache() {

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getAllPhotosFromRepo(): Flow<ApiState<List<ResponsePhotoItem>>> {
        val cachedPhotosFlow = fetchFromLocal(photoDao.getAllPhotos(), PhotoMapper::fromEntityList)
        return cachedPhotosFlow.transformLatest { cacheState ->
            if (cacheState !is ApiState.Success || cacheState.data.isEmpty()){
                safeApiCall {
                    photoApi.getAllPhotosFromApi()
                }.collect{ apiState ->
                    if (apiState is ApiState.Success){
                        photoDao.clearAll()
                        photoDao.insertAll(PhotoMapper.toEntityList(apiState.data))
                    }
                    emit(apiState)
                }
            }else{
                emit(cacheState)
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun refreshPhotos() {
        safeApiCall {
            photoApi.getAllPhotosFromApi()
        }.collect{ apiState->
            if (apiState is ApiState.Success){
                photoDao.clearAll()
                photoDao.insertAll(PhotoMapper.toEntityList(apiState.data))
            }
        }
    }
}