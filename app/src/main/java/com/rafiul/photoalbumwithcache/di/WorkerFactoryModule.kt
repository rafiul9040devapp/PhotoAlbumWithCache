package com.rafiul.photoalbumwithcache.di

import androidx.work.WorkerFactory
import com.rafiul.photoalbumwithcache.worker.photo.PhotoWorkerFactory
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class WorkerFactoryModuleModule {

    @Binds
    @Singleton
    abstract fun bindWorkerFactory(photoWorkerFactory: PhotoWorkerFactory): WorkerFactory
}
