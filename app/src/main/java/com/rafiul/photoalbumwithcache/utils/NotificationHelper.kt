package com.rafiul.photoalbumwithcache.utils


import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationHelper @Inject constructor(
   @ApplicationContext private val context: Context,
    private val notificationBuilder: NotificationCompat.Builder,
    private val notificationManager: NotificationManagerCompat
) {
    companion object {
        private const val NOTIFICATION_ID = 1
        private const val NOTIFICATION_PERMISSION_REQUEST_CODE = 1001
    }


    fun showNotification(
        title: String,
        message: String
    ) {
        notificationBuilder.setContentTitle(title)
            .setContentText(message)

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestNotificationPermission(context)
            return
        }

        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun requestNotificationPermission(context: Context) {
        if (context is Activity) {
            ActivityCompat.requestPermissions(
                context,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                NOTIFICATION_PERMISSION_REQUEST_CODE
            )
        }
    }

}



