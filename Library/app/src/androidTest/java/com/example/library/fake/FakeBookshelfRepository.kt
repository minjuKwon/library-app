package com.example.library.fake

import com.example.library.data.BookshelfRepository
import com.example.library.network.Item

class FakeNetworkBookshelfRepository :BookshelfRepository {
    override suspend fun searchVolume(query: String, limit: Int, offset: Int): Item {
        return FakeDataSource.getListRange(limit, offset)
    }
}