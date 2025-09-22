package com.example.library.fake

import com.example.library.data.entity.Item
import com.example.library.domain.RemoteRepository
import com.example.library.data.mapper.toItem

class FakeNetworkBookRepository : RemoteRepository {
    override suspend fun searchVolume(query: String, limit: Int, offset: Int): Item {
        return FakeDataSource.getListRange(limit, offset).toItem()
    }
}