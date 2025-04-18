package com.example.library.fake

import com.example.library.data.BookshelfRepository
import com.example.library.network.Item

class FakeNetworkBookshelfRepository :BookshelfRepository {
    override suspend fun getBookListInformation(query: String, count: Int, startIndex: Int): Item {
        return FakeDataSource.item
    }
}