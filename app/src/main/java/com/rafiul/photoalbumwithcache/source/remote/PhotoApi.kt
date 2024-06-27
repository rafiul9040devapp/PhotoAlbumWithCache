package com.rafiul.photoalbumwithcache.source.remote


import com.rafiul.photoalbumwithcache.model.response.ResponsePhotoItem
import com.rafiul.photoalbumwithcache.utils.PHOTO_OF_ALBUM
import retrofit2.Response
import retrofit2.http.GET

interface PhotoApi {
    @GET(PHOTO_OF_ALBUM)
    suspend fun getAllPhotosFromApi(): Response<List<ResponsePhotoItem>>
}