package com.rafiul.photoalbumwithcache.di

import android.content.Context
import com.rafiul.photoalbumwithcache.source.local.PhotoDao
import com.rafiul.photoalbumwithcache.source.local.PhotoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context):PhotoDatabase = PhotoDatabase.getInstance(context)


    @Singleton
    @Provides
    fun provideDao(photoDatabase: PhotoDatabase):PhotoDao = photoDatabase.getPhotoDao()
}