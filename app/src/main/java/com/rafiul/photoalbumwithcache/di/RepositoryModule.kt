package com.rafiul.photoalbumwithcache.di



import com.rafiul.photoalbumwithcache.repository.PhotoRepository
import com.rafiul.photoalbumwithcache.repository.PhotoRepositoryImpl
import com.rafiul.photoalbumwithcache.source.local.PhotoDao
import com.rafiul.photoalbumwithcache.source.remote.PhotoApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class RepositoryModule {

    @Singleton
    @Provides
    fun providePhotoRepository(api: PhotoApi,photoDao: PhotoDao): PhotoRepository = PhotoRepositoryImpl(api,photoDao)

}