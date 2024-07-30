package com.rafiul.photoalbumwithcache.di

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkManager
import androidx.work.WorkerFactory
import com.rafiul.photoalbumwithcache.worker.DelegatingWorkerFactory
import com.rafiul.photoalbumwithcache.worker.photo.PhotoWorker
import com.rafiul.photoalbumwithcache.worker.photo.PhotoWorkerFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class WorkerModule {

    @Singleton
    @Provides
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager = WorkManager.getInstance(context)

    @Provides
    @Singleton
    fun provideWorkerFactories(
        photoWorkerFactory: PhotoWorkerFactory,
        // Add other worker factories here as needed
    ): Map<Class<out ListenableWorker>, WorkerFactory> {
        return mapOf(
            PhotoWorker::class.java to photoWorkerFactory
            // Add other worker factory bindings here
        )
    }

}

