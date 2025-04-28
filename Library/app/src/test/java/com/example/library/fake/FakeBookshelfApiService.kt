package com.example.library.fake

import com.example.library.network.BookshelfApiService
import com.example.library.network.Item

class FakeBookshelfApiService :BookshelfApiService {
    override suspend fun searchVolume(query: String, limit: Int, offset: Int): Item {
        return FakeDataSource.item
    }
}