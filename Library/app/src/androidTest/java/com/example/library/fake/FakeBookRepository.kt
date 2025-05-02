package com.example.library.fake

import com.example.library.domain.BookRepository
import com.example.library.data.api.Item

class FakeNetworkBookRepository : BookRepository {
    override suspend fun searchVolume(query: String, limit: Int, offset: Int): Item {
        return FakeDataSource.getListRange(limit, offset)
    }
}