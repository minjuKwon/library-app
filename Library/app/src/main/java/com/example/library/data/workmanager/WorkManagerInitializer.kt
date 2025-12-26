package com.example.library.data.workmanager

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

object WorkManagerInitializer {
    fun init(context: Context) {
        val workRequest =
            PeriodicWorkRequestBuilder<CacheCleanupWorker>(
                1, TimeUnit.DAYS
            )
                .setConstraints(
                    Constraints.Builder()
                        .setRequiresBatteryNotLow(true)
                        .build()
                )
                .build()

        WorkManager.getInstance(context)
            .enqueueUniquePeriodicWork(
                "cache_cleanup",
                ExistingPeriodicWorkPolicy.KEEP,
                workRequest
            )
    }
}