package com.example.library.data.fake

import com.example.library.domain.BookRepository
import com.example.library.data.api.Item
import kotlinx.coroutines.delay

class FakeNetworkBookRepository : BookRepository {
    override suspend fun searchVolume(query: String, limit: Int, offset: Int): Item {
        delay(3000)
        return FakeDataSource.getListRange(limit, offset)
    }
}