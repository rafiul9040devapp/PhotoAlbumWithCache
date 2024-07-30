package com.rafiul.photoalbumwithcache.worker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.rafiul.photoalbumwithcache.worker.WorkerRequestBuilderProvider
import com.rafiul.photoalbumwithcache.worker.photo.PhotoWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OneTimeWorkerViewModel @Inject constructor(
    private val workManager: WorkManager,
    private val requestBuilder: WorkerRequestBuilderProvider
) : ViewModel() {

    private companion object {
        const val PHOTO_ONE_TIME_WORK = "one time photo"
        const val USER_ONE_TIME_WORK = "one time user"
        const val TODO_ONE_TIME_WORK = "one time todo"
    }

    private val photoWorkRequest: OneTimeWorkRequest
        get() = requestBuilder.oneTimeWorkRequest<PhotoWorker>()
//
//    private val userWorkRequest: OneTimeWorkRequest
//        get() = requestBuilder.oneTimeWorkRequest<UserWorker>()
//
//    private val toDoWorkRequest: OneTimeWorkRequest
//        get() = requestBuilder.oneTimeWorkRequest<ToDoWorker>()
//
//    fun scheduleChainedOneTimeWork() {
//        workManager.beginWith(toDoWorkRequest)
//            .then(userWorkRequest)
//            .enqueue()
//    }

    fun scheduleOneTimeWorkForPhoto() = workManager.enqueueUniqueWork(PHOTO_ONE_TIME_WORK,ExistingWorkPolicy.APPEND_OR_REPLACE,photoWorkRequest)

//    fun scheduleOneTimeWorkForUser() = workManager.enqueueUniqueWork(USER_ONE_TIME_WORK,ExistingWorkPolicy.KEEP,userWorkRequest)
//
//    fun scheduleOneTimeWorkForToDo() = workManager.enqueue(toDoWorkRequest)


}