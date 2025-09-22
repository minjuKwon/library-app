package com.example.library.data.fake

import com.example.library.data.entity.Item
import com.example.library.data.mapper.toItem
import com.example.library.domain.RemoteRepository
import kotlinx.coroutines.delay

class FakeNetworkBookRepository : RemoteRepository{
    override suspend fun searchVolume(query: String, limit: Int, offset: Int): Item {
        delay(3000)
        return FakeDataSource.getListRange(limit, offset).toItem()
    }
}