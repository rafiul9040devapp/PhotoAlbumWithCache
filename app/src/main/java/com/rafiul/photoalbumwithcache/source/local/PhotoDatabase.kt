package com.rafiul.photoalbumwithcache.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rafiul.photoalbumwithcache.model.entity.Photo

private const val DATABASE_NAME = "photo_db"

@Database(entities = [Photo::class], version = 1)
abstract class PhotoDatabase : RoomDatabase() {
    abstract fun getPhotoDao(): PhotoDao

    companion object {
        private var instance: PhotoDatabase? = null
        @Synchronized
        fun getInstance(context: Context): PhotoDatabase{
            return if (instance == null){
                instance = buildDatabase(context)
                instance as PhotoDatabase
            }else{
                instance as PhotoDatabase
            }
        }
        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            PhotoDatabase::class.java,
            DATABASE_NAME
        ).build()
    }
}