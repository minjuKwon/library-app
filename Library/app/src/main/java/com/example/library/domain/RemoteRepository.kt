package com.example.library.domain

import com.example.library.data.entity.Item

interface RemoteRepository {
    suspend fun searchVolume(query:String, limit:Int, offset:Int): Result<Item>
}