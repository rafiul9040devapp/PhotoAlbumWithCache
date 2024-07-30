package com.rafiul.photoalbumwithcache.worker.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.rafiul.photoalbumwithcache.worker.WorkerRequestBuilderProvider
import com.rafiul.photoalbumwithcache.worker.photo.PhotoWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PeriodicWorkerViewModel @Inject constructor(
    private val workManager: WorkManager,
    private val requestBuilder: WorkerRequestBuilderProvider
) : ViewModel() {

    private val photoWorkRequest : PeriodicWorkRequest
        get()  = requestBuilder.periodicWorkRequest<PhotoWorker>()

    val photoWorkStatus: LiveData<WorkInfo> = workManager.getWorkInfoByIdLiveData(photoWorkRequest.id)

    private companion object {
        const val PHOTO_PERIODIC_WORK = "periodic post"
        const val USER_PERIODIC_WORK = "periodic user"
        const val TODO_PERIODIC_WORK = "periodic todo"
    }

    init {
        periodicWorkerForPhoto()
    }

    private fun periodicWorkerForPhoto() {
        workManager.enqueueUniquePeriodicWork(
            PHOTO_PERIODIC_WORK,
            ExistingPeriodicWorkPolicy.KEEP,
            photoWorkRequest
        )
    }

//    fun periodicWorkerForUser() {
//        val workRequest = requestBuilder.periodicWorkRequest<UserWorker>()
//        workManager.enqueueUniquePeriodicWork(
//            USER_PERIODIC_WORK,
//            ExistingPeriodicWorkPolicy.KEEP,
//            workRequest
//        )
//        workManager.cancelUniqueWork(USER_PERIODIC_WORK)
//    }
//
//    fun periodicWorkerForToDo() {
//        val workRequest = requestBuilder.periodicWorkRequest<ToDoWorker>()
//        workManager.enqueueUniquePeriodicWork(
//            TODO_PERIODIC_WORK,
//            ExistingPeriodicWorkPolicy.KEEP,
//            workRequest
//        )
//        workManager.cancelUniqueWork(TODO_PERIODIC_WORK)
//    }

}