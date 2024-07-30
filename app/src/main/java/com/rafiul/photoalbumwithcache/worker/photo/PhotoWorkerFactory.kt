package com.rafiul.photoalbumwithcache.worker.photo

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.rafiul.photoalbumwithcache.repository.PhotoRepository
import com.rafiul.photoalbumwithcache.utils.NotificationHelper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PhotoWorkerFactory @Inject constructor(
    private val repository: PhotoRepository,
    private val notificationHelper: NotificationHelper
) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker ? {
        return when (workerClassName) {
            PhotoWorker::class.java.name ->
                PhotoWorker(appContext, workerParameters, repository, notificationHelper)
            else -> null
        }
    }
}