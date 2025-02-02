package com.rafiul.photoalbumwithcache.di


import com.rafiul.photoalbumwithcache.repository.PhotoRepository
import com.rafiul.photoalbumwithcache.repository.PhotoRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindPhotoRepository(photoRepositoryImpl: PhotoRepositoryImpl): PhotoRepository
}
