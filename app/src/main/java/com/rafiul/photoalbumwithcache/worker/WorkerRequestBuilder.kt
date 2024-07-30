package com.rafiul.photoalbumwithcache.worker

import androidx.work.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkerRequestBuilderProvider @Inject constructor() {

    companion object {
        const val INITIAL_DELAY_DURATION: Long = 10
        const val REPETITION_DELAY_DURATION: Long = 15
    }

    val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED).build()
//        .setRequiresCharging(false)

    inline fun <reified T : ListenableWorker> oneTimeWorkRequest(
        initialDelay: Long = INITIAL_DELAY_DURATION
    ): OneTimeWorkRequest {
        return OneTimeWorkRequestBuilder<T>().setInitialDelay(
            duration = initialDelay, timeUnit = TimeUnit.SECONDS
        ).setBackoffCriteria(
            backoffPolicy = BackoffPolicy.LINEAR,
            backoffDelay = REPETITION_DELAY_DURATION,
            timeUnit = TimeUnit.SECONDS
        ).build()
    }

    inline fun <reified T : ListenableWorker> periodicWorkRequest(
        repeatInterval: Long = 15,
        repeatIntervalTimeUnit: TimeUnit = TimeUnit.MINUTES,
        flexTimeInterval: Long = 10,
        flexTimeIntervalUnit: TimeUnit = TimeUnit.MINUTES
    ): PeriodicWorkRequest {
        return PeriodicWorkRequestBuilder<T>(
            repeatInterval, repeatIntervalTimeUnit, flexTimeInterval, flexTimeIntervalUnit
        ).setBackoffCriteria(
            backoffPolicy = BackoffPolicy.LINEAR,
            backoffDelay = REPETITION_DELAY_DURATION,
            timeUnit = TimeUnit.SECONDS
        ).setConstraints(constraints).build()
    }
}
