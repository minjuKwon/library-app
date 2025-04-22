package com.example.library.fake

import com.example.library.data.BookshelfRepository
import com.example.library.network.Item
import okio.IOException

class FakeNetworkBookshelfRepository :BookshelfRepository {
    override suspend fun getBookListInformation(query: String, count: Int, startIndex: Int): Item {
        return FakeDataSource.item
    }
}

class FakeBookmarkedBookshelfRepository:BookshelfRepository{
    override suspend fun getBookListInformation(query: String, count: Int, startIndex: Int): Item {
        return FakeDataSource.itemBookmarked
    }
}

class FakeExceptionBookshelfRepository:BookshelfRepository{
    override suspend fun getBookListInformation(query: String, count: Int, startIndex: Int): Item {
        throw IOException("fake exception repository")
    }
}