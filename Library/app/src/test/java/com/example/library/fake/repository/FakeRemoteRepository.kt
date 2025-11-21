package com.example.library.fake.repository

import com.example.library.data.entity.Item
import com.example.library.domain.RemoteRepository
import com.example.library.data.mapper.toItem
import com.example.library.fake.FakeDataSource
import okio.IOException

class FakeNetworkBookRepository : RemoteRepository {
    override suspend fun searchVolume(query: String, limit: Int, offset: Int): Result<Item> {
        val item= FakeDataSource.item.toItem()
        return Result.success(item)
    }
}

class FakeExceptionNetworkBookRepository: RemoteRepository {
    override suspend fun searchVolume(query: String, limit: Int, offset: Int): Result<Item> {
        val exception= IOException("fake exception repository")
        return Result.failure(exception)
    }
}