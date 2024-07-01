package com.rafiul.photoalbumwithcache


import android.app.Application
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.rafiul.photoalbumwithcache.worker.PhotoSyncWorker
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit

@HiltAndroidApp
class PhotoAlbumWithCacheApp : Application(){
    override fun onCreate() {
        super.onCreate()
        schedulePeriodicPhotoSync()
    }

    private fun schedulePeriodicPhotoSync() {
        val workRequest = PeriodicWorkRequestBuilder<PhotoSyncWorker>(2, TimeUnit.MINUTES)
            .build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "PhotoSync",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }
}