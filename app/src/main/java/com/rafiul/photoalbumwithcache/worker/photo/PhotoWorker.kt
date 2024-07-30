package com.rafiul.photoalbumwithcache.worker.photo

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.rafiul.photoalbumwithcache.repository.PhotoRepository
import com.rafiul.photoalbumwithcache.utils.NotificationHelper
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.io.IOException
import java.net.UnknownHostException


@HiltWorker
class PhotoWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: PhotoRepository,
    private val notificationHelper: NotificationHelper
) : CoroutineWorker(context, workerParams) {

    private companion object {
        const val TAG = "CustomWorkerForPhoto"
    }

    override suspend fun doWork(): Result {
        return try {
            repository.refreshPhotos()
            showProgressNotification()
            Result.success()
        } catch (e: Exception) {
            if (e is UnknownHostException || e is IOException) {
                Log.d(TAG, "Retry...")
                Result.retry()
            } else {
                Log.d(TAG, e.toString())
                Result.failure()
            }
        }
    }

    private fun showProgressNotification() {
        notificationHelper.showNotification(
            "Photo In Progress",
            "Updating The Data"
        )
    }
}
