package com.example.library.data.workmanager

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.library.data.room.BookCacheDao
import com.example.library.data.workmanager.CachePolicy.MAX_CACHE_AGE
import com.example.library.data.workmanager.CachePolicy.MAX_IDLE_TIME
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class CacheCleanupWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val bookDao: BookCacheDao
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val now = System.currentTimeMillis()

        val cachedAt = now - MAX_CACHE_AGE
        val accessedAt = now - MAX_IDLE_TIME

        bookDao.deleteExpiredBooks(cachedAt, accessedAt)
        return Result.success()
    }

}