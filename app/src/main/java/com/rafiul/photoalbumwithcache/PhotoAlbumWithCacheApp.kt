package com.rafiul.photoalbumwithcache


import android.app.Application
import android.util.Log
import androidx.work.Configuration
import com.rafiul.photoalbumwithcache.worker.DelegatingWorkerFactory
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class PhotoAlbumWithCacheApp : Application(),Configuration.Provider {
    @Inject
    lateinit var delegatingWorkerFactory: DelegatingWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .setWorkerFactory(delegatingWorkerFactory)
            .build()
}

