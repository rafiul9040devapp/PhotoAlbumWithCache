package com.rafiul.photoalbumwithcache.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.rafiul.photoalbumwithcache.repository.PhotoRepositoryImpl
import dagger.assisted.AssistedInject
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.HttpException
import javax.inject.Inject

@HiltWorker
class PhotoSyncWorker @AssistedInject constructor(
    @ApplicationContext context: Context,
    workerParams: WorkerParameters,
    private val repository: PhotoRepositoryImpl
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            repository.refreshPhotos()
            Log.d("PhotoSyncWorker", "Photos refreshed successfully")
            Result.success()
        } catch (e: HttpException) {
            Result.retry()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}
