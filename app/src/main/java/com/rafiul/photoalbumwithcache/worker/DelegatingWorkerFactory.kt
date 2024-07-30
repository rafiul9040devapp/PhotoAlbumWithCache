package com.rafiul.photoalbumwithcache.worker

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import javax.inject.Inject

class DelegatingWorkerFactory @Inject constructor(
    private val factories: Map<Class<out ListenableWorker>, @JvmSuppressWildcards WorkerFactory>
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return try {
            val workerClass = Class.forName(workerClassName).asSubclass(ListenableWorker::class.java)
            val factory = factories[workerClass] ?: findFactoryForClass(workerClass)
            factory?.createWorker(appContext, workerClassName, workerParameters)
        } catch (e: ClassNotFoundException) {
            null
        }
    }

    private fun findFactoryForClass(workerClass: Class<out ListenableWorker>): WorkerFactory? {
        for ((key, factory) in factories) {
            if (key.isAssignableFrom(workerClass)) {
                return factory
            }
        }
        return null
    }
}




