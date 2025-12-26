package com.example.library.data.workmanager

object CachePolicy {
    const val MAX_CACHE_AGE = 30L * 24 * 60 * 60 * 1000 // 30일
    const val MAX_IDLE_TIME = 15L * 24 * 60 * 60 * 1000 // 15일
}