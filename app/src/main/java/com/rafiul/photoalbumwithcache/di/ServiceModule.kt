package com.rafiul.photoalbumwithcache.di


import com.rafiul.photoalbumwithcache.source.remote.PhotoApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class ServiceModule {

    @Singleton
    @Provides
    fun providesPhotoApi(retrofit: Retrofit.Builder): PhotoApi {
        return retrofit.build().create(PhotoApi::class.java)
    }
}