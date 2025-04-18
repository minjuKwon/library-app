package com.example.library.fake

import com.example.library.network.BookshelfApiService
import com.example.library.network.Item

class FakeBookshelfApiService :BookshelfApiService {
    override suspend fun getInformation(query: String, count: Int, startIndex: Int): Item {
        return FakeDataSource.item
    }
}