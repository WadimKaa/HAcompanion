package com.powakaz.hacompanion

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import jakarta.inject.Inject
import androidx.work.Configuration
import androidx.hilt.work.HiltWorkerFactory


@HiltAndroidApp
class HACompanionApp : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

}