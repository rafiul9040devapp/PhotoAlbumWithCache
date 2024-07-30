package com.rafiul.photoalbumwithcache.utils

import android.util.Log
import androidx.work.WorkInfo

fun handleWorkInfoState(workInfo: WorkInfo, workerTag: String) {
    when (workInfo.state) {
        WorkInfo.State.ENQUEUED -> {
            Log.d(workerTag, "Enqueue")
        }

        WorkInfo.State.RUNNING -> {
            Log.d(workerTag, "Running")
        }

        WorkInfo.State.SUCCEEDED -> {
            Log.d(workerTag, "Success")
        }

        WorkInfo.State.FAILED -> {
            Log.d(workerTag, "Failed")
        }

        WorkInfo.State.BLOCKED -> {
            Log.d(workerTag, "Blocked")
        }

        WorkInfo.State.CANCELLED -> {
            Log.d(workerTag, "Canceled")
        }
    }
}