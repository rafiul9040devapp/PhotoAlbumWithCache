package com.rafiul.photoalbumwithcache.repository

import com.rafiul.photoalbumwithcache.base.ApiState
import com.rafiul.photoalbumwithcache.model.response.ResponsePhotoItem
import kotlinx.coroutines.flow.Flow

interface PhotoRepository {
    suspend fun getAllPhotosFromRepo(): Flow<ApiState<List<ResponsePhotoItem>>>

    suspend fun refreshPhotos()
}