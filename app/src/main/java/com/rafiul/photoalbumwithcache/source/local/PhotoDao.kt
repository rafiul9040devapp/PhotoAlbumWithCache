package com.rafiul.photoalbumwithcache.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rafiul.photoalbumwithcache.model.entity.Photo
import kotlinx.coroutines.flow.Flow


@Dao
interface PhotoDao {

    @Query("SELECT * FROM photos ORDER BY id DESC")
    fun getAllPhotos(): Flow<List<Photo>>

    @Query("SELECT * FROM photos ORDER BY id DESC LIMIT 20")
    fun getTop20Photos(): Flow<List<Photo>>

    @Query("SELECT * FROM photos WHERE id BETWEEN 30 AND 60 ORDER BY id DESC")
    fun getPhotosBetween30And60(): Flow<List<Photo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(photos: List<Photo>)

    @Query("DELETE FROM photos")
    suspend fun clearAll()
}