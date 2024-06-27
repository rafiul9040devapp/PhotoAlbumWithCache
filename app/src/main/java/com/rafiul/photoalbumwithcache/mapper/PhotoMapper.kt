package com.rafiul.photoalbumwithcache.mapper

import com.rafiul.photoalbumwithcache.model.entity.Photo
import com.rafiul.photoalbumwithcache.model.response.ResponsePhotoItem

object PhotoMapper {

    fun fromEntityList(photoEntities: List<Photo>): List<ResponsePhotoItem> {
        return photoEntities.map {
            ResponsePhotoItem(
                id = it.id,
                albumId = it.albumId,
                url = it.url,
                thumbnailUrl = it.thumbnailUrl,
                title = it.title
            )
        }
    }

    fun toEntityList(responsePhoto: List<ResponsePhotoItem>): List<Photo> {
        return responsePhoto.map {
            Photo(
                it.id ?: 0,
                it.albumId ?: 0,
                it.title ?: "",
                it.url ?: "",
                it.thumbnailUrl ?: ""
            )
        }
    }
}
